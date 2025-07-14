package com.example.rehomemobileapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.Category;
import com.example.rehomemobileapp.model.Province;
import com.example.rehomemobileapp.network.ApiClient;
import com.example.rehomemobileapp.network.ApiService;
import com.example.rehomemobileapp.utils.FileUtils;

import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.util.*;

public class UploadPostActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int MAX_IMAGES = 4;
    EditText edtName, edtPrice, edtOriginalPrice, edtDescription, edtAddress;
    Spinner spinnerCategory, spinnerProvince, spinnerStatus;
    Button btnSelectImages, btnSubmit;
    LinearLayout imagePreviewContainer;
    List<Uri> selectedImageUris = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();
    List<Province> provinceList = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter, provinceAdapter;
    ApiService apiService;
    SessionManager sessionManager;
    private View loadingOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        initViews();

        sessionManager = new SessionManager();
        apiService = ApiClient.getApiService();

        // 🔥 Load from cache
        categoryList = SessionManager.getCategories(this);
        provinceList = SessionManager.getProvinces(this);

        // 🔥 Populate spinners
        populateCategorySpinner();
        populateProvinceSpinner();
        populateStatusSpinner();

        btnSelectImages.setOnClickListener(v -> pickImages());

        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                uploadPost();
            }
        });
    }

    private void populateCategorySpinner() {
        List<String> names = new ArrayList<>();
        for (Category cat : categoryList) {
            names.add(cat.getName());
        }
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void populateProvinceSpinner() {
        List<String> names = new ArrayList<>();
        for (Province p : provinceList) {
            names.add(p.getName());
        }
        provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);
    }

    private void populateStatusSpinner() {
        List<String> statuses = Arrays.asList("Mới", "Like-new", "Cũ");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }



    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtOriginalPrice = findViewById(R.id.edtOriginalPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtAddress = findViewById(R.id.edtAddress);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnSubmit = findViewById(R.id.btnSubmit);
        imagePreviewContainer = findViewById(R.id.imagePreviewContainer);
        loadingOverlay = findViewById(R.id.loadingOverlay);

    }



    private void pickImages() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_IMAGE_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data == null) return;

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < Math.min(count, MAX_IMAGES - selectedImageUris.size()); i++) {
                    selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                selectedImageUris.add(data.getData());
            }

            if (selectedImageUris.isEmpty()) {
                Toast.makeText(this, "Bạn cần chọn ít nhất 1 ảnh", Toast.LENGTH_SHORT).show();
            } else {
                renderImagePreviews();
            }
        }
    }


    private void renderImagePreviews() {
        imagePreviewContainer.removeAllViews();
        for (Uri uri : selectedImageUris) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(uri);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagePreviewContainer.addView(imageView);
        }
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(edtName.getText())) {
            edtName.setError("Vui lòng nhập tên sản phẩm");
            return false;
        }
        if (TextUtils.isEmpty(edtPrice.getText())) {
            edtPrice.setError("Vui lòng nhập giá");
            return false;
        }
        if (TextUtils.isEmpty(edtDescription.getText())) {
            edtDescription.setError("Vui lòng nhập mô tả");
            return false;
        }
        if (TextUtils.isEmpty(edtAddress.getText())) {
            edtAddress.setError("Vui lòng nhập địa chỉ");
            return false;
        }

        return true;
    }

    private void uploadPost() {
        loadingOverlay.setVisibility(View.VISIBLE);
        String token = "Bearer " + sessionManager.getAuthToken(this); // Adjust if needed

        // Convert fields to RequestBody
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edtDescription.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), edtPrice.getText().toString());
        RequestBody originalPrice = RequestBody.create(MediaType.parse("text/plain"),
                TextUtils.isEmpty(edtOriginalPrice.getText()) ? "" : edtOriginalPrice.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edtAddress.getText().toString());

        // Get selected category & province IDs
        String categoryId = categoryList.get(spinnerCategory.getSelectedItemPosition()).get_id();
        String provinceId = provinceList.get(spinnerProvince.getSelectedItemPosition()).get_id();
        String statusText = spinnerStatus.getSelectedItem().toString();

        Log.d("UploadDebug", "name: " + name.toString());
        Log.d("UploadDebug", "description: " + description.toString());
        Log.d("UploadDebug", "price: " + price.toString());
        Log.d("UploadDebug", "originalPrice: " + originalPrice.toString());
        Log.d("UploadDebug", "address: " + address.toString());
        Log.d("UploadDebug", "categoryId: " + categoryId);
        Log.d("UploadDebug", "provinceId: " + provinceId);
        Log.d("UploadDebug", "statusText: " + statusText);
        Log.d("UploadDebug", "imageCount: " + selectedImageUris.size());


        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody provinceBody = RequestBody.create(MediaType.parse("text/plain"), provinceId);
        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), statusText);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }


        // Prepare image parts
        MultipartBody.Part[] imageParts = new MultipartBody.Part[selectedImageUris.size()];
        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri uri = selectedImageUris.get(i);
            File file = new File(FileUtils.getPath(this, uri)); // Helper needed
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
            imageParts[i] = MultipartBody.Part.createFormData("images", file.getName(), imageBody);
        }

        // Make API call
        apiService.uploadPost(token, name, description, price, originalPrice, address,
                        categoryBody, provinceBody, statusBody, Arrays.asList(imageParts))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingOverlay.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Toast.makeText(UploadPostActivity.this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UploadPostActivity.this, "Thất bại: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(UploadPostActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
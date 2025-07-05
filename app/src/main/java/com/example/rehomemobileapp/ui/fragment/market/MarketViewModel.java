package com.example.rehomemobileapp.ui.fragment.market;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void fetchPosts(String province) {
        loading.setValue(true);
        ApiClient.getApiService()
                .getPostsByProvince(province)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            posts.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        loading.setValue(false);
                    }
                });
    }
}

package com.example.rehomemobileapp.network;

public class ApiConstants {
    // Use this for production
    public static final String BASE_URL = "https://rehome.id.vn";

    // If you need dev:
    // public static final String BASE_URL = "http://13.238.141.102:3000";

    // If you need local:
    // public static final String BASE_URL = "http://10.0.2.2:80";

    // Auth endpoints
    public static final String LOGIN = "/api/users/login";
    public static final String REGISTER = "/api/users/register";
    public static final String FORGOT_PASSWORD = "/api/users/forgotPassword";
    public static final String RESET_PASSWORD = "/api/users/resetPassword";
    public static final String VERIFY_RESET_CODE = "/api/users/verify-reset-code";
    public static final String LOGIN_WITH_GOOGLE = "/api/users/login/google";

    // User endpoints
    public static final String GET_USER_PROFILE = "/api/users/profile";
    public static final String GET_USER_POSTS = "/api/posts/getPersonalPosts";
    public static final String GET_ALL_USERS = "/api/users/getAllUsers";
    public static final String UPDATE_PROFILE = "/api/users/updateProfile";
    public static final String GET_COIN_HISTORY = "/api/users/getCoinHistory";
    public static final String GET_MY_SAVED_POSTS = "/api/posts/getSavedItems";
    public static final String INCREASE_COIN = "/api/users/increaseCoin";
    public static final String FIND_USER_BY_UNIQUEID = "/api/users/findUserByUniqueId";
    public static final String DELETE_USER = "/api/users/deleteUser";
    public static final String GET_ALL_COINS_HISTORY = "/api/users/getAllCoinHistory";

    // Post endpoints
    public static final String CREATE_POST = "/api/posts/createPost";
    public static final String GET_ALL_VIP_POSTS = "/api/posts/allVipPosts";
    public static final String GET_POST_DETAIL_BY_ID = "/api/posts/productDetail";
    public static final String GET_POST_BY_PROVINCE = "/api/posts/province";
    public static final String GET_ALL_CATEGORY = "/api/posts/getListCategories";
    public static final String GET_ALL_PROVINCE = "/api/posts/getListProvinces";
    public static final String GET_UNVERIFIED_POSTS = "/api/posts/unverifiedPosts";
    public static final String GET_VERIFIED_POSTS = "/api/posts/verifiedPosts";
    public static final String VERIFY_POST = "/api/posts/verify";
    public static final String ADMIN_GET_POST_DETAIL = "/api/posts/viewDetailPost";

    // Message endpoints
    public static final String GET_MESSAGES = "/api/messages/getMessages";
    public static final String SEND_MESSAGE = "/api/messages/sendMessage";
    public static final String JOIN_CONVERSATION = "/api/messages/createOrGetConversation";
    public static final String GET_USER_CONVERSATIONS = "/api/messages/getUserConversations";

    // Dynamic endpoints (with IDs) — these are used by appending the ID at runtime
    public static final String DELETE_MY_POST = "/api/posts/deletePost/"; // Append {postId}
    public static final String CHANGE_POST_STATUS = "/api/posts/status/"; // Append {postId}
    public static final String SAVED_A_POST = "/api/posts/saveItem/"; // Append {postId}
    public static final String REMOVE_MY_SAVED_POST = "/api/posts/removeSavedItem/"; // Append {postId}
}

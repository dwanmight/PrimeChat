package com.junior.dwan.primechat.data.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Might on 17.11.2016.
 */

public interface RestService {

    @GET("api.php")
    Call<ResponseBody> getLoginInfo(@Query("login") String login, @Query("passwords") String pass);

    @GET("register.php")
    Call<ResponseBody> createUser(@Query("action") String action,
                                  @Query("login") String login,
                                  @Query("passwords") String pass);

    @POST("chats.php")
    Call<ResponseBody> sendMessageToServer(@Query("action") String action,
                                           @Query("author") String author,
                                           @Query("data") String data,
                                           @Query("text") String text);

    @GET("gets.php")
    Call<ResponseBody> getChatMessages(@Query("login") String login,
                                       @Query("passwords") String passwords);
}

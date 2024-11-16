package com.example.spring_restcontroller_banuqe.config;

import com.example.spring_restcontroller_banuqe.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL="http://10.0.2.2:4000/";

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            .build();

    static  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) .addConverterFactory(GsonConverterFactory.create())
            .build();

    public  static Retrofit getRetrofit() {
        return retrofit;
    }

    public static ApiService getApi(){
        return retrofit.create(ApiService.class);

    }
}

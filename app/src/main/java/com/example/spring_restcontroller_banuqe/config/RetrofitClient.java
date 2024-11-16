package com.example.spring_restcontroller_banuqe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.spring_restcontroller_banuqe.api.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RetrofitClient {

    // URL de base de l'API
    private static final String BASE_URL = "http://10.0.2.2:8082/"; // Utiliser l'IP de l'émulateur

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApi() {
        return getRetrofitInstance().create(ApiService.class);
    }
}

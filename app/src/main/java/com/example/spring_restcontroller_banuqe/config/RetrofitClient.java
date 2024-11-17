package com.example.spring_restcontroller_banuqe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.spring_restcontroller_banuqe.api.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8082/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(boolean useJson) {
        if (retrofit == null || (retrofit != null && shouldReinitialize(useJson))) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build());

            // Choisir le convertisseur en fonction du type (JSON ou XML)
            if (useJson) {
                builder.addConverterFactory(GsonConverterFactory.create());
            } else {
                builder.addConverterFactory(SimpleXmlConverterFactory.create());
            }

            retrofit = builder.build();
        }
        return retrofit;
    }


    private static boolean shouldReinitialize(boolean useJson) {
        // Re-crée l'instance Retrofit si le convertisseur doit être changé
        return (retrofit.converterFactories().get(0) instanceof GsonConverterFactory) != useJson;
    }

    public static ApiService getApi(boolean useJson) {
        return getRetrofitInstance(useJson).create(ApiService.class);
    }
}

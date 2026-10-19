package com.example.f1hub.data.api;

import com.example.f1hub.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String JOLPICA_BASE = "https://api.jolpi.ca/ergast/f1/";
    private static final String OPENF1_BASE  = "https://api.openf1.org/v1/";

    private static JolpicaApi jolpicaApi;
    private static OpenF1Api  openF1Api;

    public static JolpicaApi jolpica() {
        if (jolpicaApi == null) {
            jolpicaApi = new Retrofit.Builder()
                    .baseUrl(JOLPICA_BASE)
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(JolpicaApi.class);
        }
        return jolpicaApi;
    }

    public static OpenF1Api openF1() {
        if (openF1Api == null) {
            openF1Api = new Retrofit.Builder()
                    .baseUrl(OPENF1_BASE)
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpenF1Api.class);
        }
        return openF1Api;
    }

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }
}

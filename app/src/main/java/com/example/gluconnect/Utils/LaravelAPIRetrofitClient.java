package com.example.gluconnect.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaravelAPIRetrofitClient {
    public static final String BASE_URL = "https://gluconnect.herokuapp.com/api/";
    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(logging)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())// Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}

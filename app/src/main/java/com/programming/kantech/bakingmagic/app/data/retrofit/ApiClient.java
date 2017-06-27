package com.programming.kantech.bakingmagic.app.data.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programming.kantech.bakingmagic.app.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by patrick keogh on 2017-06-24.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_PATH)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}


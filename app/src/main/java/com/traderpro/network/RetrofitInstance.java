package com.traderpro.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api1.pistrader.com:8055/";

    /**
     * Create an instance of Retrofit object
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static final String DATAIMG_URL = "https://s2.coinmarketcap.com/";
    private static Retrofit imgtrofit;

    public static Retrofit getImageInstance() {
        if (imgtrofit == null) {
            imgtrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(DATAIMG_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return imgtrofit;
    }
}

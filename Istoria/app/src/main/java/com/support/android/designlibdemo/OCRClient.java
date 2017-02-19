package com.support.android.designlibdemo;

/**
 * Created by brand on 2/19/2017.
 */

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OCRClient {
    @GET("/search_book.php")
    Call<Book> doQuery(
        @Query("query") String query
    );
}

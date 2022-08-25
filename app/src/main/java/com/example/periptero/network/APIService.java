package com.example.periptero.network;

import com.example.periptero.model.GsonModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("top-headlines")
    Call<GsonModel> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<GsonModel> getSpecificData(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}

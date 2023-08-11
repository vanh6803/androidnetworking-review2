package com.example.review2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    //https://64d48c1fb592423e46943d2f.mockapi.io/api/product
    ApiService instance = new Retrofit.Builder()
            .baseUrl("https://64d48c1fb592423e46943d2f.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @GET("api/product")
    Call<List<Model>> getAll();

    @POST("api/product")
    Call<Model> sendData(@Body Model model);

    @PUT("api/product/{id}")
    Call<Model> updateData(@Path("id") String id, @Body Model model);

    @DELETE("api/product/{id}")
    Call<Model> deleteData(@Path("id") String id);

}

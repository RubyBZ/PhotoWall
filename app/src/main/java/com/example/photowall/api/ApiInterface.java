package com.example.photowall.api;

import static com.example.photowall.api.ApiUtilities.API_KEY;

import com.example.photowall.madel.Image;
import com.example.photowall.madel.Search;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/photos")
    Call<List<Image>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/search/photos")
    Call<Search> searchImage(
            @Query("query") String query
    );
}

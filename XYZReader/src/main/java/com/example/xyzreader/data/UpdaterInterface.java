package com.example.xyzreader.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

@SuppressWarnings("WeakerAccess")
public interface UpdaterInterface {
    @GET
    Call<List<Article>> getArticles(@Url String url);
}

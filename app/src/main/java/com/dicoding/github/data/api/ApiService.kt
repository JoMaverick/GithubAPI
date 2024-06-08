package com.dicoding.github.data.api

import com.dicoding.github.data.response.DetailUserResponse
import com.dicoding.github.data.response.GithubResponse
import com.dicoding.github.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: (token enter token here)")
    fun getGithub(@Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token (token enter token here)")
    fun getDetailUser(@Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token (token enter token here)")
    fun getFollowers(@Path("username") username: String
    ): Call <ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token (token enter token here)")
    fun getFollowing(@Path("username") username: String
    ): Call <ArrayList<ItemsItem>>
}
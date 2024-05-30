package com.fauzan.githubuserapp.api

import com.fauzan.githubuserapp.model.ResponseDetailUser
import com.fauzan.githubuserapp.model.ResponseFollow
import com.fauzan.githubuserapp.model.ResponseSearch
import com.fauzan.githubuserapp.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUserListAsync(): ArrayList<UserResponse>
    @GET("search/users")
    fun search(
        @Query("q") username: String
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun followers(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollow>>

    @GET("users/{username}/following")
    fun following(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollow>>
}
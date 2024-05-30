package com.fauzan.githubuserapp.model

import com.google.gson.annotations.SerializedName

data class ResponseFollow(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String,

    @field:SerializedName("repos_url")
    val reposUrl: String,
)

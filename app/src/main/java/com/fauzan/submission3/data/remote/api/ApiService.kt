package com.fauzan.submission3.data.remote.api


import com.fauzan.submission3.data.remote.response.DetailResponse
import com.fauzan.submission3.data.remote.response.ItemsItem
import com.fauzan.submission3.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    suspend fun getUser(
        @Query("q") query: String
    ): UserResponse

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowerUser(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>
}
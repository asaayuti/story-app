package com.example.storyapp.data.api

import com.example.storyapp.data.api.response.ErrorResponse
import com.example.storyapp.data.api.response.LoginResponse
import com.example.storyapp.data.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ErrorResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query ("page") page: Int = 1,
        @Query ("size") size: Int = 20
    ): StoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query ("location") location : Int = 1
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStoryWithLocation(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part ("description") description: RequestBody,
        @Part ("lat") lat: RequestBody,
        @Part ("lon") lon: RequestBody
    ): ErrorResponse
}
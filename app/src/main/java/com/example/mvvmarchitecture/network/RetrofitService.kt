package com.example.mvvmarchitecture.network

import com.example.mvvmarchitecture.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    @GET("posts")
    suspend fun getPosts() : PostsDataClass

    @GET("posts/{id}")
    suspend fun getSinglePost(@Path("id") id :String) :PostsDataClassItem

    @GET("posts/{id}/comments")
    suspend fun getPostComments(@Path("id") id :String) : CommentsDataClass

    //Make a new post
    @POST("posts")
    suspend fun makePost(@Body post : AddPostData) : Response<AddPostData>


    //MVC Implementation

    @GET("posts")
    fun getAllPosts() :Call<ArrayList<PostsDataClassItem>>

    //Make a new post
    @POST("posts")
    fun makeNewPost(@Body post : AddPostData) : Call<AddPostData>
}
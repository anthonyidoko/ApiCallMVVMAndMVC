package com.example.mvvmarchitecture.data


data class CommentsDataClassItem(
    var body: String,
    var email: String,
    var id: Int,
    var name: String,
    val postId: Int
)
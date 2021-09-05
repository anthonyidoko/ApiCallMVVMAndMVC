package com.example.mvvmarchitecture.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsDataClassItem(
    val body: String,
    var id: Int,
    val title: String,
    val userId: Int
) :Parcelable


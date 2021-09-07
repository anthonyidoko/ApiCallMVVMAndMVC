package com.example.mvvmarchitecture.utils

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClass
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.ui.PostDetailActivity
import com.example.mvvmarchitecture.viewModel.PostsViewModel

const val BASE_URL = "https://jsonplaceholder.typicode.com"
//val postViewModel: PostsViewModel = ViewModelProvider(PostsActiviy)[PostsViewModel::class.java]



package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.databinding.ActivityAddPost2Binding
import com.example.mvvmarchitecture.viewModel.PostsViewModel

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPost2Binding
    lateinit var viewModel :PostsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPost2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PostsViewModel::class.java]

        binding.btnCreatePost.setOnClickListener {
            val body = binding.myPostBody.text.toString()
            val title = binding.myPostTitle.text.toString()
            val post = AddPostData(11,1,title,body)
            createPost(post)
        }
    }

    //Create a new Post
    private fun createPost(post : AddPostData){
        viewModel.makeNewPost(post)
        val myPost = PostsDataClassItem(post.body,101,post.title,11)
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra("POST_DATA",myPost)
        startActivity(intent)
    }
}
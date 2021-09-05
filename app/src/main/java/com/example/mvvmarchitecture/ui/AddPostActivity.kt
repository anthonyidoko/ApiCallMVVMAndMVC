package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.viewModel.PostsViewModel

class AddPostActivity : AppCompatActivity() {
    lateinit var viewModel :PostsViewModel
    var count = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val btnAddPost : Button = findViewById(R.id.btnNewAddPost)

        val post = AddPostData(12,12,"hello there","hello api post")

        btnAddPost.setOnClickListener {
            initViewModel(post)

            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)

        }
    }


    fun initViewModel(post : AddPostData){
        viewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        viewModel.makeNewPost(post)
        viewModel.liveDataAddedPostList.observe(this, Observer {
            if (it.isSuccessful){
//                Add the new post to the posts recyclerView
                val newPost = PostsDataClassItem(post.body,count,post.title,post.userId)
//                viewModel.immutablePostList.value?.add(newPost)
                PostsRecyclerViewAdapter().postList.add(newPost)
                count ++
                Log.d("Post",it.body().toString())
                Log.d("Post",it.message())
                Log.d("Post",it.code().toString())
            } else {
                Log.d("Post","Failed Attempt")

            }

        })

    }
}
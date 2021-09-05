package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostCommentsAdapter
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.viewModel.PostsViewModel

class PostDetailActivity : AppCompatActivity() {

    lateinit var name :TextView
    lateinit var commentsRecyclerView : RecyclerView
    lateinit var postCommentsAdapter : PostCommentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        //target the various views in the layout
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)
        val btnMakeComment = findViewById<ImageView>(R.id.btnMakeComment)
        val postTitle = findViewById<TextView>(R.id.postTitle1)
        val postBody = findViewById<TextView>(R.id.postBody1)

        btnMakeComment.setOnClickListener {
            val intent = Intent(this@PostDetailActivity, AddCommentActivity::class.java)
            startActivity(intent)
        }

        val clickedItem = intent.getParcelableExtra<PostsDataClassItem>("EXTRA_DATA")

        postBody.text = clickedItem?.body
        postTitle.text = clickedItem?.title
        initViewModel(clickedItem?.id.toString())

    }

    private fun initViewModel(postId :String) {
        val postViewModel = ViewModelProvider(this)[PostsViewModel::class.java]

        postViewModel.makeSecondAPICall(postId)
        postViewModel.immutableListCommentsLiveData.observe(this, Observer{
            if (it != null){
                commentsRecyclerView.layoutManager = LinearLayoutManager(this)
                postCommentsAdapter = PostCommentsAdapter(it)
                commentsRecyclerView.adapter = postCommentsAdapter

            } else{
                Toast.makeText(this,"no data available",Toast.LENGTH_SHORT).show()
            }
        })
    }



}

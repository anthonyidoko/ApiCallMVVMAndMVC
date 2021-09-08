package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostCommentsAdapter
import com.example.mvvmarchitecture.data.CommentsDataClassItem
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.viewModel.PostsViewModel

class PostDetailActivity : AppCompatActivity() {

    lateinit var name :TextView
    lateinit var commentsRecyclerView : RecyclerView
    private lateinit var postCommentsAdapter : PostCommentsAdapter
    lateinit var commentList:ArrayList<CommentsDataClassItem>
    private lateinit var postTitle : TextView
    private lateinit var postBody : TextView
    private lateinit var commentProgressBar :  ProgressBar
    private lateinit var btnMakeComment : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        //target the various views in the layout
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)
        btnMakeComment = findViewById(R.id.btnMakeComment)
        postTitle = findViewById<TextView>(R.id.postTitle1)
        postBody = findViewById<TextView>(R.id.postBody1)
        commentProgressBar = findViewById(R.id.myProgressBar1)

        //Initialize commentList
        commentList = ArrayList()

        btnMakeComment.setOnClickListener {

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
                commentList.addAll(it)
                commentProgressBar.visibility = View.GONE
                postBody.visibility = View.VISIBLE
                postTitle.visibility = View.VISIBLE
                btnMakeComment.visibility = View.VISIBLE
                populateRecyclerView()
            } else{
                Toast.makeText(this,"no data available",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateRecyclerView(){
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        postCommentsAdapter = PostCommentsAdapter(commentList)
        commentsRecyclerView.adapter = postCommentsAdapter

    }




}

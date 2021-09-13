package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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
    private lateinit var btnAddComment : Button
    private lateinit var yourEmail : EditText
    private lateinit var yourTitle : EditText
    private lateinit var yourComment : EditText
    private lateinit var postViewModel : PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        //target the various views in the layout
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)
        btnMakeComment = findViewById(R.id.btnMakeComment)
        btnAddComment = findViewById(R.id.btnAddComment)
        postTitle = findViewById(R.id.postTitle1)
        postBody = findViewById(R.id.postBody1)
        yourEmail = findViewById(R.id.yourEmail)
        yourTitle = findViewById(R.id.yourTitle)
        yourComment = findViewById(R.id.yourComment)
        commentProgressBar = findViewById(R.id.myProgressBar1)
        postViewModel = ViewModelProvider(this)[PostsViewModel::class.java]


        //Initialize commentList
        commentList = ArrayList()

        val clickedItem = intent.getParcelableExtra<PostsDataClassItem>("EXTRA_DATA")


        val id =  clickedItem?.id.toString()

        setClickListenerOnMakeCommentImageView()
//        setClickListenerOnAddCommentButton(newComment)
        btnAddComment.setOnClickListener {
            val body =  yourComment.text.toString()
            val email =  yourEmail.text.toString()
            val name =  yourTitle.text.toString()
            //create comment object
            val newComment = CommentsDataClassItem(body,email,commentList.size,name,id.toInt())
            commentList.add(newComment)
            setVisibilityForViews(false)
        }
        postBody.text = clickedItem?.body
        postTitle.text = clickedItem?.title
        initViewModel(id)
    }


    private fun initViewModel(postId :String) {
        postViewModel.makeSecondAPICall(postId)
        postViewModel.immutableListCommentsLiveData.observe(this, Observer{
            if (it != null){
                commentList.addAll(it)
                setVisibilityForViews()
                populateRecyclerView()
            } else{
                Toast.makeText(this,"no data available",Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Populate the recyclerView
    private fun populateRecyclerView(){
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        postCommentsAdapter = PostCommentsAdapter(commentList)
        commentsRecyclerView.adapter = postCommentsAdapter
    }

    //Set click listener on make comments imageView
    private fun setClickListenerOnMakeCommentImageView(){
        btnMakeComment.setOnClickListener {
            setVisibilityForViews(true)
        }
    }

    //Set Visibility on Views
    private fun setVisibilityForViews(state :Boolean){
        if (state){
            yourEmail.visibility = View.VISIBLE
            yourTitle.visibility = View.VISIBLE
            yourComment.visibility = View.VISIBLE
            btnAddComment.visibility = View.VISIBLE
            btnMakeComment.visibility = View.GONE
            yourEmail.requestFocus()
        } else{
            yourEmail.visibility = View.GONE
            yourTitle.visibility = View.GONE
            yourComment.visibility = View.GONE
            btnAddComment.visibility = View.GONE
            btnMakeComment.visibility = View.VISIBLE
        }
    }

    private fun setVisibilityForViews(){
        commentProgressBar.visibility = View.GONE
        postBody.visibility = View.VISIBLE
        postTitle.visibility = View.VISIBLE
        btnMakeComment.visibility = View.VISIBLE
    }

}

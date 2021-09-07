package com.example.mvvmarchitecture.ui

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClass
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.viewModel.PostsViewModel
import java.util.*
import kotlin.collections.ArrayList

class PostsActivity : AppCompatActivity(){
    lateinit var postsRecyclerViewAdapter: PostsRecyclerViewAdapter
    lateinit var postsRecyclerView: RecyclerView
    lateinit var postViewModel : PostsViewModel
    lateinit var searchLiveData :MutableLiveData<PostsDataClass>
    lateinit var adapterList :ArrayList<PostsDataClassItem>

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_actvivity)

        //Get reference to all views in the layout
        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        val btnAddPost = findViewById<ImageView>(R.id.btnAddPost)
        val btnCreatePost = findViewById<Button>(R.id.btnCreatePost)
        val newPostTitle = findViewById<TextView>(R.id.newPostTitle)
        val myPostTitle = findViewById<EditText>(R.id.myPostTitle)
        val myPostBody = findViewById<EditText>(R.id.myPostBody)


        //Create an instance of the PostsViewModel class
        postViewModel = ViewModelProvider(this)[PostsViewModel::class.java]

        populateRecyclerView()

        //Initialize the adapterList
        adapterList = ArrayList()

        postsRecyclerViewAdapter = PostsRecyclerViewAdapter()
        postsRecyclerView.layoutManager = LinearLayoutManager(this)

            //Initialize the mutableLiveData that holds the searched items value
            searchLiveData = MutableLiveData()

            btnAddPost.setOnClickListener {
                //Set visibility of the views
                btnCreatePost.visibility = View.VISIBLE
                newPostTitle.visibility = View.VISIBLE
                myPostTitle.visibility = View.VISIBLE
                myPostBody.visibility = View.VISIBLE
                myPostTitle.requestFocus()
                btnAddPost.visibility = View.GONE
            }

            btnCreatePost.setOnClickListener {
                //Extract inputs from the edit text and store in variables
                val post = AddPostData(
                    11,adapterList.size,
                    myPostTitle.text.toString(),
                    myPostBody.text.toString()
                )

                //Create Post
                createPost(post)
                //Set visibility of the views
                btnAddPost.visibility = View.VISIBLE
                btnCreatePost.visibility = View.GONE
                newPostTitle.visibility = View.GONE
                myPostTitle.visibility = View.GONE
                myPostBody.visibility = View.GONE

                //Clear text input in the editText fields
                myPostTitle.text.clear()
                myPostBody.text.clear()
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        val item = menu?.findItem(R.id.mySearchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchLiveData.value?.clear()
                val searchText = newText?.toLowerCase(Locale.getDefault())
                if (searchText!!.isNotEmpty()){
                    postViewModel.immutablePostList.value?.forEach {
                        if (it.body.toLowerCase(Locale.getDefault()).contains(searchText)){
                            searchLiveData.value?.add(it)
                        }

                    }

                    postsRecyclerViewAdapter.notifyDataSetChanged()
                }else{
                    searchLiveData.value?.clear()
                    postViewModel.immutablePostList.value?.let { searchLiveData.value?.addAll(it) }
                    postsRecyclerViewAdapter.notifyDataSetChanged()
                }

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    //Fetch data from api and display on recyclerView
    private fun populateRecyclerView(){
        postViewModel.makeAPICall()
        postViewModel.immutablePostList.observe(this, Observer <PostsDataClass>{
            if (it != null){

                //Populate adapterList
                adapterList.addAll(it)

                postsRecyclerViewAdapter.setUpdateData(adapterList)
                postsRecyclerView.adapter = postsRecyclerViewAdapter

                //Set click listener on recyclerView items
                postsRecyclerViewAdapter.setOnPostClickListener(
                    object : PostsRecyclerViewAdapter.OnPostClick{
                    override fun onPostClickListener(position: Int, view: View) {
                        val title = view.findViewById<TextView>(R.id.postTitle)
                        val body = view.findViewById<TextView>(R.id.postBody)
                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@PostsActivity,
                            Pair.create(title, "title"),
                            Pair.create(body, "body"))
                        val clickedItem:PostsDataClassItem = postsRecyclerViewAdapter.postList[position]
                        val intent = Intent(this@PostsActivity, PostDetailActivity::class.java)
                        intent.putExtra("EXTRA_DATA", clickedItem)
                        startActivity(intent,options.toBundle())
                    }
                })

            }else{
                Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show()
            }
        })

    }

    //Create new post
    private fun createPost(post : AddPostData){
        postViewModel.makeNewPost(post)
        postViewModel.liveDataAddedPostList.observe(this, Observer {
            if (it.isSuccessful){

                //Add the new post to the posts recyclerView
                val newPost = PostsDataClassItem(post.body,adapterList.size+1,post.title,11)
                adapterList.add(newPost)
                postsRecyclerViewAdapter.setUpdateData(adapterList)

            } else {
                Toast.makeText(this,"Failed Attempt",Toast.LENGTH_SHORT).show()

            }

        })

    }

}


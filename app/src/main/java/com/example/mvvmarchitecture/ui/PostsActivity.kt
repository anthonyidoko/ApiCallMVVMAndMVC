package com.example.mvvmarchitecture.ui

import android.annotation.SuppressLint
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
    private lateinit var postsRecyclerView: RecyclerView
    lateinit var postViewModel : PostsViewModel
    lateinit var searchArrayList :ArrayList<PostsDataClassItem>
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


        postViewModel = ViewModelProvider(this).get(PostsViewModel::class.java)

            initViewModel()

            //Initialize the adapterList
            adapterList = ArrayList()

            //Initialize the arrayList that holds the searched items value
            searchArrayList = ArrayList()

        postsRecyclerViewAdapter = PostsRecyclerViewAdapter()
        postsRecyclerView.layoutManager = LinearLayoutManager(this)


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
                    11,adapterList.size+1,
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

        //Reference the menu item
        val item = menu?.findItem(R.id.mySearchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                searchArrayList.clear()
                val searchText = newText?.lowercase(Locale.getDefault())
                if (searchText!!.isNotEmpty()){
                    adapterList.forEach {
                        if (it.body.lowercase(Locale.getDefault()).contains(searchText)){
                            searchArrayList.add(it)
                        }
                    }

                    postsRecyclerViewAdapter.notifyDataSetChanged()
                }else{
                    searchArrayList.clear()
                    adapterList.let { searchArrayList.addAll(it) }
                    postsRecyclerViewAdapter.notifyDataSetChanged()
                }

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViewModel(){
        postViewModel.makeAPICall()
        postViewModel.immutablePostList.observe(this, Observer <PostsDataClass>{
            if (it != null){
                adapterList.addAll(it)
                searchArrayList.addAll(adapterList)
                Log.d("searchList", "initViewModel: $searchArrayList")
            }else{
                Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show()
            }
            populateRecyclerView()
            recyclerViewClickListener()
        })

    }

    private fun createPost(post : AddPostData){
        postViewModel.makeNewPost(post)
        postViewModel.liveDataAddedPostList.observe(this, Observer {
            if (it.isSuccessful){

                //Add the new post to the posts recyclerView
                val newPost = PostsDataClassItem(post.body,adapterList.size+1,post.title,11)
                adapterList.add(newPost)
//                searchArrayList.clear()
                searchArrayList.addAll(adapterList)
                populateRecyclerView()


                Log.d("TAG", "initViewModel: ${adapterList[adapterList.size-1].body}")

                Log.d("Post",it.body().toString())
                Log.d("Post",it.message())
                Log.d("Post",it.code().toString())
            } else {
                Log.d("Post","Failed Attempt")

            }

        })

    }

    private fun populateRecyclerView(){
        postsRecyclerViewAdapter.setUpdateData(searchArrayList)
        postsRecyclerView.adapter = postsRecyclerViewAdapter
    }

    private fun recyclerViewClickListener(){
        postsRecyclerViewAdapter.setOnPostClickListener(object : PostsRecyclerViewAdapter.OnPostClick{
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
    }

}


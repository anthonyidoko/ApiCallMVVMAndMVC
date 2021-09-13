package com.example.mvvmarchitecture.ui.mvvm

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
import com.example.mvvmarchitecture.connectivity.ConnectivityLiveData
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClass
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.ui.PostDetailActivity
import com.example.mvvmarchitecture.viewModel.PostsViewModel
import java.util.*
import kotlin.collections.ArrayList

class MVVMActivity : AppCompatActivity(){

    lateinit var postsRecyclerViewAdapter: PostsRecyclerViewAdapter
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postViewModel : PostsViewModel
    lateinit var searchArrayList :ArrayList<PostsDataClassItem>
    lateinit var adapterList :ArrayList<PostsDataClassItem>
    lateinit var myProgressBar :ProgressBar
    private lateinit var btnAddPost :ImageView
    private lateinit var newPostTitle :TextView
    private lateinit var myPostTitle :EditText
    private lateinit var myPostBody :EditText
    private lateinit var connectivityLiveData: ConnectivityLiveData
    lateinit var internetText :TextView


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_actvivity)

        //Get reference to all views in the layout
        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        btnAddPost = findViewById(R.id.btnAddPost)
        myProgressBar = findViewById(R.id.myProgressBar)
        connectivityLiveData = ConnectivityLiveData(this.application)
        internetText = findViewById(R.id.internetText)


        postViewModel = ViewModelProvider(this)[PostsViewModel::class.java]

        //Call the network checker
        checkForNetwork()

        //call the network calling function
        initViewModel()

        //Initialize the adapterList
        adapterList = ArrayList()

        //Initialize the arrayList that holds the searched items value
        searchArrayList = ArrayList()

        postsRecyclerViewAdapter = PostsRecyclerViewAdapter()
        postsRecyclerView.layoutManager = LinearLayoutManager(this)

            btnAddPost.setOnClickListener {
                val intent = Intent(this, MVVMAddPostActivity::class.java)
                startActivity(intent)
            }


        // Add new Post to adapterList
        val postBody = intent.getParcelableExtra<PostsDataClassItem>("POST_DATA")
        if (postBody != null) {
            adapterList.add(postBody)
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

    //Make the api call and observe your livedata
    private fun initViewModel(){
        postViewModel.makeAPICall()

        postViewModel.immutablePostList.observe(this, Observer <PostsDataClass>{
            if (it != null){
                myProgressBar.visibility = View.GONE
                btnAddPost.visibility = View.VISIBLE
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

    //Create a new Post
    private fun createPost(post : AddPostData){
        postViewModel.makeNewPost(post)
        val newPost = PostsDataClassItem(post.body,adapterList.size+1,post.title,11)
        adapterList.add(newPost)
        searchArrayList.addAll(adapterList)
        populateRecyclerView()
    }

    //Populate the recyclerView adapter
    private fun populateRecyclerView(){
        postsRecyclerViewAdapter.setUpdateData(searchArrayList)
        postsRecyclerView.adapter = postsRecyclerViewAdapter
    }

    //Check for network availability
    private fun checkForNetwork(){
        connectivityLiveData.observe(this, Observer{isAvailable->
            when (isAvailable) {
                true -> {
                    postsRecyclerView.visibility = View.VISIBLE
                    internetText.visibility = View.GONE
                }
                false -> {
                    postsRecyclerView.visibility = View.GONE
                    internetText.visibility = View.VISIBLE
                }
            }

        })
    }

    //Set Click listener on recyclerView
    private fun recyclerViewClickListener(){
        postsRecyclerViewAdapter.setOnPostClickListener(object : PostsRecyclerViewAdapter.OnPostClick{
            override fun onPostClickListener(position: Int, view: View) {
                val title = view.findViewById<TextView>(R.id.postTitle)
                val body = view.findViewById<TextView>(R.id.postBody)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@MVVMActivity,
                    Pair.create(title, "title"),
                    Pair.create(body, "body"))
                val clickedItem:PostsDataClassItem = postsRecyclerViewAdapter.postList[position]
                val intent = Intent(this@MVVMActivity, PostDetailActivity::class.java)
                intent.putExtra("EXTRA_DATA", clickedItem)
                startActivity(intent,options.toBundle())
            }
        })
    }

    //Set visibility for the various views in layout
    private fun setVisibilityForViews(){
        //Set visibility of the views
        btnAddPost.visibility = View.VISIBLE
        newPostTitle.visibility = View.GONE
        myPostTitle.visibility = View.GONE
        myPostBody.visibility = View.GONE

        //Clear text input in the editText fields
        myPostTitle.text.clear()
        myPostBody.text.clear()
    }

    //Set click for addPost button
    private fun setClickListenerOnBtnAddPost(){
        btnAddPost.setOnClickListener {
            val intent = Intent(this, MVVMAddPostActivity::class.java)
            startActivity(intent)
        }
    }

}


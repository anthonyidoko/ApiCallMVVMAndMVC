package com.example.mvvmarchitecture.ui

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
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

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_actvivity)

        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        val btnAddPost = findViewById<ImageView>(R.id.btnAddPost)

        postViewModel = ViewModelProvider(this).get(PostsViewModel::class.java)

        postsRecyclerViewAdapter = PostsRecyclerViewAdapter()
        postsRecyclerView.layoutManager = LinearLayoutManager(this)

            //Initialize the mutableLiveData that holds the searched items value
            searchLiveData = MutableLiveData()

            btnAddPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }

        initViewModel()

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

    private fun initViewModel(){

//        postViewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        postViewModel.makeAPICall()
        postViewModel.immutablePostList.observe(this, Observer <PostsDataClass>{
            if (it != null){

//                postsRecyclerViewAdapter.setUpdateData(searchLiveDataList)
                postsRecyclerViewAdapter.setUpdateData(it)
                postsRecyclerView.adapter = postsRecyclerViewAdapter
                //Add all the values in the immutablePostList to searchLivedata
//                searchLiveData.value = postViewModel.immutablePostList.value //addAll(postViewModel.immutablePostList)

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

            }else{
                Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show()
            }
        })

    }

}


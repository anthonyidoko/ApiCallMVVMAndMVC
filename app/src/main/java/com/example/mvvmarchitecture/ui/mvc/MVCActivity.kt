package com.example.mvvmarchitecture.ui.mvc

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.adapetr.PostsRecyclerViewAdapter
import com.example.mvvmarchitecture.connectivity.ConnectivityLiveData
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.network.RetrofitClient
import com.example.mvvmarchitecture.ui.PostDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MVCActivity : AppCompatActivity() {
    lateinit var adapter :PostsRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var searchArrayList :ArrayList<PostsDataClassItem>
    lateinit var adapterList :ArrayList<PostsDataClassItem>
    lateinit var myProgressBar : ProgressBar
    private lateinit var btnAddPost : ImageView
    private lateinit var connectivityLiveData: ConnectivityLiveData
    lateinit var internetText :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_posts_actvivity)

        recyclerView = findViewById(R.id.postsRecyclerView)
        btnAddPost = findViewById(R.id.btnAddPost)
        myProgressBar = findViewById(R.id.myProgressBar)
        connectivityLiveData = ConnectivityLiveData(this.application)
        internetText = findViewById(R.id.internetText)
        adapterList = ArrayList()
        searchArrayList = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostsRecyclerViewAdapter()

        getPostsFromApi()
//        populateAdapter(adapterList)
        recyclerViewClickListener()
        setVisibilityForViews()

        //Call btnAddPost click listener method
        setClickListenerOnBtnAddPost()

        setVisibilityForViews()

        //Set visibility for btnAddPost
        btnAddPost.visibility = View.GONE

        // Add new Post to adapterList
        val postBody = intent.getParcelableExtra<PostsDataClassItem>("EXTRA_DATA")
        if (postBody != null) {
            adapterList.add(postBody)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)

        //Reference the menu item
        val item = menu?.findItem(R.id.mySearchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

                    adapter.notifyDataSetChanged()
                }else{
                    searchArrayList.clear()
                    adapterList.let { searchArrayList.addAll(it) }
                    adapter.notifyDataSetChanged()
                }

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun getPostsFromApi() {
        val retrofitData= RetrofitClient.createRetroClient().getAllPosts()
        retrofitData.enqueue(object : Callback<ArrayList<PostsDataClassItem>?> {
            override fun onResponse(
                call: Call<ArrayList<PostsDataClassItem>?>,
                response: Response<ArrayList<PostsDataClassItem>?>
            ) {
                val responseBody = response.body()!!

                //Populate adapterList with fetched data
                adapterList.addAll(responseBody)

                //Populate searchedArrayList with all posts in adapterList
                searchArrayList.addAll(adapterList)

                //Call the populateAdapter method
                populateAdapter(searchArrayList)
                myProgressBar.visibility = View.GONE
                btnAddPost.visibility = View.VISIBLE

            }

            override fun onFailure(call: Call<ArrayList<PostsDataClassItem>?>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })

    }

    private fun populateAdapter(items :ArrayList<PostsDataClassItem>){
        adapter.setUpdateData(items)
        recyclerView.adapter = adapter

    }

    //Set Click listener on recyclerView
    private fun recyclerViewClickListener(){
        adapter.setOnPostClickListener(object : PostsRecyclerViewAdapter.OnPostClick{
            override fun onPostClickListener(position: Int, view: View) {
                val title = view.findViewById<TextView>(R.id.postTitle)
                val body = view.findViewById<TextView>(R.id.postBody)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@MVCActivity,
                    Pair.create(title, "title"),
                    Pair.create(body, "body"))
                val clickedItem:PostsDataClassItem = adapter.postList[position]
                val intent = Intent(this@MVCActivity, PostDetailActivity::class.java)
                intent.putExtra("EXTRA_DATA", clickedItem)
                startActivity(intent,options.toBundle())
            }
        })
    }

    //Set visibility for the various views in layout
    private fun setVisibilityForViews(){
        //Set visibility of the views
        btnAddPost.visibility = View.VISIBLE

    }

    //Set click for addPost button
    private fun setClickListenerOnBtnAddPost(){
        btnAddPost.setOnClickListener {
            val intent = Intent(this, MVCAddPostActivity::class.java)
            startActivity(intent)
        }

    }

    //Create a new Post
    private fun createPost(post : AddPostData){
        val newPost1 = RetrofitClient.createRetroClient().makeNewPost(post)
        newPost1.enqueue(object : Callback<AddPostData?> {
            override fun onResponse(call: Call<AddPostData?>, response: Response<AddPostData?>) {
                if (response.isSuccessful){
                    val myPost = PostsDataClassItem(post.body,post.id,post.title,post.userId)
                    adapterList.add(myPost)

                }else{
                    //Do something else
                }
            }

            override fun onFailure(call: Call<AddPostData?>, t: Throwable) {
                //Do something else
            }
        })

        val newPost = PostsDataClassItem(post.body,adapterList.size+1,post.title,11)
        adapterList.add(newPost)
        searchArrayList.addAll(adapterList)
        populateAdapter(searchArrayList)
    }
}
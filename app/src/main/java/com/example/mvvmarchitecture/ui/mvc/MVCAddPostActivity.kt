package com.example.mvvmarchitecture.ui.mvc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.databinding.ActivityMvcaddPostBinding
import com.example.mvvmarchitecture.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MVCAddPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityMvcaddPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcaddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //btnCreateNewPost click listener
        binding.btnCreateNewPost.setOnClickListener {
            val title = binding.postTitle.text.toString()
            val body = binding.postBody.text.toString()
            val post = AddPostData(101,11,
                title,
                body
            )
            createPost(post)
        }
    }

    //Create a new Post
    private fun createPost(post : AddPostData){
        try {

            val newPost1 = RetrofitClient.createRetroClient().makeNewPost(post)
            newPost1.enqueue(object : Callback<AddPostData?> {
                override fun onResponse(call: Call<AddPostData?>, response: Response<AddPostData?>) {
                    if (response.isSuccessful){
                        val myPost = PostsDataClassItem(post.body,post.id,post.title,post.userId)
                        val intent = Intent(this@MVCAddPostActivity, MVCActivity::class.java)
                        intent.putExtra("EXTRA_DATA",myPost)
                        startActivity(intent)

                    }else{
                        Toast.makeText(this@MVCAddPostActivity, response.message(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddPostData?>, t: Throwable) {
                    Toast.makeText(this@MVCAddPostActivity, t.message,Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e : Throwable){
            Toast.makeText(this@MVCAddPostActivity, e.message, Toast.LENGTH_SHORT).show()
        }

    }
}
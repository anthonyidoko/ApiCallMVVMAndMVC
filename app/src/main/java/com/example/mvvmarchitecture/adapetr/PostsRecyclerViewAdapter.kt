package com.example.mvvmarchitecture.adapetr

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.PostsDataClass
import com.example.mvvmarchitecture.data.PostsDataClassItem

class PostsRecyclerViewAdapter() : RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {


    lateinit var listener :OnPostClick
    var postList = ArrayList<PostsDataClassItem>()

    fun setUpdateData(items :ArrayList<PostsDataClassItem>){
        this.postList = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View,listener :OnPostClick) :RecyclerView.ViewHolder(view){
        val postId :TextView = view.findViewById(R.id.postId)
        val postTitle :TextView = view.findViewById(R.id.postTitle)
        val postBody :TextView = view.findViewById(R.id.postBody)

        init {
            itemView.setOnClickListener{
                listener.onPostClickListener(adapterPosition,itemView)
            }
        }

    }

    interface OnPostClick{
        fun onPostClickListener( position: Int, view :View)
    }

    fun setOnPostClickListener(postListener: OnPostClick){
        listener = postListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.posts_items, parent, false
        )
        return ViewHolder(view,listener)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.postId.text = currentPost.id. toString()
        holder.postTitle.text = currentPost.title
        holder.postBody.text = """ 
                  
${currentPost.body}
            """.trimIndent()

    }

    override fun getItemCount() = postList.size

}
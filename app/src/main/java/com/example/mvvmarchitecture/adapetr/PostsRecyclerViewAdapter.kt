package com.example.mvvmarchitecture.adapetr

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        notifyItemInserted(items.size)
    }

    inner class ViewHolder(view : View,listener :OnPostClick) :RecyclerView.ViewHolder(view){
        val postId :TextView = view.findViewById(R.id.postId)
        val userId :TextView = view.findViewById(R.id.userId)
        val postTitle :TextView = view.findViewById(R.id.postTitle)
        val postBody :TextView = view.findViewById(R.id.postBody)
        val picture:ImageView = view.findViewById(R.id.pictures)

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
        holder.userId.text = currentPost.userId. toString()
        holder.postTitle.text = currentPost.title
        holder.postBody.text = """ 
                  
${currentPost.body}
            """.trimIndent()
        val imageList = listOf(
            R.drawable.xhaka,R.drawable.auba,R.drawable.cr7,R.drawable.kdb,
            R.drawable.leno,R.drawable.messi,R.drawable.neymae,R.drawable.pepe,
            R.drawable.ronaldo,R.drawable.suarez
        )
        when(holder.userId.text){
            "1" -> holder.picture.setImageResource(imageList[0])
            "2" -> holder.picture.setImageResource(imageList[1])
            "3" -> holder.picture.setImageResource(imageList[2])
            "4" -> holder.picture.setImageResource(imageList[3])
            "5" -> holder.picture.setImageResource(imageList[4])
            "6" -> holder.picture.setImageResource(imageList[5])
            "7" -> holder.picture.setImageResource(imageList[6])
            "8" -> holder.picture.setImageResource(imageList[7])
            "9" -> holder.picture.setImageResource(imageList[7])
            "10" -> holder.picture.setImageResource(imageList[9])

        }
    }

    override fun getItemCount() = postList.size

}
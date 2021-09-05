package com.example.mvvmarchitecture.adapetr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.CommentsDataClassItem

class PostCommentsAdapter(val postComments :ArrayList<CommentsDataClassItem>) :RecyclerView.Adapter<PostCommentsAdapter.ViewHolder>() {

    class ViewHolder(view : View) :RecyclerView.ViewHolder(view){
        val id : TextView = view.findViewById(R.id.commentId)
        val commentTitle : TextView = view.findViewById(R.id.commentTitle)
        val email :TextView = view.findViewById(R.id.email)
        val body :TextView = view.findViewById(R.id.commentBody)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.post_detail_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentComment = postComments[position]

        holder.id.text = currentComment.id.toString()
        holder.commentTitle.text = currentComment.name
        holder.body.text = currentComment.body
        holder.email.text = currentComment.email
    }

    override fun getItemCount() = postComments.size
}


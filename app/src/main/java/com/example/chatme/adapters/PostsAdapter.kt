package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.PostDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.posts_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostsAdapter(var list: ArrayList<PostDataClass>,var clickListener: (PostDataClass) -> Unit): RecyclerView.Adapter<PostsAdapter.PostsHolder>(){

    class PostsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(postData: PostDataClass, clickListener: (PostDataClass) -> Unit) {
            itemView.post_user_name.text = postData.name
            if (postData.date == SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                itemView.post_user_date.text = postData.time
            else
                itemView.post_user_date.text = postData.date

            Picasso.get().load(postData.userimage).into(itemView.post_user_image as ImageView)
            Picasso.get().load(postData.postimage).into(itemView.user_post_image as ImageView)

            itemView.post_user_name.setOnClickListener { clickListener(postData) }
            itemView.post_user_image.setOnClickListener { clickListener(postData) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        return PostsHolder(LayoutInflater.from(parent.context).inflate(R.layout.posts_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshdata(){
        list.reverse()
        notifyDataSetChanged()
    }

}
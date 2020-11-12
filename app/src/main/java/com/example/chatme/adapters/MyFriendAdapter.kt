package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.MyFriendData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.myfriend_list_layout.view.*

class MyFriendAdapter(var list: ArrayList<MyFriendData>,var clickListener: (MyFriendData) -> Unit): RecyclerView.Adapter<MyFriendAdapter.MyFriendHolder>() {

    class MyFriendHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(myFriend: MyFriendData, clickListener: (MyFriendData) -> Unit) {
            itemView.friend_name.text = myFriend.name
            if(myFriend.image == "ic_boy")
                itemView.friend_image.setImageResource(R.drawable.ic_boy)
            else
                Picasso.get().load(myFriend.image).into(itemView.friend_image as ImageView)

            itemView.friend_name.setOnClickListener {
                clickListener(myFriend)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFriendHolder {
        return MyFriendHolder(LayoutInflater.from(parent.context).inflate(R.layout.myfriend_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyFriendHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    fun refresh(){
        notifyDataSetChanged()
    }

}
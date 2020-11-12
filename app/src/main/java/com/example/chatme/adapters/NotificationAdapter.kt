package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.PostLikeDataClass
import com.example.chatme.Utils.RequestAcceptDataClass
import com.example.chatme.Utils.UserRequestDataClass
import com.example.chatme.Utils.utilclass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.request_accept_layout.view.*
import kotlinx.android.synthetic.main.user_like_your_post.view.*
import kotlinx.android.synthetic.main.your_request_accespt_layout.view.*

class NotificationAdapter(var list: ArrayList<Any>,var addclick: (UserRequestDataClass)->Unit,var cancelclick: (UserRequestDataClass)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class UserRequestAcceptHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind1(
            data1: UserRequestDataClass,
            addclick: (UserRequestDataClass) -> Unit,
            cancelclick: (UserRequestDataClass) -> Unit
        ) {
            if(data1.image.toString() == "ic_boy")
                itemView.user_requested_image.setImageResource(R.drawable.ic_boy)
            else
                Picasso.get().load(data1.image.toString()).into(itemView.user_requested_image as ImageView)

            itemView.user_requested_name.text = data1.name
            data1
            itemView.user_requested_accept.setOnClickListener {
                addclick(data1)
                itemView.user_requested_accept.visibility = View.GONE
                itemView.user_requested_cancel.visibility = View.GONE
            }
            itemView.user_requested_cancel.setOnClickListener {
                cancelclick(data1)
            }

            itemView.user_requested_name.setOnClickListener {
                utilclass.profileid = data1.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }

            itemView.user_requested_image.setOnClickListener {
                utilclass.profileid = data1.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }
        }
    }

    class RequestAcceptHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind2(data2: RequestAcceptDataClass) {
            if(data2.image.toString() == "ic_boy")
                itemView.user_image.setImageResource(R.drawable.ic_boy)
            else
                Picasso.get().load(data2.image.toString()).into(itemView.user_image as ImageView)
            itemView.user_name.text = data2.name

            itemView.user_name.setOnClickListener {
                utilclass.profileid = data2.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }

            itemView.user_image.setOnClickListener {
                utilclass.profileid = data2.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }
        }
    }

    class PostLikeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind3(data3: PostLikeDataClass) {
            if(data3.userimage.toString() == "ic_boy")
                itemView.user_liker_image.setImageResource(R.drawable.ic_boy)
            else
                Picasso.get().load(data3.userimage.toString()).into(itemView.user_liker_image as ImageView)
            itemView.user_liker_name.text = data3.name
            itemView.like_your_post_image.setImageResource(data3.postimage.toInt())

            itemView.user_liker_name.setOnClickListener {
                utilclass.profileid = data3.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }

            itemView.user_liker_image.setOnClickListener {
                utilclass.profileid = data3.id
                utilclass.profilebackdata = "notification"
                Navigation.findNavController(itemView).navigate(R.id.notification_to_profile) }
        }
    }

    fun removeItem(item: UserRequestDataClass){
        list.remove(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            return UserRequestAcceptHolder(LayoutInflater.from(parent.context).inflate(R.layout.request_accept_layout,parent,false))
        }
        if(viewType == 2){
            return RequestAcceptHolder(LayoutInflater.from(parent.context).inflate(R.layout.your_request_accespt_layout,parent,false))
        }
        return PostLikeHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_like_your_post,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list[position].toString() == "1")
            (holder as UserRequestAcceptHolder).bind1(list[position] as UserRequestDataClass,addclick,cancelclick)
        if (list[position].toString() == "2")
            (holder as RequestAcceptHolder).bind2(list[position] as RequestAcceptDataClass)
        if(list[position].toString() == "3")
            (holder as PostLikeHolder).bind3(list[position] as PostLikeDataClass)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        if(list[position].toString() == "1"){
            return 1
        }
        if(list[position].toString() == "2"){
            return 2
        }
        return 3
    }

    fun refreshdata(){
        list.reverse()
        notifyDataSetChanged()
    }
}
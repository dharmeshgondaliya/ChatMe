package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.MessageDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_layout.view.*

class MessageAdapter(var list: ArrayList<MessageDataClass>,var clickListener: (MessageDataClass) -> Unit): RecyclerView.Adapter<MessageAdapter.MessageHolder>(){

    class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(messageData: MessageDataClass, clickListener: (MessageDataClass) -> Unit) {
            itemView.user_message_name.text = messageData.name
            itemView.user_text_message.text = messageData.message
            itemView.user_message_time.text = messageData.time
            if(messageData.image == "ic_boy"){
                itemView.user_message_image.setImageResource(R.drawable.ic_boy)
            }
            else{
                Picasso.get().load(messageData.image).into(itemView.user_message_image as ImageView)
            }
            itemView.setOnClickListener {
                clickListener(messageData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
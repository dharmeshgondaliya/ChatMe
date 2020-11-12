package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.ImageReceiveDataClass
import com.example.chatme.Utils.ImageSentDataClass
import com.example.chatme.Utils.ReceiveMessageDataClass
import com.example.chatme.Utils.SentMessageDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_receive_layout.view.*
import kotlinx.android.synthetic.main.image_sent_layout.view.*
import kotlinx.android.synthetic.main.message_receive_layout.view.*
import kotlinx.android.synthetic.main.message_sent_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(var list: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MessageSent(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind1(sentMessage: SentMessageDataClass) {
            itemView.text_message_1.text = sentMessage.message1
            if (sentMessage.date1 == SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                itemView.message_sent_datetime.text = sentMessage.time1
            else
                itemView.message_sent_datetime.text = sentMessage.date1
        }

    }

    class MessageReceive(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind2(receiveMessage: ReceiveMessageDataClass) {
            itemView.text_message_2.text = receiveMessage.message2
            if (receiveMessage.date2 == SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                itemView.message_receive_datetime.text = receiveMessage.time2
            else
                itemView.message_receive_datetime.text = receiveMessage.date2
        }

    }

    class ImageSent(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind3(imageSent: ImageSentDataClass) {
            Picasso.get().load(imageSent.image3).into(itemView.message_sent_image as ImageView)
            if (imageSent.date3 == SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                itemView.image_sent_datetime.text = imageSent.time3
            else
                itemView.image_sent_datetime.text = imageSent.date3
        }

    }

    class ImageReceive(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind4(imageReceive: ImageReceiveDataClass) {
            Picasso.get().load(imageReceive.image4).into(itemView.message_receive_image as ImageView)
            if (imageReceive.date4 == SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                itemView.image_receive_datetime.text = imageReceive.time4
            else
                itemView.image_receive_datetime.text = imageReceive.date4
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1)
            return MessageSent(LayoutInflater.from(parent.context).inflate(R.layout.message_sent_layout,parent,false))
        if(viewType == 2)
            return MessageReceive(LayoutInflater.from(parent.context).inflate(R.layout.message_receive_layout,parent,false))
        if(viewType == 3)
            return ImageSent(LayoutInflater.from(parent.context).inflate(R.layout.image_sent_layout,parent,false))

        return ImageReceive(LayoutInflater.from(parent.context).inflate(R.layout.image_receive_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list[position].toString() == "1")
            (holder as MessageSent).bind1(list[position] as SentMessageDataClass)
        if(list[position].toString() == "2")
            (holder as MessageReceive).bind2(list[position] as ReceiveMessageDataClass)
        if(list[position].toString() == "3")
            (holder as ImageSent).bind3(list[position] as ImageSentDataClass)
        if(list[position].toString() == "4")
            (holder as ImageReceive).bind4(list[position] as ImageReceiveDataClass)
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
        if(list[position].toString() == "3"){
            return 3
        }
        return 4
    }
}
package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.StatusDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.status_layout.view.*

class StatusAdapater(var list:ArrayList<StatusDataClass>,var clickListener: (StatusDataClass) -> Unit): RecyclerView.Adapter<StatusAdapater.StatusHolder>(){

    class StatusHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(statusData: StatusDataClass, clickListener: (StatusDataClass) -> Unit) {
            itemView.status_name.text = statusData.name
            Picasso.get().load(statusData.image).into(itemView.status_image as ImageView)

            itemView.setOnClickListener {
                clickListener(statusData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusHolder {
        return StatusHolder(LayoutInflater.from(parent.context).inflate(R.layout.status_layout,parent,false))
    }

    override fun onBindViewHolder(holder: StatusHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshdata(){
        notifyDataSetChanged()
    }

}
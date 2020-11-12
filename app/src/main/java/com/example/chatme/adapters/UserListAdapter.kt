package com.example.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.Utils.UsersDataClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_list_layout.view.*

class UserListAdapter(var list: ArrayList<UsersDataClass>, var clickListener:(UsersDataClass) -> Unit,var buttonClickListener: (UsersDataClass)->Unit ):  RecyclerView.Adapter<UserListAdapter.UserViewHolser>(){

    class UserViewHolser(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(usersData: UsersDataClass, clickListener: (UsersDataClass) -> Unit,buttonClickListener: (UsersDataClass) -> Unit) {

            itemView.user_name.text = usersData.name
            if(usersData.image.toString() == "ic_boy")
                itemView.user_image.setImageResource(R.drawable.ic_boy)
            else
                Picasso.get().load(usersData.image).into(itemView.user_image as ImageView)

            itemView.user_add_button.setOnClickListener {
                if(itemView.user_add_button.text == "Add"){
                    itemView.user_add_button.text = "Request Sent"
                    itemView.user_add_button.textSize = 12F
                    usersData.button = "Add"
                    buttonClickListener(usersData)
                }
                else{
                    itemView.user_add_button.text = "Cancel"
                    itemView.user_add_button.textSize = 15F
                    android.os.Handler().postDelayed({
                        itemView.user_add_button.text = "Add"
                        usersData.button = "Cancel"
                        buttonClickListener(usersData)
                    },1500)
                }
            }
            itemView.user_image.setOnClickListener { clickListener(usersData) }
            itemView.user_name.setOnClickListener { clickListener(usersData) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolser {
        return UserViewHolser(LayoutInflater.from(parent.context).inflate(R.layout.user_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolser, position: Int) {
        holder.bind(list[position],clickListener,buttonClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
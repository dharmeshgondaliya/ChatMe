package com.example.chatme.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.R
import com.example.chatme.Utils.AuthListener
import com.example.chatme.Utils.MyFriendData
import com.example.chatme.Utils.toast
import com.example.chatme.Utils.utilclass
import com.example.chatme.adapters.MyFriendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_my_friends.*
import kotlinx.android.synthetic.main.fragment_my_friends.view.*

class MyFriendsFragment : Fragment(),AuthListener {
    lateinit var myview: View
    var users: ArrayList<MyFriendData> = ArrayList<MyFriendData>()
    var ids: ArrayList<String> = ArrayList<String>()
    lateinit var adapter: MyFriendAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myview = view
        view.recycler_my_friend.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyFriendAdapter(users,{ click -> onFriendClick(click)})
        view.recycler_my_friend.adapter = adapter

        getFriendsids()
        Handler().postDelayed({
            if(ids.size == 0){
                view.getfriendbutton.visibility = View.GONE
            }else{
                view.getfriendbutton.visibility = View.VISIBLE }},2000)

        view.getfriendbutton.setOnClickListener {
            for (i in 0..ids.size-1){
                Handler().postDelayed({
                    getFriendsDetails(ids[i])
                    view.recycler_my_friend.adapter = adapter
                    adapter.refresh()
                },500)
                adapter.refresh()
            }
        }

        view.friends_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.myfriends_to_profile)
        }

    }

    fun onFriendClick(friend: MyFriendData){
        utilclass.profileid = friend.id
        utilclass.profilebackdata = "myfriends"
        Navigation.findNavController(myview).navigate(R.id.myfriends_to_userprofile)
    }

    override fun OnStart() {

    }

    override fun OnFail(message: String) {
        requireContext().toast(message)
    }

    override fun OnSuccess() {

    }

    fun getFriendsids(){
        FirebaseDatabase.getInstance().getReference("UserManage").child(FirebaseAuth.getInstance().currentUser!!.uid).child("MyFriends").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        var uid = i.value as HashMap<String,String>
                        ids.add(uid["id"].toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

    fun getFriendsDetails(id: String){
        FirebaseDatabase.getInstance().getReference("Users").child(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var usr = snapshot.value as HashMap<String,String>
                var usid = MyFriendData(usr["id"].toString(),usr["name"].toString(),usr["image"].toString())
                if(!users.contains(usid))
                    users.add(usid)
            }
            override fun onCancelled(error: DatabaseError) { OnFail(error.message) }
        })
    }

}
package com.example.chatme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.AuthListener
import com.example.chatme.Utils.MessageDataClass
import com.example.chatme.Utils.UsersDataClass
import com.example.chatme.Utils.idss
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageUserViewModel: ViewModel() {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    private val _users = MutableLiveData<List<MessageDataClass>>()
    val users: LiveData<List<MessageDataClass>> get() = _users
    var authListener: AuthListener? = null
    var uids = ArrayList<idss>()

    fun getMesageUser(){
        var myid = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDatabase.getReference("Users").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var userlist = mutableListOf<MessageDataClass>()
                    for (i in snapshot.children) {
                        var userdatas = i.value as HashMap<String, String>
                        if (userdatas["id"].toString() != myid) {
                            if(uids.contains(idss(userdatas["id"].toString())))
                                userlist.add(MessageDataClass(userdatas["id"].toString(),userdatas["name"].toString(),"","",userdatas["image"].toString()))
                        }
                    }
                    _users.value = userlist
                }
            }
            override fun onCancelled(error: DatabaseError) {
                authListener?.OnFail(error.message.toString())
            }
        })
    }
}
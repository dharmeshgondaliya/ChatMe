package com.example.chatme.viewmodels

import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MessageViewModel: ViewModel() {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    var authListener: AuthListener? = null


    fun sentMessage(message: String, messagekey: String) {
        var map = HashMap<String,String>()
        map.put("sender",FirebaseAuth.getInstance().currentUser!!.uid)
        map.put("message",message)
        map.put("date",SimpleDateFormat("dd/M/yyyy").format(Date()))
        map.put("time",SimpleDateFormat("hh:mm a").format(Date()))
        var key: String = firebaseDatabase.getReference("UserMessage").child(messagekey).push().key.toString()
        firebaseDatabase.getReference("UserMessage").child(messagekey).child(key!!).setValue(map)
    }

    fun sentImage(url: String, messagekey: String) {
        var map = HashMap<String,String>()
        map.put("sender",FirebaseAuth.getInstance().currentUser!!.uid)
        map.put("image",url)
        map.put("date",SimpleDateFormat("dd/M/yyyy").format(Date()))
        map.put("time",SimpleDateFormat("hh:mm a").format(Date()))
        var key: String = firebaseDatabase.getReference("UserMessage").child(messagekey).push().key.toString()
        firebaseDatabase.getReference("UserMessage").child(messagekey).child(key!!).setValue(map)
        authListener?.OnFail("Image Sent Success")
    }

}
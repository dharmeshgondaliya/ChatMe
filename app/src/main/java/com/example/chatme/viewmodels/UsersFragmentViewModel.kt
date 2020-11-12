package com.example.chatme.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersFragmentViewModel: ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    var authListener: AuthListener? = null

    private val _users = MutableLiveData<List<UsersDataClass>>()
    val users: LiveData<List<UsersDataClass>> get() = _users
    var uids = ArrayList<idss>()

    fun getUsers(){
        var myid = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDatabase.getReference("Users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var userlist = mutableListOf<UsersDataClass>()
                    for (i in snapshot.children) {
                        var userdatas = i.value as HashMap<String, String>
                        if (userdatas["id"].toString() != myid) {
                            if(!uids.contains(idss(userdatas["id"].toString()))) {
                                userlist.add(UsersDataClass(userdatas["id"].toString(), userdatas["name"].toString(), userdatas["image"].toString()))
                            }
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

    fun friendRequestSent(id: String,context: Context){
        var pref = context.getSharedPreferences("chatme",0)
        var myid = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        var myname = pref.getString("username","defaultvalue").toString()
        var myimg = pref.getString("userimage","defaultvalue").toString()

        var sent = Notification12(id,"1")
        firebaseDatabase.getReference("UserManage").child(id).child("Notification").child("1+"+myid).setValue(UserRequestDataClass(myid,myname,myimg,"1"))
        firebaseDatabase.getReference("UserManage").child(myid).child("RequestSent").child("1+"+id).setValue(sent)
    }

    fun friendRequestCancel(myid: String,requestid: String){
        firebaseDatabase.getReference("UserManage").child(requestid).child("Notification").child("1+"+myid).removeValue()
        //
        firebaseDatabase.getReference("UserManage").child(myid).child("RequestSent").child("1+"+requestid).removeValue()
    }

}
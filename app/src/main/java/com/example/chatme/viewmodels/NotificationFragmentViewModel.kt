package com.example.chatme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationFragmentViewModel: ViewModel() {

    var firebaseAuth  = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var authListener: AuthListener? = null

    private var _notify = MutableLiveData<List<Any>>()
    val notificationlist: LiveData<List<Any>> get() = _notify


    fun getNotifications(){
        firebaseDatabase.getReference("UserManage").child(firebaseAuth.currentUser!!.uid).child("Notification").limitToLast(50).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var noti = ArrayList<Any>()
                    for (i in snapshot.children){
                        var notif = i.value as HashMap<String,String>
                        if(notif["type"].toString() == "1"){
                            noti.add(UserRequestDataClass(notif["id"].toString(),notif["name"].toString(),notif["image"].toString(),"1"))
                        }
                        else if(notif["type"].toString() == "2"){
                            noti.add(RequestAcceptDataClass(notif["id"].toString(),notif["name"].toString(),notif["image"].toString(),"2"))
                        }
                        else if(notif["type"].toString() == "3"){
                            noti.add(PostLikeDataClass(notif["id"].toString(),notif["name"].toString(),notif["userimage"].toString(),notif["postimage"].toString(),"3"))
                        }
                    }
                    _notify.value = noti
                }
            }
            override fun onCancelled(error: DatabaseError) { authListener?.OnFail(error.message.toString()) }
        })
    }

    fun friendRequestAccept(myid: String,requestid: UserRequestDataClass,name: String,image: String) {
        friendRequestCancel(myid,requestid.id)
        var msgkey = firebaseDatabase.getReference("UserMessage").push().key
        var friend1: HashMap<String,String> = HashMap()
        friend1.put("id",myid)
        friend1.put("messageid",msgkey!!.toString())
        var friend2: HashMap<String,String> = HashMap()
        friend2.put("id",requestid.id)
        friend2.put("messageid",msgkey!!.toString())
        firebaseDatabase.getReference("UserMessage").child(msgkey!!.toString())
        var key = firebaseDatabase.getReference("UserManage").child(requestid.id).child("Notification").push().key
        firebaseDatabase.getReference("UserManage").child(myid).child("MyFriends").child("F+"+requestid.id).setValue(friend2)
        firebaseDatabase.getReference("UserManage").child(requestid.id).child("MyFriends").child("F+"+myid).setValue(friend1)
        firebaseDatabase.getReference("UserManage").child(requestid.id).child("Notification").child(key.toString()).setValue(RequestAcceptDataClass(myid,name+"  Accept your friend request",image))
    }

    fun friendRequestCancel(myid: String,requestid: String) {
        firebaseDatabase.getReference("UserManage").child(myid).child("Notification").child("1+"+requestid).removeValue()
        //
        firebaseDatabase.getReference("UserManage").child(requestid).child("RequestSent").child("1+"+myid).removeValue()
    }

}
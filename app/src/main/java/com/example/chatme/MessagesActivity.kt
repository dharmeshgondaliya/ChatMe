package com.example.chatme

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.Utils.*
import com.example.chatme.adapters.ChatAdapter
import com.example.chatme.viewmodels.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity(),AuthListener {
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var userid: String
    lateinit var viewmodel: MessageViewModel
    lateinit var adapter: ChatAdapter
    var messagelist = ArrayList<Any>()
    var messagekey: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        userid = intent.getStringExtra("id").toString()
        messagekey = intent.getStringExtra("msgkey").toString()
        viewmodel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        viewmodel.authListener = this
        messages_toolbar_name.text = intent.getStringExtra("name").toString()
        recycler_chat.layoutManager = LinearLayoutManager(this)

        var myid = firebaseAuth.currentUser!!.uid.toString()

        firebaseDatabase.getReference("UserMessage").child(messagekey).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        var msgid = i.key.toString()
                        var msg = i.value as HashMap<String,String>
                        if(msg.containsKey("message")){
                            if(msg["sender"].toString() == myid) {
                                var sentmsg = SentMessageDataClass(msgid, msg["message"].toString(), msg["date"].toString(), msg["time"].toString())
                                if (!messagelist.contains(sentmsg))
                                    messagelist.add(sentmsg)
                            }
                            else{
                                var receivemsg = ReceiveMessageDataClass(msgid, msg["message"].toString(), msg["date"].toString(), msg["time"].toString())
                                if (!messagelist.contains(receivemsg))
                                    messagelist.add(receivemsg)
                            }
                        }
                        else {
                            if (msg["sender"].toString() == myid) {
                                var sentimg = ImageSentDataClass(msgid, msg["image"].toString(), msg["date"].toString(), msg["time"].toString())
                                if (!messagelist.contains(sentimg))
                                    messagelist.add(sentimg)
                            } else {
                                var receiveimg = ImageReceiveDataClass(msgid, msg["image"].toString(), msg["date"].toString(), msg["time"].toString())
                                if (!messagelist.contains(receiveimg))
                                    messagelist.add(receiveimg)
                            }
                        }
                    }
                    adapter = ChatAdapter(messagelist)
                    recycler_chat.adapter = adapter
                    recycler_chat.scrollToPosition(messagelist.size-1)
                }
            }
            override fun onCancelled(error: DatabaseError) { toast(error.message) }
        })


        if(intent.getStringExtra("image").toString() == "ic_boy"){
            message_toolbar_image.setImageResource(R.drawable.ic_boy)
        }
        else{
            Picasso.get().load(intent.getStringExtra("image").toString()).into(message_toolbar_image as ImageView)
        }

        message_send_button.setOnClickListener {
            var message = type_message.text.toString().trim()
            if(!message.isNullOrEmpty()){
                viewmodel.sentMessage(message,messagekey)
                type_message.text.clear()
            }
            else{
                return@setOnClickListener
            }
        }

        image_send_button.setOnClickListener {
            var i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i,"Choose Image for Send"),999)
        }
    }

    override fun OnStart() {}
    override fun OnFail(message: String) { toast(message) }
    override fun OnSuccess() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 999 && resultCode == Activity.RESULT_OK && data != null){
            var filepath = data.data!!
            var imgref: StorageReference = FirebaseStorage.getInstance().reference.child("chats").child(firebaseAuth.currentUser!!.uid).child(filepath.lastPathSegment.toString())
            imgref.putFile(filepath).addOnCompleteListener {
                if(it.isSuccessful){
                    imgref.downloadUrl.addOnSuccessListener {url->
                        viewmodel.sentImage(url.toString(),messagekey)
                    }
                }
            }
        }
    }

}
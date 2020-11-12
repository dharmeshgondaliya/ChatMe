package com.example.chatme.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.MessagesActivity
import com.example.chatme.R
import com.example.chatme.Utils.MessageDataClass
import com.example.chatme.Utils.idss
import com.example.chatme.Utils.toast
import com.example.chatme.adapters.MessageAdapter
import com.example.chatme.viewmodels.MessageUserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_message.view.*

class MessageFragment : Fragment() {
    var ids = ArrayList<idss>()
    lateinit var viewmodel:MessageUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(MessageUserViewModel::class.java)
        view.recycler_message.layoutManager = LinearLayoutManager(requireContext())

        Handler().postDelayed({
            viewmodel.uids = ids
            viewmodel.getMesageUser()
        },2000)

        viewmodel.users.observe(viewLifecycleOwner, Observer {
            view.recycler_message.adapter = MessageAdapter(it as ArrayList<MessageDataClass>,{ click -> MessageClick(click) })
        })

        view.message_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.message_to_home)
        }
    }

    private fun MessageClick(userClick: MessageDataClass) {
        var msgkey = ""
        for (i in ids){
            if(i.id == userClick.id){
                msgkey = i.key
                break
            }
        }
        var i = Intent(requireContext(),MessagesActivity::class.java)
        i.putExtra("id",userClick.id)
        i.putExtra("name",userClick.name)
        i.putExtra("image",userClick.image)
        i.putExtra("msgkey",msgkey)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        FirebaseDatabase.getInstance().getReference("UserManage").child(FirebaseAuth.getInstance().currentUser!!.uid).child("MyFriends").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        var uid = i.value as HashMap<String,String>
                        ids.add(idss(uid["id"].toString(),uid["messageid"].toString()))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

}
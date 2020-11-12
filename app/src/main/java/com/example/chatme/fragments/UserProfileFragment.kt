package com.example.chatme.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import com.example.chatme.MessagesActivity
import com.example.chatme.R
import com.example.chatme.Utils.GridDataClass
import com.example.chatme.Utils.toast
import com.example.chatme.Utils.utilclass
import com.example.chatme.adapters.GridAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

class UserProfileFragment : Fragment() {
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var msgkey: String
    lateinit var myid: String
    lateinit var myname: String
    lateinit var myimage: String
    var gridlist = ArrayList<GridDataClass>()
    lateinit var gridAdapter: GridAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase.getReference("UserManage").child(firebaseAuth.currentUser!!.uid).child("MyFriends").child("F+"+utilclass.profileid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    view.user_profile_buttons.visibility = View.VISIBLE
                    var user = snapshot.value as HashMap<String,String>
                    msgkey = user["messageid"].toString()
                }
                else{
                    view.user_profile_buttons.visibility = View.GONE
                    view.user_profile_gridview.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })

        firebaseDatabase.getReference("Users").child(utilclass.profileid!!.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.value as HashMap<String,String>
                if(user["image"].toString() == "ic_boy") {
                    view.user_profile_image.setImageResource(R.drawable.ic_boy)
                    myimage = "ic_boy"
                }else{
                    Picasso.get().load(user["image"].toString()).into(view.user_profile_image as ImageView)
                    myimage = user["image"].toString()
                }
                view.user_profile_name.text = user["name"].toString()
                myname = user["name"].toString()
                myid = user["id"].toString()
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })

        firebaseDatabase.getReference("UserManage").child(utilclass.profileid!!.toString()).child("MyFriends").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                view.user_total_friend.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })

        firebaseDatabase.getReference("UsersPost").orderByChild("id").equalTo(utilclass.profileid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    view.user_total_posts.text = snapshot.childrenCount.toString()
                    for (i in snapshot.children){
                        var img = i.value as HashMap<String,String>
                        var imgdata = GridDataClass(img["postimage"].toString())
                        if(!gridlist.contains(imgdata))
                            gridlist.add(imgdata)
                    }
                    gridAdapter = GridAdapter(gridlist,requireContext())
                    view?.user_profile_gridview?.adapter = gridAdapter
                    view?.user_profile_gridview?.setOnItemClickListener { parent, view, position, id ->
                        utilclass.image = gridlist[position].img
                        Navigation.findNavController(view).navigate(R.id.userprofile_to_image)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })

        view.profile_friend_button.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Are you sure")
                setMessage("You want to UnFriend")
                setPositiveButton("Yes"){ _, _ ->
                    firebaseDatabase.getReference("UserManage").child(firebaseAuth.currentUser!!.uid).child("MyFriends").child("F+"+utilclass.profileid).removeValue()
                    firebaseDatabase.getReference("UserManage").child(utilclass.profileid!!).child("MyFriends").child(firebaseAuth.currentUser!!.uid).removeValue()
                }
                setNegativeButton("No"){ _, _ -> }
            }.create().show()
        }

        view.profile_message_button.setOnClickListener {
            var i = Intent(requireContext(), MessagesActivity::class.java)
            i.putExtra("id",myid)
            i.putExtra("name",myname)
            i.putExtra("image",myimage)
            i.putExtra("msgkey",msgkey)
            startActivity(i)
        }

        view.user_back.setOnClickListener {
            when(utilclass.profilebackdata) {
                "myfriends" -> { Navigation.findNavController(view).navigate(R.id.userprofile_to_myfriends)
                    utilclass.profilebackdata = ""
                    utilclass.profileid = "" }

                "users" -> { Navigation.findNavController(view).navigate(R.id.userprofile_to_user)
                    utilclass.profilebackdata = ""
                    utilclass.profileid = "" }

                "notification" -> { Navigation.findNavController(view).navigate(R.id.profile_to_notification)
                    utilclass.profilebackdata = ""
                    utilclass.profileid = "" }

                "home" -> { Navigation.findNavController(view).navigate(R.id.profile_to_home)
                    utilclass.profilebackdata = ""
                    utilclass.profileid = "" }
            }
        }

    }
}
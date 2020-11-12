package com.example.chatme.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.R
import com.example.chatme.Utils.*
import com.example.chatme.adapters.PostsAdapter
import com.example.chatme.adapters.StatusAdapater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.status_post_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : Fragment() {
    var statuslist = ArrayList<StatusDataClass>()
    var postslist = ArrayList<PostDataClass>()
    lateinit var myview:View
    lateinit var statusAdapater: StatusAdapater
    lateinit var postAdapater: PostsAdapter
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var mystatus: String = ""
    var ids = ArrayList<idss>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myview = view
        ids.add(idss(firebaseAuth.currentUser!!.uid.toString()))
        getids()
        var layout = LinearLayoutManager(requireContext())
        layout.orientation = LinearLayoutManager.HORIZONTAL
        view.recycler_status.layoutManager = layout
        view.recycler_post.layoutManager = LinearLayoutManager(requireContext())

        getStatusList()
        getPostsList()

        statusAdapater = StatusAdapater(statuslist,{click -> onStatusClick(click)})
        postAdapater = PostsAdapter(postslist,{ click -> onPostClick(click) })

        view.recycler_status.adapter = statusAdapater
        view.recycler_post.adapter = postAdapater

        view.messages.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.home_to_message)
        }

        view.my_status.setOnClickListener {
            if(mystatus == ""){
                return@setOnClickListener
            }
            utilclass.image = mystatus
            Navigation.findNavController(view).navigate(R.id.home_to_image)
        }

        view.add_post_button.setOnClickListener {
            var alertlayout = LayoutInflater.from(requireContext()).inflate(R.layout.status_post_layout,null)
            var i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            var dialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Update Status   or   Upload Post")
                setView(alertlayout)
            }.create().show()

            alertlayout.status_upload_button.setOnClickListener {
                startActivityForResult(Intent.createChooser(i,"Choose Image for Status"),111)
            }
            alertlayout.post_upload_button.setOnClickListener {
                startActivityForResult(Intent.createChooser(i,"Choose Image for Post"),222)
            }
        }
    }

    fun onStatusClick(statusClick: StatusDataClass) {
        utilclass.image = statusClick.image
        Navigation.findNavController(myview).navigate(R.id.home_to_image)
    }

    fun getStatusList() {
        firebaseDatabase.getReference("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        var status = i.value as HashMap<String,String>
                        if (status.containsKey("status"))
                            if(status["id"].toString() == firebaseAuth.currentUser!!.uid)
                            {
                                mystatus = status["status"].toString()
                            }
                            else{
                                var st = StatusDataClass(status["id"].toString(),status["name"].toString(),status["status"].toString())
                                if (!statuslist.contains(st) && ids.contains(idss(st.id)))
                                    statuslist.add(st)
                            }
                    }
                    statusAdapater.refreshdata()
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

    fun getPostsList(){
        firebaseDatabase.getReference("UsersPost").limitToLast(50).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var postid = i.key.toString()
                        var user = i.value as HashMap<String,String>
                        var post = PostDataClass(postid,user["id"].toString(),user["name"].toString(),user["image"].toString(),user["postimage"].toString(),user["date"].toString(),user["time"].toString())
                        if(!postslist.contains(post)  && ids.contains(idss(post.userid))){
                            postslist.add(post)
                        }
                    }
                    postAdapater.refreshdata()
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

    fun onPostClick(postClick: PostDataClass){
        utilclass.profileid = postClick.userid
        utilclass.profilebackdata = "home"
        Navigation.findNavController(myview).navigate(R.id.home_to_profile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var pref = requireContext().getSharedPreferences("chatme",0)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            var filepath: Uri = data.data!!
            var imgref: StorageReference = FirebaseStorage.getInstance().reference.child("status").child(firebaseAuth.currentUser!!.uid).child(filepath.lastPathSegment!!.toString())
            imgref.putFile(filepath).addOnCompleteListener {
                if(it.isSuccessful){
                    imgref.downloadUrl.addOnSuccessListener {url->
                        firebaseDatabase.getReference("Users").child(firebaseAuth.currentUser!!.uid).child("status").setValue(url.toString()).addOnCompleteListener {di->
                            if (di.isSuccessful)
                                requireContext().toast("Status Update")
                        }

                    }
                }
            }
        }
        if(requestCode == 222 && resultCode == Activity.RESULT_OK && data != null){
            val filepath: Uri = data.data!!
            var imgref: StorageReference = FirebaseStorage.getInstance().reference.child("posts").child(firebaseAuth.currentUser!!.uid).child(filepath.lastPathSegment!!.toString())
            imgref.putFile(filepath).addOnCompleteListener {
                if(it.isSuccessful){
                    imgref.downloadUrl.addOnSuccessListener {url->
                        var pref = requireContext().getSharedPreferences("chatme",0)
                        var map = HashMap<String,String>()
                        map.put("postimage",url.toString())
                        map.put("date",SimpleDateFormat("dd/M/yyyy").format(Date()).toString())
                        map.put("time",SimpleDateFormat("hh:mm a").format(Date()).toString())
                        map.put("name",pref.getString("username","defaultvalue").toString())
                        map.put("image",pref.getString("userimage","defaultvalue").toString())
                        map.put("id",pref.getString("userid","defaultvalue").toString())
                        var key:String = firebaseDatabase.getReference("UsersPost").push().key.toString()
                        firebaseDatabase.getReference("UsersPost").child(key).setValue(map).addOnCompleteListener {
                            requireContext().toast("Post Upload")
                        }
                    }
                }
            }
        }
    }

    fun getids(){
        FirebaseDatabase.getInstance().getReference("UserManage").child(FirebaseAuth.getInstance().currentUser!!.uid).child("MyFriends").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        var uid = i.value as HashMap<String,String>
                        ids.add(idss(uid["id"].toString()))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

}
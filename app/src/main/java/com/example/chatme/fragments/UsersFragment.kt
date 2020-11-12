package com.example.chatme.fragments

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
import com.example.chatme.R
import com.example.chatme.Utils.*
import com.example.chatme.adapters.UserListAdapter
import com.example.chatme.viewmodels.UsersFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_users.view.*

class UsersFragment : Fragment(),AuthListener {
    lateinit var myview: View
    lateinit var viewModel: UsersFragmentViewModel
    var ids = ArrayList<idss>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myview = view
        viewModel = ViewModelProviders.of(this).get(UsersFragmentViewModel::class.java)
        viewModel.authListener = this
        Handler().postDelayed({
            viewModel.uids = ids
            viewModel.getUsers()
        },2000)

        var layout = LinearLayoutManager(requireContext())
        layout.orientation = LinearLayoutManager.VERTICAL
        view.recycler_users.layoutManager = layout

        viewModel.users.observe(viewLifecycleOwner, Observer {
            view.recycler_users.adapter = UserListAdapter(it as ArrayList<UsersDataClass>, { click -> onUserClick(click) },{buttonclick -> onButtonClick(buttonclick) })
        })
    }

    fun onButtonClick(buttonclick: UsersDataClass){
        if(buttonclick.toString() == "Add"){
            viewModel.friendRequestSent(buttonclick.id,requireContext())
        }
        else if(buttonclick.toString() == "Cancel"){
            viewModel.friendRequestCancel(FirebaseAuth.getInstance().currentUser!!.uid,buttonclick.id)

        }
    }

    fun onUserClick(userClick: UsersDataClass) {
        // Users to UserProfile
        utilclass.profileid = userClick.id
        utilclass.profilebackdata = "users"
        Navigation.findNavController(myview).navigate(R.id.user_to_userprofile)
    }

    override fun OnStart() {}
    override fun OnFail(message: String) { requireContext().toast(message) }
    override fun OnSuccess() {}

    override fun onStart() {
        super.onStart()
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
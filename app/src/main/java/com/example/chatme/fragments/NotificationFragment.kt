package com.example.chatme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatme.R
import com.example.chatme.Utils.*
import com.example.chatme.adapters.NotificationAdapter
import com.example.chatme.viewmodels.NotificationFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_notification.view.*

class NotificationFragment : Fragment(),AuthListener {
    lateinit var viewModel: NotificationFragmentViewModel
    lateinit var adapter: NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationFragmentViewModel::class.java)
        viewModel.authListener = this

        view.recycler_notification.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getNotifications()
        viewModel.notificationlist.observe(viewLifecycleOwner, Observer {
            adapter = NotificationAdapter(it as ArrayList<Any>,
                { addclick -> addbuttonClick(addclick) },{ cancelclick -> cancelbuttonClick(cancelclick) })
            view.recycler_notification.adapter = adapter
            adapter.refreshdata()
        })
    }

    fun addbuttonClick(addclick: UserRequestDataClass){
        var pref = requireContext().getSharedPreferences("chatme",0)
        var name = pref.getString("username","defaultname")
        var image = pref.getString("userimage","defaultvalue")
        viewModel.friendRequestAccept(FirebaseAuth.getInstance().currentUser!!.uid,addclick,name.toString(),image.toString())
        requireContext().toast("Request Accept")
    }

    fun cancelbuttonClick(cancelclick: UserRequestDataClass){
        viewModel.friendRequestCancel(FirebaseAuth.getInstance().currentUser!!.uid,cancelclick.id)
        adapter.removeItem(cancelclick)
    }

    override fun OnStart() {

    }

    override fun OnFail(message: String) {
        requireContext().toast(message)
    }

    override fun OnSuccess() {

    }
}
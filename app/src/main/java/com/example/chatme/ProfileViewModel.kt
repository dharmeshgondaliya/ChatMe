package com.example.chatme

import android.app.AlertDialog
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.ProfileListener
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel: ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var profileListener: ProfileListener? = null

    var myname: String? = null
    var myemail: String? = null
    var mypassword: String? = null

    fun SaveButtonClick(view: View){
        profileListener?.OnStart()
        name = name.toString().trim()
        email = email.toString().trim()
        password = password.toString().trim()

        if(CheckData()){

        }
        else{
            return
        }
    }

    fun OnLogoutButtonClick(view: View){
        AlertDialog.Builder(view.context).apply {
            setTitle("Are you sure?")
            setPositiveButton("yes"){ _, _ ->
                FirebaseAuth.getInstance().signOut()
                profileListener?.OnLogout()
            }
            setNegativeButton("No"){ _, _ -> }
        }.create().show()
    }

    fun CheckData(): Boolean{
        return true
    }

}
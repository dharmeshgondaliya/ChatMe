package com.example.chatme

import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.AuthListener
import com.example.chatme.Utils.SignupData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase

    fun RegisterButtonClick(view: View){
        authListener?.OnStart()
        name = name.toString()!!.trim()
        email = email.toString()!!.trim()
        password = password.toString()!!.trim()

        if(Check()){
            RegisterUser()
        }
        else{
            return
        }
    }

    fun Check():Boolean{
        if(name.isNullOrEmpty()){
            authListener?.OnFail("Enter Name")
            return false
        }
        if(email.isNullOrEmpty()){
            authListener?.OnFail("Enter Email")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            authListener?.OnFail("Enter Valid Email")
            return false
        }
        if(password.isNullOrEmpty() || password.toString().length < 6){
            authListener?.OnFail("Enter Passwors 6 Character or More")
            return false
        }
        return true
    }

    fun RegisterUser() {
        // https://firebasestorage.googleapis.com/v0/b/chatme-8116b.appspot.com/o/demo.jpeg?alt=media&token=d9b29401-cf8e-495e-bdc5-b686f80e0201
        try {
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()

            firebaseAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener { cu ->
                    if (cu.isSuccessful) {
                        var user = SignupData(firebaseAuth.currentUser!!.uid,name.toString(),email.toString(),password.toString(),
                            "ic_boy","false")

                        firebaseDatabase.getReference("Users").child(firebaseAuth.currentUser!!.uid).setValue(user).addOnCompleteListener { di->
                            if(di.isSuccessful){
                                firebaseDatabase.getReference("UserManage").child(firebaseAuth.currentUser!!.uid)
                                authListener?.OnSuccess()
                            }
                            else{
                                authListener?.OnFail(di.exception!!.message.toString())
                            }
                        }
                    } else {
                        authListener?.OnFail(cu.exception!!.message.toString())
                    }
                }
        }
        catch (e: Exception){
            authListener?.OnFail(e.message.toString())
        }
    }
}
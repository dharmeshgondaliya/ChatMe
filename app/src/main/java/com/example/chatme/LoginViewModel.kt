package com.example.chatme

import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.chatme.Utils.AuthListener
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null
    lateinit var firebaseAuth: FirebaseAuth

    fun LoginButtonClick(view: View){
        authListener?.OnStart()
        email = email.toString()!!.trim()
        password = password.toString()!!.trim()

        if(Check()){
            LoginUser()
        }
        else{
            return
        }
    }

    fun Check():Boolean{
        if(email.isNullOrEmpty()){
            authListener?.OnFail("Enter Email")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            authListener?.OnFail("Enter Valid Email")
            return false
        }
        if(password.isNullOrEmpty() || password.toString().length < 6){
            authListener?.OnFail("Enter Password 6 Character Or More")
            return false
        }
        return true
    }

    fun LoginUser(){
        try {
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        authListener?.OnSuccess()
                    } else {
                        authListener?.OnFail(it.exception!!.message.toString())
                    }
                }
        }catch (e: Exception){
            authListener?.OnFail(e.message.toString())
        }
    }
}
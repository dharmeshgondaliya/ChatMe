package com.example.chatme

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.chatme.Utils.AuthListener
import com.example.chatme.Utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity(), AuthListener {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: com.example.chatme.databinding.ActivityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        var viewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.loginmodel = viewmodel
        viewmodel.authListener = this

        signup_text.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }

    override fun OnStart() {
        login_progress.visibility = View.VISIBLE
    }

    override fun OnFail(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
        login_progress.visibility = View.GONE
    }

    override fun OnSuccess() {
        var pref = this.getSharedPreferences("chatme",0)
        login_progress.visibility = View.GONE
        var edit = pref.edit()
        edit.putString("signout","false")
        edit.apply()
        edit.commit()

        startActivity(Intent(this,HomeActivity::class.java))
        this.finish()
    }

    override fun onStart() {
        super.onStart()

        var pref = this.getSharedPreferences("chatme",0)
        if(pref.getString("signout","defaultvalue").toString() == "true"){
            return
        }
        try {
            FirebaseAuth.getInstance().currentUser!!.uid.let {
                startActivity(Intent(this,HomeActivity::class.java))
                this.finish()
            }
        }
        catch (e: Exception){
            return
        }
        /*FirebaseAuth.getInstance().currentUser.let {
            startActivity(Intent(this,HomeActivity::class.java))
            this.finish()
        }*/
    }

}
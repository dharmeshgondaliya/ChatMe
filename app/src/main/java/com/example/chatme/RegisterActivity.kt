package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.chatme.Utils.AuthListener
import com.example.chatme.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.register_progress

class RegisterActivity : AppCompatActivity(),AuthListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityRegisterBinding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        var viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding.registermodel = viewModel
        viewModel.authListener = this

        login_text.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    override fun OnStart() {
        register_progress.visibility = View.VISIBLE
    }

    override fun OnFail(message: String) {
        register_progress.visibility = View.GONE
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun OnSuccess() {
        var pref = this.getSharedPreferences("chatme",0)
        var edit = pref.edit()
        edit.putString("signout","false")
        edit.apply()
        edit.commit()

        register_progress.visibility = View.GONE
        var i = Intent(this,EmailVerifyActivity::class.java)
        i.putExtra("type","signup")
        startActivity(i)
        this.finish()
    }
}
package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.chatme.Utils.toast
import kotlinx.android.synthetic.main.activity_email_verify.*

class EmailVerifyActivity : AppCompatActivity() {
    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verify)

        //toast(intent.getStringExtra("OTP").toString())
        if(intent.getStringExtra("type").toString() == "signup"){
            Handler().postDelayed({
                startActivity(Intent(this,HomeActivity::class.java))
                this.finish()
            },3000)
        }

    }
}
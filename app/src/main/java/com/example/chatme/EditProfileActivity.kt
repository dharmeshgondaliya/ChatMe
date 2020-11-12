package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.chatme.Utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        if(intent.getStringExtra("image").toString() == "ic_boy")
            edit_profile_image.setImageResource(R.drawable.ic_boy)
        else
            Picasso.get().load(intent.getStringExtra("image").toString()).into(edit_profile_image as ImageView)

        edit_email.isEnabled = false
        edit_password.isEnabled = false
        edit_name.isEnabled = false

        edit_back.setOnClickListener {
            this.finish()
        }

        edit_profile_logout.setOnClickListener {
            OnLogout()
        }
    }

    fun OnLogout() {
        FirebaseAuth.getInstance().signOut()
        toast("Logout")
        var pref = this.getSharedPreferences("chatme",0)
        var edit = pref.edit()
        edit.putString("signout","true")
        edit.apply()
        edit.commit()
        startActivity(Intent(this,LoginActivity::class.java))
        this.finish()
    }
}
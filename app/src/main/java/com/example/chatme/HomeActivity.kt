package com.example.chatme


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chatme.Utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var bottommenu = findViewById<BottomNavigationView>(R.id.bottom_bar)
        var navController = findNavController(R.id.home_fragment)
        bottommenu.setupWithNavController(navController)

        var pref = this.getSharedPreferences("chatme",0)
        var edit = pref.edit()

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.value as HashMap<String,String>
                edit.putString("username",user["name"].toString())
                edit.putString("userid",user["id"].toString())
                edit.putString("userimage",user["image"].toString())
                edit.apply()
                edit.commit()
            }
            override fun onCancelled(error: DatabaseError) { toast(error.message) }
        })
    }
}
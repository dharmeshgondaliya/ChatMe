package com.example.chatme.Utils

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String){
    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
}

class utilclass{
    companion object myutilclass{
        var profilebackdata: String? = null
        var profileid: String? = null
        var image: String? = null
    }
}
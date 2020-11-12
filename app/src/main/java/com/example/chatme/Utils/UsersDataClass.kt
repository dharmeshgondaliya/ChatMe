package com.example.chatme.Utils

data class UsersDataClass(var id: String,var name: String,var image: String,var button: String = ""){
    override fun toString(): String {
        return button
    }
}
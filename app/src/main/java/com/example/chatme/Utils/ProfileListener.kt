package com.example.chatme.Utils

interface ProfileListener {
    fun OnStart()
    fun OnFail(message: String)
    fun OnSuccess()
    fun OnLogout()
}
package com.example.chatme.Utils

interface AuthListener {
    fun OnStart()
    fun OnFail(message: String)
    fun OnSuccess()
}
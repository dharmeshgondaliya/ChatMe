package com.example.chatme.Utils

data class SentMessageDataClass(var msgid1: String,var message1: String,var date1: String,var time1: String) {
    override fun toString(): String {
        return "1"
    }

    override fun equals(other: Any?): Boolean {
        return if(other is SentMessageDataClass){
            other.msgid1 == msgid1
        }else false
    }
}
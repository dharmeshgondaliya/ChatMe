package com.example.chatme.Utils

data class ReceiveMessageDataClass(var msgid2: String,var message2: String,var date2: String,var time2: String) {
    override fun toString(): String {
        return "2"
    }

    override fun equals(other: Any?): Boolean {
        return if(other is ReceiveMessageDataClass){
            other.msgid2 == msgid2
        }else false
    }
}
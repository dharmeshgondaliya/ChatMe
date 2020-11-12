package com.example.chatme.Utils

data class ImageReceiveDataClass (var msgid4: String,var image4: String,var date4: String,var time4: String) {
    override fun toString(): String {
        return "4"
    }

    override fun equals(other: Any?): Boolean {
        return if(other is ImageReceiveDataClass){
            other.msgid4 == msgid4
        }else false
    }

}
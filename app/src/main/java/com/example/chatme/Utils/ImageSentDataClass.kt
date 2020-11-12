package com.example.chatme.Utils

data class ImageSentDataClass(var msgid3: String,var image3: String,var date3: String,var time3: String) {
    override fun toString(): String {
        return "3"
    }

    override fun equals(other: Any?): Boolean {
        return if(other is ImageSentDataClass){
            other.msgid3 == msgid3
        }else false
    }

}
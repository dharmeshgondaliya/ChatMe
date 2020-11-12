package com.example.chatme.Utils

data class Notification12(var id: String? = null,var type: String? = null){
    override fun toString(): String {
        return type.toString()
    }
}
data class Notification3(var id: String? = null,var type: String? = null,var postimage: String? = null){
    override fun toString(): String {
        return type.toString()
    }
}
data class idss(var id: String,var key: String=""){
    override fun equals(other: Any?): Boolean {
        return  if (other is idss){
            other.id == id
        }else false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
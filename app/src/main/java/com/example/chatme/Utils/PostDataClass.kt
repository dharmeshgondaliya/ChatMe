package com.example.chatme.Utils

data  class PostDataClass(var postid: String,var userid: String,var name: String,var userimage: String,var postimage: String,var date: String,var time: String){
    override fun equals(other: Any?): Boolean {
        return if(other is PostDataClass){
            other.postid == postid
        }else false
    }

    override fun hashCode(): Int {
        var result = postid.hashCode()
        result = 31 * result + userid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + userimage.hashCode()
        result = 31 * result + postimage.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}

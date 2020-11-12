package com.example.chatme.Utils

data class GridDataClass(var img: String){
    override fun equals(other: Any?): Boolean {
        return if(other is GridDataClass){
            other.img == img
        }else false
    }
}
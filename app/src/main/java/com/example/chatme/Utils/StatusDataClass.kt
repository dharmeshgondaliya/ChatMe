package com.example.chatme.Utils

data class StatusDataClass(var id: String,var name: String,var image: String){
    override fun equals(other: Any?): Boolean {
        return if(other is StatusDataClass){
            other.id == id
        }else false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}
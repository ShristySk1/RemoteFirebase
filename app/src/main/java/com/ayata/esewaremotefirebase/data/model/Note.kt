package com.ayata.esewaremotefirebase.data.model

import java.io.Serializable

data class Note(
    val title: String,
    val description: String,
    var id:String?=null

):Serializable{
    constructor(): this(title = "", description =  "")
}
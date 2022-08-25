package com.ayata.esewaremotefirebase.data.model

import java.io.Serializable

data class Quote(
    val author: String,
    val quote: String,
    var id:String?=null

):Serializable{
    constructor(): this(author = "", quote =  "")
}
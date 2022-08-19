package com.ayata.esewaremotefirebase.data.model

data class Note(
    val title: String,
    val description: String,

){
    constructor(): this("", "")

}
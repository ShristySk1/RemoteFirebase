package com.ayata.esewaremotefirebase.domain

interface Repository {
    fun insert()
    fun update(id: Int)
    fun delete(id: Int)
    fun getAll()
}
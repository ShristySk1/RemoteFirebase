package com.ayata.esewaremotefirebase.data.datasource

import com.ayata.esewaremotefirebase.data.model.ThemeData

interface IRemoteConfigApi {
    fun getSubTitle(): String
    fun getThemeData(): ThemeData

}
package com.ayata.esewaremotefirebase.data

import android.util.Log
import com.ayata.esewaremotefirebase.data.datasource.IRemoteConfigApi
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(
    private val gson: Gson,
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : IRemoteConfigApi {
    private val config: FirebaseRemoteConfig = firebaseRemoteConfig.apply {

        fetch(CONFIG_CACHE_EXPIRATION_SECONDS)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("testdata", ": ");
                    // activate()//only when next time user opens app
                }
            }
    }

    override fun getSubTitle(): String =
        read<String>(ConfigParam.SUB_TITLE) ?: SOME_DEFAULT_VALUE

    private inline fun <reified T> read(param: ConfigParam): T? = read(param, T::class.java)

    private fun <T> read(param: ConfigParam, returnType: Class<T>): T? {
        val value: Any? = when (returnType) {
            String::class.java -> config.getString(param.key)
            Boolean::class.java -> config.getBoolean(param.key)
            Long::class.java -> config.getLong(param.key)
            Int::class.java -> config.getLong(param.key).toInt()
            Double::class.java -> config.getDouble(param.key)
            Float::class.java -> config.getDouble(param.key).toFloat()
            else -> {
                val json = config.getString(param.key)
                json.takeIf { it.isNotBlank() }?.let { gson.jsonToObjectOrNull(json, returnType) }
            }
        }
        @Suppress("UNCHECKED_CAST")
        return (value as? T)
    }

    private enum class ConfigParam(val key: String) {
        SUB_TITLE("app_subtitle"),
    }

    companion object {
        /**
         * Config expiration interval 30 minutes.
         */
        internal const val CONFIG_CACHE_EXPIRATION_SECONDS = 0L
        private const val SOME_DEFAULT_VALUE = "Hello !"
    }

    fun <T> Gson.jsonToObjectOrNull(json: String?, clazz: Class<T>): T? =
        try {
            fromJson(json, clazz)
        } catch (ignored: Exception) {
            null
        }


}
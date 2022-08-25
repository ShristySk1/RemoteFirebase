package com.ayata.esewaremotefirebase.di

import com.ayata.esewaremotefirebase.data.configmanager.RemoteConfigManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRemoteConfigManager(): RemoteConfigManager {
        return RemoteConfigManager(provideGson(), provideRemoteConfig())
    }

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(RemoteConfigManager.CONFIG_CACHE_EXPIRATION_SECONDS)
                .build()
            setConfigSettingsAsync(configSettings)
        }

    }
}
package com.line.assignment.simpleimageloader.di

import com.line.assignment.simpleimageloader.App
import com.line.assignment.simpleimageloader.data.local.LocalData
import com.line.assignment.simpleimageloader.utils.SimpleImageDownloader
import com.line.assignment.simpleimageloader.utils.Network
import com.line.assignment.simpleimageloader.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideLocalRepository(): LocalData {
        return LocalData()
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(): NetworkConnectivity {
        return Network(App.context)
    }

    @Provides
    @Singleton
    fun provideDownloadManager(): SimpleImageDownloader {
        return SimpleImageDownloader(App.context)
    }
}

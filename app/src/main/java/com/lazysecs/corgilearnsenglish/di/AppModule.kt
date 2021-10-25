package com.lazysecs.corgilearnsenglish.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(
    private val context: Context,
    private val application: Application
) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideApplication(): Application {
        return application
    }
}
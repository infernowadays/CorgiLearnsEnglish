package com.lazysecs.corgilearnsenglish

import android.app.Application
import android.content.Context
import com.lazysecs.corgilearnsenglish.di.AppComponent

class CorgiLearnsEnglishApp : Application() {

    val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this, this) }

    companion object {

        @JvmStatic
        fun get(context: Context): CorgiLearnsEnglishApp {
            return context.applicationContext as CorgiLearnsEnglishApp
        }
    }
}
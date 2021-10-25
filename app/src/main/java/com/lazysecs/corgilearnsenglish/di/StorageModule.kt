package com.lazysecs.corgilearnsenglish.di

import android.content.Context
import androidx.room.Room
import com.lazysecs.data.db.CorgiLearnsEnglishDatabase
import com.lazysecs.data.db.SharedPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    companion object {
        private const val DATABASE_NAME = "corgi_learns_english.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CorgiLearnsEnglishDatabase {
        return Room.databaseBuilder(context, CorgiLearnsEnglishDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPrefs {
        return SharedPrefs(context)
    }
}
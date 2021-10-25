package com.lazysecs.corgilearnsenglish.di

import android.app.Application
import com.lazysecs.data.api.SearchService
import com.lazysecs.data.db.CorgiLearnsEnglishDatabase
import com.lazysecs.data.db.SharedPrefs
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.data.interactors.SearchInteractor
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.interactors.WordsListInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorsModule {

    @Provides
    @Singleton
    fun provideWordListInteractor(database: CorgiLearnsEnglishDatabase): WordsListInteractor {
        return WordsListInteractor(database)
    }

    @Provides
    @Singleton
    fun provideWordsInteractor(database: CorgiLearnsEnglishDatabase): WordsInteractor {
        return WordsInteractor(database)
    }

    @Provides
    @Singleton
    fun provideSearchInteractor(
        searchService: SearchService,
        database: CorgiLearnsEnglishDatabase
    ): SearchInteractor {
        return SearchInteractor(searchService)
    }

    @Provides
    @Singleton
    fun provideLanguagesInteractor(
        application: Application,
        sharedPrefs: SharedPrefs,
    ): LanguagesInteractor {
        return LanguagesInteractor(application, sharedPrefs)
    }
}
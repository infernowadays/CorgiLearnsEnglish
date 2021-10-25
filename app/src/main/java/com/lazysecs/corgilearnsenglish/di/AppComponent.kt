package com.lazysecs.corgilearnsenglish.di

import android.app.Application
import android.content.Context
import com.lazysecs.corgilearnsenglish.ui.MainActivity
import com.lazysecs.corgilearnsenglish.ui.learn.LearnComponent
import com.lazysecs.corgilearnsenglish.ui.learn.LearnModule
import com.lazysecs.corgilearnsenglish.ui.search.SearchComponent
import com.lazysecs.corgilearnsenglish.ui.search.SearchModule
import com.lazysecs.corgilearnsenglish.ui.select_language.SelectLanguageComponent
import com.lazysecs.corgilearnsenglish.ui.select_language.SelectLanguageModule
import com.lazysecs.corgilearnsenglish.ui.word_list.WordListComponent
import com.lazysecs.corgilearnsenglish.ui.word_list.WordListModule
import com.lazysecs.corgilearnsenglish.ui.words.WordsComponent
import com.lazysecs.corgilearnsenglish.ui.words.WordsModule
import com.lazysecs.data.di.ApiModule
import com.lazysecs.data.di.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ApiModule::class,
        NavigationModule::class,
        InteractorsModule::class,
        StorageModule::class,
    ]
)
interface AppComponent {

    class Initializer private constructor() {
        companion object {
            fun init(context: Context, application: Application): AppComponent {
                return DaggerAppComponent.builder()
                    .appModule(AppModule(context, application))
                    .build()
            }
        }
    }

    fun inject(mainActivity: MainActivity)

    fun plus(wordListModule: WordListModule): WordListComponent

    fun plus(wordsModule: WordsModule): WordsComponent

    fun plus(searchModule: SearchModule): SearchComponent

    fun plus(learnModule: LearnModule): LearnComponent

    fun plus(selectLanguageModule: SelectLanguageModule): SelectLanguageComponent
}
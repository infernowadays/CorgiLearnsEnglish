package com.lazysecs.corgilearnsenglish.ui.learn

import android.app.Application
import com.lazysecs.corgilearnsenglish.di.scopes.FragmentScope
import com.lazysecs.corgilearnsenglish.extensions.createViewModel
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.data.interactors.WordsInteractor
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [LearnModule::class])
interface LearnComponent {

    fun inject(learnFragment: LearnFragment)
}

@Module
class LearnModule(private val learnFragment: LearnFragment) {

    @Provides
    @FragmentScope
    fun viewModel(
        application: Application,
        wordsInteractor: WordsInteractor,
        languagesInteractor: LanguagesInteractor,
    ): LearnViewModel {
        return learnFragment.createViewModel {
            LearnViewModel(
                application,
                wordsInteractor,
                languagesInteractor,
            )
        }
    }
}
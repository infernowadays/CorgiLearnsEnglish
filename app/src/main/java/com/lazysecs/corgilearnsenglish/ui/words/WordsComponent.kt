package com.lazysecs.corgilearnsenglish.ui.words

import com.lazysecs.corgilearnsenglish.di.scopes.FragmentScope
import com.lazysecs.corgilearnsenglish.extensions.createViewModel
import com.lazysecs.data.interactors.WordsInteractor
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WordsModule::class])
interface WordsComponent {

    fun inject(wordsFragment: WordsFragment)
}

@Module
class WordsModule(private val wordsFragment: WordsFragment) {

    @Provides
    @FragmentScope
    fun viewModel(
        wordsInteractor: WordsInteractor
    ): WordsViewModel {
        return wordsFragment.createViewModel {
            WordsViewModel(
                wordsInteractor = wordsInteractor
            )
        }
    }
}
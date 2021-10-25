package com.lazysecs.corgilearnsenglish.ui.word_list

import com.lazysecs.corgilearnsenglish.di.scopes.FragmentScope
import com.lazysecs.corgilearnsenglish.extensions.createViewModel
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.interactors.WordsListInteractor
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WordListModule::class])
interface WordListComponent {

    fun inject(wordListFragment: WordListFragment)
}

@Module
class WordListModule(private val wordListFragment: WordListFragment) {

    @Provides
    @FragmentScope
    fun viewModel(
        wordsListInteractor: WordsListInteractor,
        wordsInteractor: WordsInteractor,
    ): WordListViewModel {
        return wordListFragment.createViewModel {
            WordListViewModel(
                wordsListInteractor = wordsListInteractor,
                wordsInteractor = wordsInteractor,
            )
        }
    }
}
package com.lazysecs.corgilearnsenglish.ui.search

import com.lazysecs.corgilearnsenglish.di.scopes.FragmentScope
import com.lazysecs.corgilearnsenglish.extensions.createViewModel
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.data.interactors.SearchInteractor
import com.lazysecs.data.interactors.WordsInteractor
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)
}

@Module
class SearchModule(private val searchFragment: SearchFragment) {

    @Provides
    @FragmentScope
    fun viewModel(
        searchInteractor: SearchInteractor,
        wordsInteractor: WordsInteractor,
        languagesInteractor: LanguagesInteractor,
    ): SearchViewModel {
        return searchFragment.createViewModel {
            SearchViewModel(
                searchInteractor = searchInteractor,
                wordsInteractor = wordsInteractor,
                languagesInteractor = languagesInteractor,
            )
        }
    }
}
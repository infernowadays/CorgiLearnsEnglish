package com.lazysecs.corgilearnsenglish.ui.select_language

import com.lazysecs.corgilearnsenglish.di.scopes.FragmentScope
import com.lazysecs.corgilearnsenglish.extensions.createViewModel
import com.lazysecs.data.interactors.LanguagesInteractor
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SelectLanguageModule::class])
interface SelectLanguageComponent {

    fun inject(selectLanguageFragment: SelectLanguageFragment)
}

@Module
class SelectLanguageModule(private val selectLanguageFragment: SelectLanguageFragment) {

    @Provides
    @FragmentScope
    fun viewModel(
        languagesInteractor: LanguagesInteractor,
    ): SelectLanguageViewModel {
        return selectLanguageFragment.createViewModel {
            SelectLanguageViewModel(
                languagesInteractor,
            )
        }
    }
}
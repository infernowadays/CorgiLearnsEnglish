package com.lazysecs.corgilearnsenglish.navigation

import androidx.fragment.app.Fragment
import com.lazysecs.corgilearnsenglish.ui.learn.LearnFragment
import com.lazysecs.corgilearnsenglish.ui.search.SearchFragment
import com.lazysecs.corgilearnsenglish.ui.select_language.SelectLanguageFragment
import com.lazysecs.corgilearnsenglish.ui.words.WordsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class Learn(private val wordListId: String) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return LearnFragment.newInstance(wordListId)
        }
    }

    class WordsList(
        private val id: String,
        private val name: String,
        private val createdMillis: Long,
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return WordsFragment.newInstance(id, name, createdMillis)
        }
    }

    class SearchWords(
        private val wordListId: String,
        private val query: String? = null,
        private val fromLanguageCode: String? = null,
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return SearchFragment.newInstance(wordListId, query, fromLanguageCode)
        }
    }

    class SelectLanguage(private val fromLanguage: Boolean) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return SelectLanguageFragment.newInstance(fromLanguage)
        }
    }
}
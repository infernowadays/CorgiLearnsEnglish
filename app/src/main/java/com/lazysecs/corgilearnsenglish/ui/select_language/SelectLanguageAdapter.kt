package com.lazysecs.corgilearnsenglish.ui.select_language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lazysecs.corgilearnsenglish.databinding.LanguageItemBinding
import com.lazysecs.domain.models.Language

class SelectLanguageAdapter(
    private val saveSelectedLanguage: (String) -> Unit,
    private var selectedLanguageName: String,
) : ListAdapter<Language, LanguageVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageVH(
        LanguageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        this::selectLanguage
    )

    override fun onBindViewHolder(holder: LanguageVH, position: Int) {
        holder.bind(getItem(position), selectedLanguageName == getItem(position).name)
    }

    private fun selectLanguage(languageName: String) {
        saveSelectedLanguage(languageName)
        notifyItemChanged(currentList.indexOfFirst { it.name == selectedLanguageName })
        notifyItemChanged(currentList.indexOfFirst { it.name == languageName })
        selectedLanguageName = languageName
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Language>() {
            override fun areItemsTheSame(
                oldItem: Language,
                newItem: Language
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Language,
                newItem: Language
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
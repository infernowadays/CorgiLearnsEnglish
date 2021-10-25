package com.lazysecs.corgilearnsenglish.ui.select_language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.databinding.FragmentSelectLanguageBinding
import com.lazysecs.corgilearnsenglish.databinding.ToolbarBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.search.SearchFragment
import javax.inject.Inject

class SelectLanguageFragment : BaseFragment() {

    private var _binding: FragmentSelectLanguageBinding? = null
    private val binding get() = _binding!!
    private var fromLanguage = true
    private val adapter by lazy {
        SelectLanguageAdapter(
            ::saveSelectedLanguage,
            if (fromLanguage) viewModel.getFromLanguage() else viewModel.getToLanguage(),
        )
    }

    @Inject
    lateinit var viewModel: SelectLanguageViewModel

    companion object {
        fun newInstance(fromLanguage: Boolean) = SelectLanguageFragment().apply {
            arguments = Bundle().apply {
                putBoolean(Extras.FROM_LANGUAGE, fromLanguage)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null) {
            fromLanguage = requireArguments().getBoolean(Extras.FROM_LANGUAGE)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSelectLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(viewModel.getLanguages())
        binding.rvLanguages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLanguages.adapter = adapter
    }

    override fun setListeners() {
        binding.toolbar.ivBack.setOnClickListener {
            back()
        }
    }

    override fun getToolbarBinding(): ToolbarBinding {
        return binding.toolbar
    }

    override fun getToolbarTitle(): String {
        return "Search words"
    }

    private fun saveSelectedLanguage(language: String) {
        if (fromLanguage) {
            viewModel.setFromLanguageName(language)
        } else {
            viewModel.setToLanguageName(language)
        }
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(requireActivity())
            .appComponent
            .plus(SelectLanguageModule(this))
            .inject(this)
    }
}
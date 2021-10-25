package com.lazysecs.corgilearnsenglish.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.FragmentSearchWordsBinding
import com.lazysecs.corgilearnsenglish.databinding.ToolbarBinding
import com.lazysecs.corgilearnsenglish.ui.MainActivity
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private val adapter by lazy { SearchAdapter() }
    private val searchSubject by lazy { BehaviorSubject.create<String>() }
    private val binding get() = _binding!!

    private var _binding: FragmentSearchWordsBinding? = null
    private var searchDisposable: Disposable? = null

    private lateinit var wordListId: String
    private lateinit var query: String
    private lateinit var fromLanguageCode: String

    @Inject
    lateinit var viewModel: SearchViewModel

    companion object {
        const val DEBOUNCE_DELAY = 300L
        const val MIN_SEARCH_LENGTH = 1

        fun newInstance(
            wordListId: String,
            query: String? = null,
            fromLanguageCode: String? = null,
        ): SearchFragment {
            val fragment = SearchFragment()
            val b = Bundle()
            b.putString(Extras.WORD_LIST_ID, wordListId)
            b.putString(Extras.SEARCH_QUERY, query)
            b.putString(Extras.FROM_LANGUAGE, fromLanguageCode)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null) {
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID) ?: ""
            query = requireArguments().getString(Extras.SEARCH_QUERY) ?: ""
            fromLanguageCode = requireArguments().getString(Extras.FROM_LANGUAGE) ?: ""
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary))
        setOrUpdateLanguages()
        if (fromLanguageCode.isNotBlank() && fromLanguageCode != viewModel.getFromLanguageCode()) {
            viewModel.swapLanguages()
            setOrUpdateLanguages()
        }
        binding.rvDictionaryLookup.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDictionaryLookup.adapter = adapter
        if (query.isNotBlank()) binding.etSearch.setText(query)
        viewModel.showAddWordToListButtonLiveEvent.observe(viewLifecycleOwner, { showAddWordToListButton ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.ivAddWord.setBackgroundResource(
                    if (showAddWordToListButton) R.drawable.ic_add_white else R.drawable.ic_added
                )
                binding.ivAddWord.isVisible = showAddWordToListButton
                binding.ivAddWord.isEnabled = true
            }
        })
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSearchWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setListeners() {
        binding.ivAddWord.setOnClickListener {
            binding.ivAddWord.setBackgroundResource(R.drawable.ic_added)
            binding.ivAddWord.isEnabled = false
            viewModel.getWord()?.let {
                viewModel.addWordToList(it)
            }
        }
        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
        }
        binding.tvFromLanguage.setOnClickListener {
//            (requireActivity() as MainActivity).hideSoftwareKeyboard(binding.etSearch)
//            navigateTo(Screens.SelectLanguage(true))
        }
        binding.tvToLanguage.setOnClickListener {
//            (requireActivity() as MainActivity).hideSoftwareKeyboard(binding.etSearch)
//            navigateTo(Screens.SelectLanguage(false))
        }
        binding.ivSwapLanguages.setOnClickListener {
            viewModel.swapLanguages()
            setOrUpdateLanguages()
            binding.etSearch.setText(binding.etSearch.text.toString())
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDisposable?.dispose()
                setupSearchDisposable()
                if (s.toString().length > MIN_SEARCH_LENGTH) {
                    searchSubject.onNext(s.toString())
                    showProgress(true)
                } else {
                    searchSubject.onNext("")
                    clearSearchResults()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.ivClearSearch.visibility = if (s?.isEmpty() == true) GONE else VISIBLE
            }
        })
        binding.rvDictionaryLookup.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) hideSoftwareKeyboard(binding.etSearch)
            }
        })
        binding.toolbar.ivBack.setOnClickListener {
            (requireActivity() as MainActivity).hideSoftwareKeyboard(binding.etSearch)
            back()
        }
    }

    override fun getToolbarTitle(): String {
        return "Search words"
    }

    override fun getToolbarBinding(): ToolbarBinding {
        return binding.toolbar
    }

    override fun setupView() {
        binding.etSearch.requestFocus()
        (requireActivity() as MainActivity).showSoftwareKeyboard(binding.etSearch)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

    private fun setupSearchDisposable() {
        searchDisposable = searchSubject
            .debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .switchMap { query ->
                Observable.zip(
                    viewModel.translate(text = query, wordListId = wordListId),
                    viewModel.dictionaryLookup(text = query),
                    { translation, dictionary ->
                        translation to dictionary
                    }
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { translateAndDictionaryPair ->
                    translateAndDictionaryPair.first.let {
                        binding.tvTranslation.text = it.text
                        binding.tvTranslation.visibility = VISIBLE
                    }
                    translateAndDictionaryPair.second.let {
                        if (it.isNotEmpty()) {
                            adapter.submitList(it)
                            binding.rvDictionaryLookup.visibility = VISIBLE
                        }
                    }
                    showProgress(false)
                },
                {
                    Timber.e(it)
                }
            )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchResults() {
        adapter.submitList(null)
        adapter.notifyDataSetChanged()
        binding.groupTranslation.visibility = GONE
        showProgress(false)
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(requireActivity())
            .appComponent
            .plus(SearchModule(this))
            .inject(this)
    }

    private fun showProgress(showProgress: Boolean) {
        if (showProgress) {
            binding.groupTranslation.visibility = GONE
            binding.pb.visibility = VISIBLE
        } else {
            binding.pb.visibility = GONE
        }
    }

    private fun setOrUpdateLanguages() {
        viewModel.getLanguages().let {
            binding.tvFromLanguage.text = it.first
            binding.tvToLanguage.text = it.second
        }
    }
}
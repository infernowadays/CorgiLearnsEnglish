package com.lazysecs.corgilearnsenglish.ui.word_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.custom_views.LottieProgressView
import com.lazysecs.corgilearnsenglish.databinding.FragmentWordListBinding
import com.lazysecs.corgilearnsenglish.navigation.Screens
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.Requests
import com.lazysecs.domain.models.WordList
import javax.inject.Inject

class WordListFragment : BaseFragment(), WordListClickListener {

    private var _binding: FragmentWordListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WordListsAdapter

    @Inject
    lateinit var viewModel: WordListViewModel

    companion object {
        fun newInstance(): WordListFragment {
            val fragment = WordListFragment()
            val b = Bundle()
            fragment.arguments = b
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = WordListsAdapter(this, context.resources)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setFragmentResultListener(Requests.CREATE_WORD_LIST) { _, bundle ->
            val wordListName = bundle.getString(Extras.WORD_LIST_NAME)
            if (wordListName != null) viewModel.createWordsList(wordListName)
        }
        setFragmentResultListener(Requests.SHOW_EDIT_WORD_LIST_DIALOG) { _, bundle ->
            val wordListId = bundle.getString(Extras.WORD_LIST_ID)
            val wordListName = bundle.getString(Extras.WORD_LIST_NAME)
            if (wordListId != null && wordListName != null) CreateWordListDialog.newInstance(
                wordListId,
                wordListName
            ).show(requireActivity().supportFragmentManager, Requests.EDIT_WORD_LIST)
        }
        setFragmentResultListener(Requests.EDIT_WORD_LIST) { _, bundle ->
            val wordListId = bundle.getString(Extras.WORD_LIST_ID)
            val wordListName = bundle.getString(Extras.WORD_LIST_NAME)
            if (wordListId != null && wordListName != null) viewModel.editWordListName(wordListId, wordListName)
        }
        setFragmentResultListener(Requests.SHOW_DELETE_WORD_LIST_DIALOG) { _, bundle ->
            val wordListId = bundle.getString(Extras.WORD_LIST_ID)
            if (wordListId != null) DeleteWordListDialog.newInstance(
                wordListId
            ).show(requireActivity().supportFragmentManager, Requests.DELETE_WORD_LIST)
        }
        setFragmentResultListener(Requests.DELETE_WORD_LIST) { _, bundle ->
            val wordListId = bundle.getString(Extras.WORD_LIST_ID)
            if (wordListId != null) viewModel.deleteWordList(wordListId)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvWordList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvWordList.adapter = adapter
        viewModel.getList()
        viewModel.wordListsLiveEvent.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isNullOrEmpty()) {
                binding.progress.show(getString(R.string.press_plus_to_create_list), R.raw.nothing_found)
            } else {
                binding.progress.hide()
            }
        })
        viewModel.createdWordListLiveEvent.observe(viewLifecycleOwner, { wordList ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                navigateTo(Screens.WordsList(wordList.id, wordList.name, wordList.createdMillis))
            }
        })
    }

    override fun getProgressBinding(): LottieProgressView {
        return binding.progress
    }

    override fun setListeners() {
        binding.fabAddWordList.setOnClickListener {
            showDialog(
                CreateWordListDialog.newInstance(
                    null,
                    null
                ),
                Requests.CREATE_WORD_LIST
            )
        }
        binding.rvWordList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabAddWordList.isShown) {
                    binding.fabAddWordList.hide()
                } else {
                    binding.fabAddWordList.show()
                }
            }
        })
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentWordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onWordListClick(wordList: WordList) {
        navigateTo(Screens.WordsList(wordList.id, wordList.name, wordList.createdMillis))
    }

    override fun onWordListLongClick(wordList: WordList) {
        showWordListMenu(wordList)
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(requireActivity())
            .appComponent
            .plus(WordListModule(this))
            .inject(this)
    }

    private fun showWordListMenu(wordList: WordList) {
        WordListBottomSheetDialogFragment.newInstance(wordList.id, wordList.name).show(
            requireActivity().supportFragmentManager,
            WordListBottomSheetDialogFragment.TAG
        )
    }
}
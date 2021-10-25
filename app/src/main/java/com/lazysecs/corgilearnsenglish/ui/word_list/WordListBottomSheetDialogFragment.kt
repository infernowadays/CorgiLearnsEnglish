package com.lazysecs.corgilearnsenglish.ui.word_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.FragmentWordListDialogBinding
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.Requests

class WordListBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentWordListDialogBinding? = null
    private val binding get() = _binding!!
    private var wordListId: String? = null
    private var wordListName: String? = null

    companion object {
        const val TAG = Requests.SHOW_WORD_LIST_MENU

        fun newInstance(wordListId: String, wordListName: String): WordListBottomSheetDialogFragment =
            WordListBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(Extras.WORD_LIST_ID, wordListId)
                    putString(Extras.WORD_LIST_NAME, wordListName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (arguments != null) {
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID)
            wordListName = requireArguments().getString(Extras.WORD_LIST_NAME)
        }
        _binding = FragmentWordListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (wordListName != null) {
            binding.tvWordListName.text = wordListName
        }
        setListeners()
    }

    private fun setListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.llEditWordList.setOnClickListener {
            setFragmentResult(
                Requests.SHOW_EDIT_WORD_LIST_DIALOG,
                Bundle().apply {
                    putString(Extras.WORD_LIST_ID, wordListId)
                    putString(Extras.WORD_LIST_NAME, binding.tvWordListName.text.toString())
                }
            )
            dismiss()
        }
        binding.llDeleteWordList.setOnClickListener {
            setFragmentResult(
                Requests.SHOW_DELETE_WORD_LIST_DIALOG,
                Bundle().apply {
                    putString(Extras.WORD_LIST_ID, wordListId)
                }
            )
            dismiss()
        }
    }
}
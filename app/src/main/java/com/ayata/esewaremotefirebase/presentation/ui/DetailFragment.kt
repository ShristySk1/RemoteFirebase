package com.ayata.esewaremotefirebase.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ayata.esewaremotefirebase.R
import com.ayata.esewaremotefirebase.data.model.Quote
import com.ayata.esewaremotefirebase.databinding.FragmentDetailBinding
import com.ayata.esewaremotefirebase.presentation.MainActivity
import com.ayata.esewaremotefirebase.utils.hideSoftKeyboard

private const val QUOTE = "param1"

/**
 * This fragment is used for adding new quotes
 */
class DetailFragment : Fragment() {
    private var detailBundle: Quote? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            detailBundle = it.getSerializable(QUOTE) as Quote?
        }
    }

    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        (activity as MainActivity).showFab(false)
        initButton()
        return binding.root
    }

    private fun initButton() {
        binding.btnSave.setOnClickListener {
            val author = binding.etAuthor.text.toString()
            val quote = binding.etQuote.text.toString()
            if (author.trim().isNotEmpty() && quote.trim().isNotEmpty()) {
                //save to firebase firestore
                insertQuote(author, quote)
                activity?.hideSoftKeyboard(binding.root)

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_string_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }
    }


    /**
     * If we want to edit quote
     */
    private fun initPreviousDetail() {
        detailBundle?.let {
            binding.apply {
                binding.etAuthor.setText(it.author)
                binding.etQuote.setText(it.quote)
            }
        }
    }

    /**
     * To insert or update quote
     */
    private fun insertOrUpdateQuote(author: String, quote: String) {
        detailBundle?.let {
            (activity as MainActivity).updateQuote(detailBundle?.id!!, author, quote, binding.root)
        } ?: kotlin.run {
            insertQuote(author, quote)
        }
    }

    /**
     * To insert quote
     */
    private fun insertQuote(author: String, quote: String) {
        (activity as MainActivity).insertQuote(
            author,
            quote,
            binding.root
        )//add method auto generates id
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Quote?) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(QUOTE, param1)
                }
            }
    }
}
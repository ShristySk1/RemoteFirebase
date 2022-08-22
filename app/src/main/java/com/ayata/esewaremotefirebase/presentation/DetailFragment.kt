package com.ayata.esewaremotefirebase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ayata.esewaremotefirebase.data.model.Note
import com.ayata.esewaremotefirebase.databinding.FragmentDetailBinding
import com.ayata.esewaremotefirebase.utils.hideSoftKeyboard
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTE = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    private var detailBundle: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            detailBundle = it.getSerializable(NOTE) as Note?
        }
    }
    lateinit var binding: FragmentDetailBinding
    val databaseRef = FirebaseFirestore.getInstance()
    val collectionRef = databaseRef.collection("notebook")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initPreviousDetail()
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            if (title.trim().isNotEmpty() && description.trim().isNotEmpty()) {
                //save to firebase firestore
                detailBundle?.let {
                    collectionRef.document(detailBundle!!.id!!).update("title",title,"description",description)
                    Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
                }?: kotlin.run {
                    collectionRef.add(Note(title, description))//add method auto generates id
                    Toast.makeText(requireContext(), "Note Added", Toast.LENGTH_SHORT).show()
                }
                //hide keyboard
                activity?.hideSoftKeyboard(binding.root)
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please insert title and description.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun initPreviousDetail() {
        detailBundle?.let {
            binding.apply {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Note?) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(NOTE, param1)
                }
            }
    }
}
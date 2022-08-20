package com.ayata.esewaremotefirebase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.data.model.Note
import com.ayata.esewaremotefirebase.databinding.FragmentDashboardBinding
import com.ayata.esewaremotefirebase.presentation.ui.adapter.NoteAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        val query: Query = FirebaseFirestore.getInstance().collection("notebook")
        val option: FirestoreRecyclerOptions<Note> =
            FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java).build()
        noteAdapter = NoteAdapter(option)
        binding.rvNote.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
            hasFixedSize()
        }
        val touch = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteAdapter.deleteNote(viewHolder.bindingAdapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(touch)
        itemTouchHelper.attachToRecyclerView(binding.rvNote)
        noteAdapter.setNoteClickListener { documentSnapshot, position ->
            val note=documentSnapshot.toObject(Note::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }
}
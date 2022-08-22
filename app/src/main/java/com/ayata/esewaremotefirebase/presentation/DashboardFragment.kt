package com.ayata.esewaremotefirebase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.R
import com.ayata.esewaremotefirebase.data.RemoteConfigManager
import com.ayata.esewaremotefirebase.data.model.Note
import com.ayata.esewaremotefirebase.databinding.FragmentDashboardBinding
import com.ayata.esewaremotefirebase.presentation.ui.adapter.NoteAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    private lateinit var noteAdapter: NoteAdapter

    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        initRemoteConfig()
        setUpRecyclerView()
        return binding.root
    }

    private fun initRemoteConfig() {
        val newSubTitle = remoteConfigManager.getSubTitle()
        binding.tvSubtitle.text = newSubTitle
//        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 15
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.dashboard_default_values)
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    val updated = task.result
//                    Log.d("test", "Config params updated: $updated")
//                    Toast.makeText(
//                        requireContext(), "Fetch and activate succeeded",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    val appSubtitle = remoteConfig.getString("app_subtitle")
//                    binding.tvSubtitle.text = appSubtitle
//                } else {
//                    Toast.makeText(
//                        requireContext(), "Fetch failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
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
            val note = documentSnapshot.toObject(Note::class.java)
            note?.id = documentSnapshot.id
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, DetailFragment.newInstance(note))
                .addToBackStack("null").commit()

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
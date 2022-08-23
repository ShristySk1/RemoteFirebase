package com.ayata.esewaremotefirebase.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.R
import com.ayata.esewaremotefirebase.data.RemoteConfigManager
import com.ayata.esewaremotefirebase.data.model.Note
import com.ayata.esewaremotefirebase.databinding.FragmentDashboardBinding
import com.ayata.esewaremotefirebase.presentation.ui.adapter.NoteAdapter
import com.ayata.esewaremotefirebase.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    private lateinit var noteAdapter: NoteAdapter
    var avatarAnimateStartPointY: Float = 0F
    var avatarCollapseAnimationChangeWeight: Float = 0F
    var isCalculated = false
    var verticalToolbarAvatarMargin =0F
    var EXPAND_AVATAR_SIZE=200
    var horizontalToolbarAvatarMargin=10

    var COLLAPSE_IMAGE_SIZE=200

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
        setUpCollapsingAppbarBehaviour()
        return binding.root
    }

    private fun setUpCollapsingAppbarBehaviour() {
        /**/

//        binding.appBarLayout.addOnOffsetChangedListener(
//            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
//                updateAppbarView(Math.abs((i) / appBarLayout.totalScrollRange.toFloat()))
//            })
    }

//    private fun updateAppbarView(offset: Float) {
//        var appBarLayout=binding.appBarLayout
//        var toolbar=binding.toolbar
//        if (isCalculated.not()) {
////            (200-(100+10))/scroll range
//            avatarAnimateStartPointY =
//                Math.abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
//                    .toFloat()
//
//            avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
//
//            verticalToolbarAvatarMargin = ((toolbar.height - COLLAPSE_IMAGE_SIZE)).toFloat()
//            isCalculated = true
//        }
//
//        binding.tvSubtitle.apply {
//            when {
//                offset > avatarAnimateStartPointY -> {
//                    val avatarCollapseAnimateOffset = (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
////                    val avatarSize = EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
////                    this.layoutParams.also {
////                        it.height = Math.round(avatarSize)
////                        it.width = Math.round(avatarSize)
////                    }
//                    this.translationX = ((appBarLayout.width)) * avatarCollapseAnimateOffset
//                    this.translationY = ((toolbar.height)) * avatarCollapseAnimateOffset
//                }
//                else -> this.layoutParams.also {
//                    if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
//                        it.height = EXPAND_AVATAR_SIZE.toInt()
//                        it.width = EXPAND_AVATAR_SIZE.toInt()
//                        this.layoutParams = it
//                    }
//                    translationX = 0f
//                }
//            }
//
//        }
//    }

    private fun initRemoteConfig() {
        val newSubTitle = remoteConfigManager.getSubTitle()
        binding.tvSubtitle.text = newSubTitle
        val mainBackground=remoteConfigManager.getThemeData().color
        val contentColor=remoteConfigManager.getThemeData().content_color
        Log.d("testcolor", "initRemoteConfig: "+mainBackground);
        binding.appBarLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(mainBackground)));
        binding.tvSubtitle.setTextColor(Color.parseColor(contentColor))
        binding.collapseToolbar.setExpandedTitleColor(Color.parseColor(contentColor))
        binding.collapseToolbar.setCollapsedTitleTextColor(Color.parseColor(contentColor))

    }

    private fun setUpRecyclerView() {
        val query: Query = FirebaseFirestore.getInstance().collection("notebook")
        val option: FirestoreRecyclerOptions<Note> =
            FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java).build()
        noteAdapter = NoteAdapter(option)
        binding.rvNote.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
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
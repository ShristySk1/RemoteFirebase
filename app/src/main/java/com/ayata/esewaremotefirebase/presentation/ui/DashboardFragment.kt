package com.ayata.esewaremotefirebase.presentation.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.R
import com.ayata.esewaremotefirebase.constants.AppContracts
import com.ayata.esewaremotefirebase.data.configmanager.RemoteConfigManager
import com.ayata.esewaremotefirebase.data.model.Quote
import com.ayata.esewaremotefirebase.databinding.FragmentDashboardBinding
import com.ayata.esewaremotefirebase.presentation.MainActivity
import com.ayata.esewaremotefirebase.presentation.ui.adapter.QuoteAdapter
import com.ayata.esewaremotefirebase.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * This fragment contains the list of data from firebase firestore
 */
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    private lateinit var quoteAdapter: QuoteAdapter

    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        (activity as MainActivity).showFab(true)
        initRemoteConfig()
        setUpRecyclerView()
        setUpCollapsingAppbarBehaviour()
        return binding.root
    }

    private fun setUpCollapsingAppbarBehaviour() {
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                val percentage = 1 - (Math.abs(i) / appBarLayout.getTotalScrollRange().toFloat())
                binding.tvSubtitle.setAlpha(percentage)
            })
    }


    private fun initRemoteConfig() {
        //for dasboard subtitle only
        val newSubTitle = remoteConfigManager.getSubTitle()
        binding.tvSubtitle.text = newSubTitle

        //for collapsing toolbar background and content color
        val mainBackground = remoteConfigManager.getThemeData().color
        val contentColor = remoteConfigManager.getThemeData().content_color
        binding.appBarLayout.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    mainBackground
                )
            )
        );
        binding.tvSubtitle.setTextColor(Color.parseColor(contentColor))
        binding.collapseToolbar.setExpandedTitleColor(Color.parseColor(contentColor))
        binding.collapseToolbar.setCollapsedTitleTextColor(Color.parseColor(contentColor))

    }

    private fun setUpRecyclerView() {
        val query: Query = (activity as MainActivity).getCollectionReference()
        val option: FirestoreRecyclerOptions<Quote> =
            FirestoreRecyclerOptions.Builder<Quote>()
                .setQuery(query, Quote::class.java).build()
        quoteAdapter = QuoteAdapter(option)
        binding.rvQuote.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = quoteAdapter
            hasFixedSize()
        }
//        swipeDeleteQuote()
    }

    /**
     * For swipe deleting each quote
     * Not used for now
     */
    private fun swipeDeleteQuote() {
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
                quoteAdapter.deleteQuote(viewHolder.bindingAdapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(touch)
        itemTouchHelper.attachToRecyclerView(binding.rvQuote)
    }

    /**
     * For editing each quote
     * Not used for now
     */
    private fun quoteClick() {
        quoteAdapter.setQuoteClickListener { documentSnapshot, position ->
            val Quote = documentSnapshot.toObject(Quote::class.java)
            Quote?.id = documentSnapshot.id
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, DetailFragment.newInstance(Quote))
                .addToBackStack("null").commit()
        }
    }

    override fun onStart() {
        super.onStart()
        quoteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        quoteAdapter.stopListening()
    }
}
package com.ayata.esewaremotefirebase.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.data.model.Quote
import com.ayata.esewaremotefirebase.databinding.RecyclerDashboardQuoteBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class QuoteAdapter(options: FirestoreRecyclerOptions<Quote>) :
    FirestoreRecyclerAdapter<Quote, QuoteAdapter.QuoteViewHolder>(
        options
    ) {
    inner class QuoteViewHolder(val binding: RecyclerDashboardQuoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = RecyclerDashboardQuoteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    fun deleteQuote(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int, model: Quote) {
        holder.binding.apply {
            tvAuthor.text ="- "+ model.author
            tvQuote.text = model.quote
        }
        holder.itemView.setOnClickListener {
            quoteClickListener?.let {
                if(position!=-1)
                it(snapshots.getSnapshot(position),position)
            }
        }

    }
    private var quoteClickListener:((DocumentSnapshot, Int)->Unit)?=null
    fun setQuoteClickListener(listener:((DocumentSnapshot, Int)->Unit)){
        quoteClickListener=listener
    }


}
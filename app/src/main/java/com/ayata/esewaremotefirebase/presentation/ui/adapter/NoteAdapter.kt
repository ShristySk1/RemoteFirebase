package com.ayata.esewaremotefirebase.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayata.esewaremotefirebase.data.model.Note
import com.ayata.esewaremotefirebase.databinding.RecyclerNoteBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(options: FirestoreRecyclerOptions<Note>) :
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(
        options
    ) {
    inner class NoteViewHolder(val binding: RecyclerNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = RecyclerNoteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    fun deleteNote(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.binding.apply {
            title.text = model.title
            description.text = model.description
        }
        holder.itemView.setOnClickListener {
            noteClickListener?.let {
                if(position!=-1)
                it(snapshots.getSnapshot(position),position)
            }
        }

    }
    private var noteClickListener:((DocumentSnapshot,Int)->Unit)?=null
    fun setNoteClickListener(listener:((DocumentSnapshot,Int)->Unit)){
        noteClickListener=listener
    }


}
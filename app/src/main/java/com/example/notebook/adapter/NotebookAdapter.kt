package com.example.notebook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.EditActivity
import com.example.notebook.IntentConstants.INTENT_DESCRIPTION_KEY
import com.example.notebook.IntentConstants.INTENT_ID_KEY
import com.example.notebook.IntentConstants.INTENT_TITLE_KEY
import com.example.notebook.IntentConstants.INTENT_URI_KEY
import com.example.notebook.databinding.RecyclerViewItemBinding
import com.example.notebook.model.Note
import com.example.notebook.sqlite.NotebookDbManager

class NotebookAdapter(context: Context) :
    RecyclerView.Adapter<NotebookAdapter.NotebookViewHolder>() {

    private var notesList = ArrayList<Note>()
    private val _context = context

    class NotebookViewHolder(
        val binding: RecyclerViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = notesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotebookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemBinding.inflate(inflater, parent, false)

        return NotebookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotebookViewHolder, position: Int) {
        val note = notesList[position]

        holder.binding.itemTextView.text = note.title
        holder.binding.noteTime.text = note.time
        holder.binding.itemContainer.setOnClickListener {
            _context.startActivity(
                Intent(_context, EditActivity::class.java).apply {
                    putExtra(INTENT_ID_KEY, note.id)
                    putExtra(INTENT_TITLE_KEY, note.title)
                    putExtra(INTENT_DESCRIPTION_KEY, note.description)
                    putExtra(INTENT_URI_KEY, note.uri)
                }
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(notes: List<Note>) {
        notesList.clear()
        notesList.addAll(notes)
        notifyDataSetChanged()
    }

    fun removeAdapter(position: Int, dbManager: NotebookDbManager) {
        dbManager.removeItemFromDb(notesList[position].id.toString())
        notesList.removeAt(position)
        notifyItemRangeChanged(0, notesList.size)
        notifyItemRemoved(position)
    }

}


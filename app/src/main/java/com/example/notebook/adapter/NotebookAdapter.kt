package com.example.notebook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.EditActivity
import com.example.notebook.databinding.RecyclerViewItemBinding
import com.example.notebook.model.NotesList

class NotebookAdapter(context: Context) :
    RecyclerView.Adapter<NotebookAdapter.NotebookViewHolder>() {

    private var notesList = ArrayList<NotesList>()
    private val _context = context

    class NotebookViewHolder(
        private val binding: RecyclerViewItemBinding, contextViewHolder: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = contextViewHolder

        fun setData(notes: NotesList) {
            binding.itemTextView.text = notes.title

            binding.itemContainer.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(INTENT_TITLE_KEY, notes.title)
                    putExtra(INTENT_DESCRIPTION_KEY, notes.description)
                    putExtra(INTENT_URI_KEY, notes.uri)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotebookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemBinding.inflate(inflater, parent, false)

        return NotebookViewHolder(binding, _context)
    }

    override fun onBindViewHolder(holder: NotebookViewHolder, position: Int) {
        val notes = notesList[position]

        holder.setData(notes)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(notes: List<NotesList>) {
        notesList.clear()
        notesList.addAll(notes)

        notifyDataSetChanged()
    }

    companion object {

        private const val INTENT_TITLE_KEY = "TITLE_KEY"
        private const val INTENT_DESCRIPTION_KEY = "DESCRIPTION_KEY"
        private const val INTENT_URI_KEY = "URI_KEY"

    }
}


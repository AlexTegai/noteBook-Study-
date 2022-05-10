package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.adapter.NotebookAdapter
import com.example.notebook.databinding.ActivityMainBinding
import com.example.notebook.sqlite.NotebookDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter = NotebookAdapter(this)
    private val notebookDbManager = NotebookDbManager(this)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addFloatingButton.setOnClickListener {
            onAddButtonPressed()
        }

        initSearchView()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        notebookDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        notebookDbManager.openDb()
        fillAdapter()
    }

    private fun onAddButtonPressed() {
        val intent = Intent(this, EditActivity::class.java)
        startActivity(intent)
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
        getSwipeManager()
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                text?.let { fillAdapter(it) }
                return true
            }

        })
    }

    private fun fillAdapter(text: String = "") {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val notesList = notebookDbManager.readDb(text)
            adapter.updateAdapter(notesList)

            if (notesList.size > 0) {
                binding.emptyTextView.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun getSwipeManager() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeAdapter(viewHolder.absoluteAdapterPosition, notebookDbManager)
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

}
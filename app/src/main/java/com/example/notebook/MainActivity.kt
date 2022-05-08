package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebook.adapter.NotebookAdapter
import com.example.notebook.databinding.ActivityMainBinding
import com.example.notebook.sqlite.NotebookDbManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter = NotebookAdapter(this)
    private val notebookDbManager = NotebookDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addFloatingButton.setOnClickListener {
            addButtonPressed()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
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

    private fun addButtonPressed() {
        val intent = Intent(this, EditActivity::class.java)
        startActivity(intent)
    }

    private fun fillAdapter() {
        adapter.updateAdapter(notebookDbManager.readDb())
    }

}
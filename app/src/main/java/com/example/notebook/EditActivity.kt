package com.example.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.notebook.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            backImageButton.setOnClickListener {
                onBackButtonPressed()
            }

            newImageButton.setOnClickListener {
                onNewImageButtonPressed()
            }

            saveFloatingButton.setOnClickListener {
                onSaveNoteButtonPressed()
            }
        }
    }

    private fun onBackButtonPressed() {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
    }

    private fun onNewImageButtonPressed() {
        Toast.makeText(this, "New Image", Toast.LENGTH_SHORT).show()
    }

    private fun onSaveNoteButtonPressed() {
        Toast.makeText(this, "New Note", Toast.LENGTH_SHORT).show()
    }

}
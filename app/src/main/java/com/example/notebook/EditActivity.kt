package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.notebook.databinding.ActivityEditBinding
import com.example.notebook.sqlite.NotebookDbManager

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private val notebookDbManager = NotebookDbManager(this)
    private var launcher: ActivityResultLauncher<Intent>? = null

    private var tempImageUri = "empty"

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

            editAvatarButton.setOnClickListener {
                editAvatarButtonPressed()
            }

            deleteAvatarButton.setOnClickListener {
                deleteAvatarButtonPressed()
            }

            saveFloatingButton.setOnClickListener {
                onSaveNoteButtonPressed(
                    editTitle.text.toString(), editContent.text.toString(), tempImageUri
                )
            }
        }

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                binding.imageAvatar.setImageURI(result.data?.data)
                tempImageUri = result.data?.data.toString()
            }
        }

        checkContentIsNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        notebookDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        notebookDbManager.openDb()
    }

    private fun onBackButtonPressed() {
        finish()
    }

    private fun onNewImageButtonPressed() {
        binding.apply {
            avatarContainer.visibility = View.VISIBLE
            newImageButton.visibility = View.GONE
        }
    }

    private fun editAvatarButtonPressed() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launcher?.launch(intent)
    }

    private fun deleteAvatarButtonPressed() {
        binding.apply {
            avatarContainer.visibility = View.GONE
            newImageButton.visibility = View.VISIBLE
        }
    }

    private fun onSaveNoteButtonPressed(title: String, content: String, image: String) {
        if (title.isNotBlank() && content.isNotBlank()) {
            notebookDbManager.insertToDb(title, content, image)
        }
        finish()
    }

    private fun checkContentIsNotEmpty() {
        if (intent != null) {
            if (intent.getStringExtra(INTENT_TITLE_KEY) != null) {
                binding.editTitle.setText(intent.getStringExtra(INTENT_TITLE_KEY))
                binding.editContent.setText(intent.getStringExtra(INTENT_DESCRIPTION_KEY))
            }
        }
    }

    companion object {

        private const val INTENT_TITLE_KEY = "TITLE_KEY"
        private const val INTENT_DESCRIPTION_KEY = "DESCRIPTION_KEY"

    }
}
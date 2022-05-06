package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.notebook.databinding.ActivityEditBinding
import com.example.notebook.sqlite.NotebookDbManager

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private val notebookDbManager = NotebookDbManager(this)
    private var launcher: ActivityResultLauncher<Intent>? = null

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

//            saveFloatingButton.setOnClickListener {
//                onSaveNoteButtonPressed(
//                    editTitle.toString(), editContent.toString(),
//                )
//            }

            editAvatarButton.setOnClickListener {
                editAvatarButtonPressed()
            }

            deleteAvatarButton.setOnClickListener {
                deleteAvatarButtonPressed()
            }
        }

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
           if (result.resultCode == RESULT_OK) {
               binding.imageAvatar.setImageURI(result.data?.data)
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        notebookDbManager.closeDb()
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

    private fun onBackButtonPressed() {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
    }

    private fun onNewImageButtonPressed() {
        binding.apply {
            avatarContainer.visibility = View.VISIBLE
            newImageButton.visibility = View.GONE
        }
    }

//    private fun onSaveNoteButtonPressed(title: String, content: String, image: String) {
//        if (title.isNotBlank() || content.isNotBlank()) {
//            notebookDbManager.apply {
//                openDb()
//                insertToDb(title, content, image)
//            }
//        }
//    }

}
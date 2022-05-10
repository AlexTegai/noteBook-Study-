package com.example.notebook

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.notebook.IntentConstants.INTENT_DESCRIPTION_KEY
import com.example.notebook.IntentConstants.INTENT_ID_KEY
import com.example.notebook.IntentConstants.INTENT_TITLE_KEY
import com.example.notebook.IntentConstants.INTENT_URI_KEY
import com.example.notebook.databinding.ActivityEditBinding
import com.example.notebook.sqlite.NotebookDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private val notebookDbManager = NotebookDbManager(this)
    private var launcher: ActivityResultLauncher<Intent>? = null

    private var tempImageUri = "empty"
    private var isEditing = false
    private var id: Int = 0

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
                onEditAvatarButtonPressed()
            }

            deleteAvatarButton.setOnClickListener {
                onDeleteAvatarButtonPressed()
            }

            unlockFloatingButton.setOnClickListener {
                onUnlockButtonPressed()
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
                result.data?.data?.let {
                    contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }
        }

        checkItemIsNotEmpty()
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

    private fun onEditAvatarButtonPressed() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        launcher?.launch(intent)
    }

    private fun onDeleteAvatarButtonPressed() {
        tempImageUri = "empty"
        binding.apply {
            imageAvatar.setImageResource(R.drawable.avatar)
            avatarContainer.visibility = View.GONE
            newImageButton.visibility = View.VISIBLE
        }
    }

    private fun onUnlockButtonPressed() {
        binding.apply {
            if (avatarContainer.visibility == View.GONE) newImageButton.visibility = View.VISIBLE

            editTitle.isEnabled = true
            editContent.isEnabled = true
            editAvatarButton.visibility = View.VISIBLE
            deleteAvatarButton.visibility = View.VISIBLE
            unlockFloatingButton.visibility = View.GONE
        }
    }

    private fun onSaveNoteButtonPressed(title: String, description: String, imageUri: String) {
        if (title.isNotBlank() && description.isNotBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                if (isEditing) {
                    notebookDbManager.updateItemInDb(
                        title,
                        description,
                        imageUri,
                        id,
                        getCurrentTime()
                    )
                } else {
                    notebookDbManager.insertToDb(title, description, imageUri, getCurrentTime())
                }
                finish()
            }
        }
    }

    private fun checkItemIsNotEmpty() {
        val itemId = intent.getIntExtra(INTENT_ID_KEY, 0)
        val uri = intent.getStringExtra(INTENT_URI_KEY)
        val title = intent.getStringExtra(INTENT_TITLE_KEY)
        val description = intent.getStringExtra(INTENT_DESCRIPTION_KEY)

        if (intent != null) {
            if (title != null) {
                isEditing = true
                id = itemId

                binding.apply {
                    editTitle.isEnabled = false
                    editContent.isEnabled = false
                    editAvatarButton.visibility = View.GONE

                    unlockFloatingButton.visibility = View.VISIBLE
                    newImageButton.visibility = View.GONE
                    deleteAvatarButton.visibility = View.GONE

                    editTitle.setText(title)
                    editContent.setText(description)

                    if (uri != "empty" && uri != null) {
                        tempImageUri = uri
                        avatarContainer.visibility = View.VISIBLE
                        newImageButton.visibility = View.GONE

                        imageAvatar.setImageURI(Uri.parse(tempImageUri))
                    }
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }

}
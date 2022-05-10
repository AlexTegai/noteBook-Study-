package com.example.notebook.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notebook.model.Note
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_DESCRIPTION
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_IMAGE_URI
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_TIME
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_TITLE
import com.example.notebook.sqlite.NotebookContract.TABLE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotebookDbManager(
    context: Context
) {
    private val dbHelper = NotebookDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    @SuppressLint("Range", "Recycle")
    suspend fun readDb(searchText: String): ArrayList<Note> = withContext(Dispatchers.IO) {
        val dataList = ArrayList<Note>()
        val selection = "$COLUMN_NAME_TITLE like ?"
        val cursor = db?.query(
            TABLE_NAME,
            null,
            selection,
            arrayOf("%$searchText%"),
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val note = Note(
                id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)),
                description = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION)),
                uri = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMAGE_URI)),
                time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME))
            )

            dataList.add(note)
        }
        cursor?.close()
        return@withContext dataList
    }

    suspend fun insertToDb(
        title: String,
        description: String,
        uri: String,
        time: String
    ) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_IMAGE_URI, uri)
            put(COLUMN_NAME_TIME, time)
        }

        db?.insert(TABLE_NAME, null, values)
    }

    suspend fun updateItemInDb(
        title: String,
        description: String,
        uri: String,
        id: Int,
        time: String
    ) = withContext(Dispatchers.IO) {
        val selection = BaseColumns._ID + "=$id"

        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_IMAGE_URI, uri)
            put(COLUMN_NAME_TIME, time)
        }

        db?.update(TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(TABLE_NAME, selection, null)
    }

    fun closeDb() {
        dbHelper.close()
    }
}
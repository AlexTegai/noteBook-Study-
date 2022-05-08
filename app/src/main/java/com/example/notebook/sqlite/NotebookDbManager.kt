package com.example.notebook.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notebook.model.NotesList

class NotebookDbManager(
    context: Context
) {
    private val dbHelper = NotebookDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertToDb(title: String, description: String, uri: String) {
        val values = ContentValues().apply {
            put(NotebookContract.COLUMN_NAME_TITLE, title)
            put(NotebookContract.COLUMN_NAME_DESCRIPTION, description)
            put(NotebookContract.COLUMN_NAME_IMAGE_URI, uri)
        }

        db?.insert(NotebookContract.TABLE_NAME, null, values)
    }

    @SuppressLint("Range", "Recycle")
    fun readDb(): ArrayList<NotesList> {
        val dataList = ArrayList<NotesList>()
        val cursor = db?.query(
            NotebookContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val notes = NotesList(
                title = cursor.getString(
                    cursor.getColumnIndex(NotebookContract.COLUMN_NAME_TITLE)
                ),
                description = cursor.getString(
                    cursor.getColumnIndex(NotebookContract.COLUMN_NAME_DESCRIPTION)
                ),
                uri = cursor.getString(
                    cursor.getColumnIndex(NotebookContract.COLUMN_NAME_IMAGE_URI)
                )
            )

            dataList.add(notes)
        }
        cursor?.close()
        return dataList
    }

    fun removeItemFrmDb(id: String) {
        val selection = BaseColumns._ID + "=i$id"
        db?.delete(NotebookContract.TABLE_NAME, selection, null)
    }

    fun closeDb() {
        dbHelper.close()
    }
}
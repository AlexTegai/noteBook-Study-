package com.example.notebook.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class NotebookDbManager(
    context: Context
) {
    private val dbHelper = NotebookDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertToDb(title: String, content: String, uri: String) {
        val values = ContentValues().apply {
            put(NotebookContract.COLUMN_NAME_TITLE, title)
            put(NotebookContract.COLUMN_NAME_CONTENT, content)
            put(NotebookContract.COLUMN_NAME_IMAGE_URI, uri)
        }

        db?.insert(NotebookContract.TABLE_NAME, null, values)
    }

    @SuppressLint("Range", "Recycle")
    fun readDb(): ArrayList<String> {
        val dataList = ArrayList<String>()
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
            val dataText =
                cursor.getString(cursor.getColumnIndex(NotebookContract.COLUMN_NAME_TITLE))
            dataList.add(dataText.toString())
        }
        cursor?.close()
        return dataList
    }

    fun closeDb() {
        dbHelper.close()
    }
}
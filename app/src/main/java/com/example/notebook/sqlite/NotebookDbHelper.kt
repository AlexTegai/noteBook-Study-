package com.example.notebook.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_DESCRIPTION
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_IMAGE_URI
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_TIME
import com.example.notebook.sqlite.NotebookContract.COLUMN_NAME_TITLE
import com.example.notebook.sqlite.NotebookContract.TABLE_NAME

class NotebookDbHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DELETE_TABLE)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Notebook.db"
    }

}

private const val CREATE_TABLE =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_DESCRIPTION TEXT," +
            "$COLUMN_NAME_IMAGE_URI TEXT," +
            "$COLUMN_NAME_TIME TEXT)"

private const val DELETE_TABLE =
    "DROP TABLE IF EXISTS $TABLE_NAME"



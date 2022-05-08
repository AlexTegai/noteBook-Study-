package com.example.notebook.sqlite

import android.provider.BaseColumns

object NotebookContract : BaseColumns {

    const val TABLE_NAME = "note"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DESCRIPTION = "description"
    const val COLUMN_NAME_IMAGE_URI = "uri"

}


package com.example.mymodule

import android.content.Context
import android.net.Uri

object MyClass {
    fun foo(
        context: Context,
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ) {
        val cursor =
            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (it.moveToNext()) {
                val foo = it.getString(it.getColumnIndex("Foo"))
            }
        }
    }
}
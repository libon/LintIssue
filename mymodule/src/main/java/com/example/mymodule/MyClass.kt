package com.example.mymodule

import android.Manifest
import androidx.annotation.RequiresPermission

object MyClass {
    fun foo(
    ) {
        fooImpl()
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun fooImpl() {
    }
}
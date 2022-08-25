package com.ayata.esewaremotefirebase.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


fun Activity.hideSoftKeyboard(view: View) {
    val imm: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showSnackbar(message: String) {
    // Create a snackbar
    Snackbar
        .make(
            this,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
}
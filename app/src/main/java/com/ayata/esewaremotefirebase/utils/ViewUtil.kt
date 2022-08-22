package com.ayata.esewaremotefirebase.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard(view: View) {
    val imm: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
}
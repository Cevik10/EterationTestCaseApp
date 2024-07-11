package com.hakancevik.eterationtestcaseapp.extension

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.hakancevik.eterationtestcaseapp.R


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Activity.customToast(toastMessage: String) {
    val inflater = this.layoutInflater
    val toast = Toast(this)
    toast.duration = Toast.LENGTH_SHORT
    val layout = inflater.inflate(
        R.layout.custom_toast,
        this.findViewById(R.id.custom_toast_constraint)
    )
    val toastTextView: TextView = layout.findViewById<View>(R.id.custom_toast_text) as TextView
    toastTextView.text = toastMessage
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    toast.view = layout
    toast.show()
}

fun Activity.createCustomProgressDialog(progressTitle: String): AlertDialog {
    val view = LayoutInflater.from(this).inflate(R.layout.custom_progress_dialog, null)

    view.findViewById<TextView>(R.id.progressTitleText).text = progressTitle

    val dialog = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(false)
        .create()

    dialog.window?.setBackgroundDrawableResource(R.drawable.bg_round)
    dialog.window?.setGravity(Gravity.CENTER)

    return dialog
}

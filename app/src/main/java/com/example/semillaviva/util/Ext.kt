package com.example.semillaviva.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.text():String = editText!!.text.toString()

fun AppCompatActivity.toast(msg:Int) = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    .show()

fun Fragment.toastF(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG)
    .show()
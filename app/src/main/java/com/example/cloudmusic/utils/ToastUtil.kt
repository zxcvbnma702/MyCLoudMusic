package com.example.cloudmusic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * @author:SunShibo
 * @date:2022-07-03 12:56
 * @feature:
 */
@SuppressLint("StaticFieldLeak")
object ToastUtil {
    const val SHORT = 0
    const val LONG = 1

    private lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    fun MyToast(value: String, time: Int){
        Toast.makeText(context, value, time).show()
    }
}
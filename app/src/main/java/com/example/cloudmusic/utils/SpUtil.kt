package com.example.cloudmusic.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author:SunShibo
 * @date:2022-07-03 12:47
 * @feature:
 */
object SpUtil {

    private lateinit var sp: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    fun init(context: Context){
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        edit = sp.edit()
    }

    fun putString(key: String, value: String){
        edit.putString(key, value)
        edit.commit()
    }

    fun getString(key: String, defValue: String): String? =
        sp.getString(key, defValue)
}
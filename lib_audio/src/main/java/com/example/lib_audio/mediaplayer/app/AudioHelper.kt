package com.example.lib_audio.mediaplayer.app

import android.content.Context

/**
 * @author:SunShibo
 * @date:2022-06-11 10:33
 * @feature:
 */
class AudioHelper {
    private var context: Context? = null

    fun init(context: Context){
        this.context = context
    }

    fun getContext() = context

    companion object {
        val instance by lazy { AudioHelper() }
    }

}
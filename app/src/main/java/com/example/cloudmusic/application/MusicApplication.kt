package com.example.cloudmusic.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.cloudmusic.utils.SpUtil
import com.example.cloudmusic.utils.ToastUtil
import com.example.lib_audio.mediaplayer.app.AudioHelper

/**
 * @author:SunShibo
 * @date:2022-06-10 22:31
 * @feature:
 */
class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AudioHelper.init(this)
        SpUtil.init(this)
        ToastUtil.init(this)
    }
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
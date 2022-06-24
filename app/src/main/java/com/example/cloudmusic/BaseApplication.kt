package com.example.cloudmusic

import android.app.Application
import com.example.lib_audio.mediaplayer.app.AudioHelper

/**
 * @author:SunShibo
 * @date:2022-06-10 22:31
 * @feature:
 */
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AudioHelper.instance.init(this)
    }
}
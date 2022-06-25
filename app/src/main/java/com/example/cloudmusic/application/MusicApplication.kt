package com.example.cloudmusic.application

import android.app.Application
import com.example.lib_audio.mediaplayer.app.AudioHelper

/**
 * @author:SunShibo
 * @date:2022-06-10 22:31
 * @feature:
 */
class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AudioHelper.init(this)
    }
}
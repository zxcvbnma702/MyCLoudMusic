package com.example.lib_audio.mediaplayer.app

import android.annotation.SuppressLint
import android.content.Context
import com.example.lib_audio.mediaplayer.core.AudioController
import com.example.lib_audio.mediaplayer.core.AudioController.pause
import com.example.lib_audio.mediaplayer.core.AudioController.resume
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_audio.mediaplayer.service.MusicService

/**
 * @author:SunShibo
 * @date:2022-06-25 19:13
 * @feature:
 */

@SuppressLint("StaticFieldLeak")
object AudioHelper {
    //SDK全局Context, 供子模块用
    internal lateinit var context: Context

    fun init(context: Context) {
        AudioHelper.context = context
    }

    fun addAudio(list: ArrayList<AudioBean>) {
        AudioController.addAudio(list)
        //        MusicPlayerActivity.start(activity);
    }

    fun startServices() = MusicService.startMusicService()

    fun setIndex(index: Int) = AudioController.setPlayIndex(index)


    fun pauseAudio() = pause()
    fun resumeAudio() = resume()

}
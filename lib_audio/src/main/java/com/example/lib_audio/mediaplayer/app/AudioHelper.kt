package com.example.lib_audio.mediaplayer.app

import android.annotation.SuppressLint
import android.content.Context
import com.example.lib_audio.mediaplayer.core.AudioController.addAudio
import com.example.lib_audio.mediaplayer.core.AudioController.pause
import com.example.lib_audio.mediaplayer.core.AudioController.resume
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_audio.mediaplayer.core.AudioController
import java.util.ArrayList

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
        //初始化本地数据库
    }

    fun addAudio(list: ArrayList<AudioBean>) {
        AudioController.addAudio(list)
        //        MusicPlayerActivity.start(activity);
    }

    fun pauseAudio() = pause()
    fun resumeAudio() = resume()

}
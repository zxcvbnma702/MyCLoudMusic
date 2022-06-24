package com.example.lib_audio.mediaplayer.event

import com.example.lib_audio.mediaplayer.core.AudioController

/**
 * @author:SunShibo
 * @date:2022-06-11 19:31
 * @feature:
 */
class AudioPlayModeEvent(playMode: AudioController.PlayMode?) {
    var mPlayMode: AudioController.PlayMode? = null

    init {
        mPlayMode = playMode
    }
}
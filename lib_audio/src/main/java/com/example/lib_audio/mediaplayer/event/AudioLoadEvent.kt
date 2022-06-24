package com.example.lib_audio.mediaplayer.event

import com.example.lib_audio.mediaplayer.model.AudioBean

/**
 * @author:SunShibo
 * @date:2022-06-11 12:05
 * @feature:
 */
class AudioLoadEvent(audioBean: AudioBean) {
    var audioBean: AudioBean

    init {
        this.audioBean = audioBean
    }
}
package com.example.lib_audio.mediaplayer.event

import com.example.lib_audio.mediaplayer.core.CustomMediaPlayer

/**
 * @author:SunShibo
 * @date:2022-06-11 11:37
 * @feature: 播放进度事件
 */
class AudioProgressEvent(status: CustomMediaPlayer.Status, progress: Double, maxLength: Double) {
    var mStatus: CustomMediaPlayer.Status
    var progress: Double
    var maxLength: Double

    init {
        mStatus = status
        this.progress = progress
        this.maxLength = maxLength
    }
}
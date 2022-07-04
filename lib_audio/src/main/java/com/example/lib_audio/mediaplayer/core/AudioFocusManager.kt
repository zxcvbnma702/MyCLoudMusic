package com.example.lib_audio.mediaplayer.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author:SunShibo
 * @date:2022-06-10 23:10
 * @feature: 音频焦点监听器
 */

internal class AudioFocusManager(context: Context, listener: AudioFocusListener): AudioManager.OnAudioFocusChangeListener {
    private val TAG = AudioFocusManager::class.java.simpleName

    private var mAudioFocusListener: AudioFocusListener
    private var audioManager: AudioManager

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioFocusListener = listener
    }

    fun requestAudioFocus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    .setAcceptsDelayedFocusGain(false)
                    .build()) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            audioManager.requestAudioFocus(
            this, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .build())
        }else{
            audioManager.abandonAudioFocus(this)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            // 重新获得焦点
            AudioManager.AUDIOFOCUS_GAIN -> mAudioFocusListener.audioFocusGrant()
            // 永久丢失焦点，如被其他播放器抢占
            AudioManager.AUDIOFOCUS_LOSS -> mAudioFocusListener.audioFocusLoss()
            // 短暂丢失焦点，如来电
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> mAudioFocusListener.audioFocusLossTransient()
            // 瞬间丢失焦点，如通知
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mAudioFocusListener.audioFocusLossDuck()
        }
    }

    /**
     * 音频焦点改变,接口回调，
     */
    interface AudioFocusListener {
        //获得焦点回调处理
        fun audioFocusGrant()

        //永久失去焦点回调处理
        fun audioFocusLoss()

        //短暂失去焦点回调处理
        fun audioFocusLossTransient()

        //瞬间失去焦点回调
        fun audioFocusLossDuck()
    }
}
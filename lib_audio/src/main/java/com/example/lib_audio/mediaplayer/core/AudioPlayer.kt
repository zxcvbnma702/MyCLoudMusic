package com.example.lib_audio.mediaplayer.core

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.SEEK_CLOSEST
import android.net.wifi.WifiManager
import android.os.*
import android.provider.MediaStore
import android.util.Log
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.event.*
import com.example.lib_audio.mediaplayer.model.AudioBean
import org.greenrobot.eventbus.EventBus

/**
 * @author:SunShibo
 * @date:2022-06-10 23:03
 * @feature:播放音频, 发送事件, 对各种事物的一层封装
 */

internal class AudioPlayer() : MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioFocusManager.AudioFocusListener{

    private var isPauseByFocusLossTransient: Boolean = false
    private val TAG = "AudioPlayer"
    private val TIME_MSG = 0x01
    private val TIME_INVAL = 100

    private var cmp: CustomMediaPlayer = CustomMediaPlayer()
    private var mWifiLock: WifiManager.WifiLock
    private var mAudioFocusManager: AudioFocusManager

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIME_MSG ->           //暂停也要更新进度，防止UI不同步，只不过进度一直一样
                    if (getStatus() === CustomMediaPlayer.Status.STARTED || getStatus() === CustomMediaPlayer.Status.PAUSED) {
                        //UI类型处理事件
                        EventBus.getDefault().post(
                                AudioProgressEvent(
                                    getStatus(),
                                    getCurrentPosition(),
                                    getDuration()
                                )
                            )
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL.toLong())
                    }
            }
        }
    }

    init {
        val attr = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
        cmp.apply {
            setWakeMode(AudioHelper.context, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(attr)
            setOnCompletionListener(this)
            setOnPreparedListener(this@AudioPlayer)
            setOnBufferingUpdateListener(this@AudioPlayer)
            setOnErrorListener(this@AudioPlayer)
        }
        mWifiLock = ((AudioHelper.context.applicationContext.getSystemService(Context.WIFI_SERVICE)) as WifiManager)
            .createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, TAG)
        mAudioFocusManager = AudioFocusManager(AudioHelper.context, this)
    }

    /**
     * 获取播放器状态
     */
    fun getStatus(): CustomMediaPlayer.Status  = cmp.getState()

    /**
     * 获取当前音乐总时长
     */
    fun getDuration(): Int {
        return if (getStatus() === CustomMediaPlayer.Status.STARTED
            || getStatus() === CustomMediaPlayer.Status.PAUSED
        ) {
            cmp.duration
        } else 0
    }

    /**
     * 获取当前进度
     */
    fun getCurrentPosition(): Int {
        return if (getStatus() === CustomMediaPlayer.Status.STARTED
            || getStatus() === CustomMediaPlayer.Status.PAUSED
        ) {
            cmp.currentPosition
        } else 0
    }

    /**
     * 跳转到指定时间
     */
    fun seekTo(time : Long){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            cmp.seekTo(time, SEEK_CLOSEST)
        }
    }

    /**
   * prepare以后自动调用start方法,外部不能调用
   */
    private fun start() {
        // 获取音频焦点,保证播放器顺利播放
        if (!mAudioFocusManager.requestAudioFocus()) {
            Log.e(TAG, "获取音频焦点失败")
        }
        cmp.start()
        // 启用wifi锁
        mWifiLock.acquire()
        //更新进度
        mHandler.sendEmptyMessage(TIME_MSG)
        //发送start事件，UI类型处理事件
        EventBus.getDefault().post(AudioStartEvent())
    }

    /**
     * 加载音频资源
     */
    fun load(audioBean: AudioBean){
        try {
            cmp.apply {
                reset()
                setDataSource(audioBean.url)
                prepareAsync()
            }
            EventBus.getDefault().post(AudioLoadEvent(audioBean))
        }catch (e: Exception){
            EventBus.getDefault().post(AudioErrorEvent(AudioErrorEvent.Error.loadError))
        }
    }

    /**
     * 恢复播放
     */
    fun resume() {
        if(getStatus() == CustomMediaPlayer.Status.PAUSED){
            start()
        }
    }

    /**
     * 暂停播放
     */
    fun pause() {
        if(getStatus() == CustomMediaPlayer.Status.STARTED){
            cmp.pause()
            if(mWifiLock.isHeld){
                mWifiLock.release()
            }
            mAudioFocusManager.abandonAudioFocus()
            EventBus.getDefault().post(AudioPauseEvent())
        }
    }

    /**
     * 释放全部资源
     */
    fun release(){
        cmp.release()
        // 取消音频焦点
        mAudioFocusManager.abandonAudioFocus()
        // 关闭wifi锁
        if (mWifiLock.isHeld) {
            mWifiLock.release()
        }
        mHandler.removeCallbacksAndMessages(null)
        //发送销毁播放器事件,清除通知等
        EventBus.getDefault().post(AudioReleaseEvent())
    }

    /**
     * 设置音量
     */
    private fun setVolumn(fl: Float, fr: Float) = cmp.setVolume(fl, fr)

    /**
     * 请求焦点成功
     */
    override fun audioFocusGrant() {
        setVolumn(1.0f, 1.0f)
        if(isPauseByFocusLossTransient){
            resume()
        }
        isPauseByFocusLossTransient = false
    }

    /**
     * 永久失去焦点
     */
    override fun audioFocusLoss() {
        pause()
        isPauseByFocusLossTransient = true
    }

    /**
     * 短暂失去焦点
     */
    override fun audioFocusLossTransient() {
        pause()
        isPauseByFocusLossTransient = true
    }

    /**
     * 瞬间失去焦点
     */
    override fun audioFocusLossDuck() {
        setVolumn(0.5f, 0.5f)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        EventBus.getDefault().post(AudioCompleteEvent())
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }

    override fun onPrepared(mp: MediaPlayer?) {
        start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        EventBus.getDefault().post(AudioErrorEvent(AudioErrorEvent.Error.error))
        return true
    }

}
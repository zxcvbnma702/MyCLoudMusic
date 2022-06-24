package com.example.lib_audio.mediaplayer.core

import android.media.MediaPlayer
import java.io.IOException

/**
 * @author:SunShibo
 * @date:2022-06-10 22:50
 * @feature:状态机
 */

class CustomMediaPlayer() : MediaPlayer(), MediaPlayer.OnCompletionListener{

    enum class Status {
        IDLE, INITIALIZED, STARTED, PAUSED, STOPPED, COMPLETED
    }

    private var mState: Status? = null

    private var mOnCompletionListener: OnCompletionListener? = null

    init {
        mState = Status.IDLE
        super.setOnCompletionListener(this)
    }

    override fun reset() {
        super.reset()
        mState = Status.IDLE
    }

    @Throws(
        IOException::class,
        IllegalArgumentException::class,
        SecurityException::class,
        IllegalStateException::class
    )

    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        mState = Status.INITIALIZED
    }

    override fun start() {
        super.start()
        mState = Status.STARTED
    }

    override fun setOnCompletionListener(listener: OnCompletionListener?) {
        mOnCompletionListener = listener
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mState = Status.COMPLETED
        if (mOnCompletionListener != null) {
            mOnCompletionListener!!.onCompletion(mp)
        }
    }

    @Throws(IllegalStateException::class)
    override fun stop() {
        super.stop()
        mState = Status.STOPPED
    }

    @Throws(IllegalStateException::class)
    override fun pause() {
        super.pause()
        mState = Status.PAUSED
    }

    fun setState(mState: Status?) {
        this.mState = mState
    }

    fun getState(): Status? {
        return mState
    }

    fun isComplete(): Boolean {
        return mState == Status.COMPLETED
    }
}
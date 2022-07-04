package com.example.lib_audio.mediaplayer.core

import android.media.MediaPlayer
import java.io.IOException

/**
 * @author:SunShibo
 * @date:2022-06-10 22:50
 * @feature:状态机
 */
class CustomMediaPlayer internal constructor() : MediaPlayer(), MediaPlayer.OnCompletionListener {
    private var mState = Status.IDLE
    private lateinit var mListener: OnCompletionListener

    enum class Status {
        IDLE, INITIALIZED, STARTED, PAUSED, STOPPED, COMPLETED, PREPARED
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mState = Status.COMPLETED
            mListener.onCompletion(mp)
    }

    @Throws(
        IOException::class,
        IllegalArgumentException::class,
        IllegalStateException::class,
        SecurityException::class
    )
    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        mState = Status.INITIALIZED
    }

    @Throws(IllegalStateException::class)
    override fun start() {
        super.start()
        mState = Status.STARTED
    }

    @Throws(IllegalStateException::class)
    override fun pause() {
        super.pause()
        mState = Status.PAUSED
    }

    @Throws(IllegalStateException::class)
    override fun stop() {
        super.stop()
        mState = Status.STOPPED
    }

    override fun reset() {
        super.reset()
        mState = Status.IDLE
    }

    @Throws(IOException::class, IllegalStateException::class)
    override fun prepare() {
        super.prepare()
        mState = Status.PREPARED
    }

    fun setState(mState: Status) {
        this.mState = mState
    }

    fun getState(): Status {
        return mState
    }

    fun isComplete(): Boolean {
        return mState == Status.COMPLETED
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        mListener = listener
    }

    init {
        mState = Status.IDLE
        super.setOnCompletionListener(this)
    }
}
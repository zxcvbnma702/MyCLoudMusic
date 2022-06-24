package com.example.lib_audio.mediaplayer.event

/**
 * @author:SunShibo
 * @date:2022-06-11 12:08
 * @feature:
 */
class AudioErrorEvent(error: Error) {

    enum class Error{
        loadError,
        error
    }

    var error: Error

    init {
        this.error = error
    }
}
package com.example.lib_audio.mediaplayer.model

import io.objectbox.annotation.Entity

/**
 * @author:SunShibo
 * @date:2022-06-11 11:53
 * @feature:
 */

data class AudioBean (
    val id: String,
    val url: String,
    val name: String,
    val author: String,
    val album: String,
    val albumInfo: String,
    val albumPic: String,
    val totalTime: String
){
    constructor() : this("999", "1", "error", "error", "error", "error", "error", "error")

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        return if (other !is AudioBean) { false } else other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + albumInfo.hashCode()
        result = 31 * result + albumPic.hashCode()
        result = 31 * result + totalTime.hashCode()
        return result
    }
}
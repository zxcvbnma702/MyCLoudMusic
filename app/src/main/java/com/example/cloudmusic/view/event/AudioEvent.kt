package com.example.cloudmusic.view.event

import com.example.lib_audio.mediaplayer.model.AudioBean

class AudioEvent(musics: ArrayList<AudioBean>) {
    private var musics: ArrayList<AudioBean>
    init {
        this.musics = musics
    }
    fun getMusic() = musics
}

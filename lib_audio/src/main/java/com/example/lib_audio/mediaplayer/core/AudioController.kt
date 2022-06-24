package com.example.lib_audio.mediaplayer.core

import com.example.lib_audio.mediaplayer.event.AudioCompleteEvent
import com.example.lib_audio.mediaplayer.event.AudioErrorEvent
import com.example.lib_audio.mediaplayer.event.AudioPlayModeEvent
import com.example.lib_audio.mediaplayer.exception.AudioQueueEmptyException
import com.example.lib_audio.mediaplayer.model.AudioBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * @author:SunShibo
 * @date:2022-06-11 13:01
 * @feature:音频控制类
 */
class AudioController {
    enum class PlayMode{
        LOOP,
        RANDOM,
        REPEAT
    }

    companion object{
        val instance by lazy{ AudioController() }
    }

    private var audioPlayer: AudioPlayer = AudioPlayer()
    private var queue: ArrayList<AudioBean> = ArrayList()
    private var indexQueue = 0
    private var playMode: PlayMode

    init {
        EventBus.getDefault().register(this)
        indexQueue = 0
        playMode = PlayMode.LOOP
    }

    //播放队列
    fun getQueue(): ArrayList<AudioBean> = queue

    fun setQueue(queue: ArrayList<AudioBean>){
        setQueue(queue, 0)
    }
    fun setQueue(queue: ArrayList<AudioBean>, index: Int){
        this.queue.addAll(queue)
        indexQueue = index
    }

    //单个歌曲操作
    fun addAudio(bean: AudioBean){
        addAudio(0 ,bean)
    }
    fun addAudio(index: Int, bean: AudioBean){
        if(queue == null){
            throw AudioQueueEmptyException("当前播放队列为空!!!")
        }
        val query = queryAudio(bean)
        if(query){
            val nowBean = getNowPlaying()
            if(nowBean.id != bean.id){
                setPlayIndex(index)
            }
        }else{
            addCustomAudio(index, bean)
            setPlayIndex(index)
        }
    }

    private fun addCustomAudio(index: Int, bean: AudioBean) {
        queue.add(index, bean)
    }

    private fun setPlayIndex(index: Int) {
        indexQueue = index
        play()
    }

    private fun queryAudio(bean: AudioBean): Boolean = queue.contains(bean)

    //播放模式
    fun getPlayMode(): PlayMode = playMode
    fun setPlayMode(playMode: PlayMode){
        this.playMode = playMode
        //对外发送切换事件，更新UI
        EventBus.getDefault().post(AudioPlayModeEvent(this.playMode))
    }

    //播放索引
    fun getPlayIndex(): Int = indexQueue;
    fun setIndexQueue(index: Int){
        if(queue.isEmpty()){
            throw AudioQueueEmptyException("当前播放队列为空!!!")
        }
        this.indexQueue = index
        play()
    }
    fun play() {
        val audio = getNowPlaying()
        audioPlayer.load(audio)
    }
    fun playNext(){
        val audio = getNextPlaying()
        audioPlayer.load(audio)
    }
    fun playPrevious(){
        val audio = getPreviousPlaying()
        audioPlayer.load(audio)
    }

    /**
     * 切换播放或暂停
     */
    fun playOrPause(){
        if(isPauseStatus()){
            resume()
        }else if(isStartState()){
            pause()
        }
    }

    fun pause() = audioPlayer.pause()
    fun resume() = audioPlayer.resume()
    fun release(){
        audioPlayer.release()
        EventBus.getDefault().unregister(this)
    }


    /**
     * 对外提供获取当前播放时间
     */
    fun getNowPlayTime(): Int = audioPlayer.getCurrentPosition()
    /**
     * 对外提供获取总播放时间
     */
    fun getTotalPlayTime(): Int = audioPlayer.getCurrentPosition()

    //状态判断
    fun isStartState(): Boolean = CustomMediaPlayer.Status.STARTED == getStatus()
    fun isPauseStatus(): Boolean = CustomMediaPlayer.Status.PAUSED == getStatus()
    private fun getStatus(): CustomMediaPlayer.Status? = audioPlayer.getStatus()

    //获取歌曲
    fun getNowPlaying(): AudioBean = getPlaying()

    /**
     * 返回下一首歌曲
     */
    private fun getNextPlaying(): AudioBean {
        when(playMode){
            PlayMode.LOOP -> this@AudioController.indexQueue = (indexQueue + 1) % queue.size
            PlayMode.RANDOM -> this@AudioController.indexQueue = Random().nextInt(queue.size) % queue.size
            PlayMode.REPEAT -> this@AudioController.indexQueue
        }
        return getPlaying()
    }

    /**
     * 返回前一首歌曲
     */
    private fun getPreviousPlaying(): AudioBean {
        when(playMode){
            PlayMode.LOOP -> this@AudioController.indexQueue = (indexQueue - 1) % queue.size
            PlayMode.RANDOM -> this@AudioController.indexQueue = Random().nextInt(queue.size) % queue.size
            PlayMode.REPEAT -> this@AudioController.indexQueue
        }
        return getPlaying()
    }

    /**
     * 返回现在播放的歌曲
     */
    private fun getPlaying(): AudioBean {
        return if(queue != null && queue.isNotEmpty() && indexQueue >= 0 && indexQueue <= queue.size){
            queue[indexQueue]
        }else{
            throw AudioQueueEmptyException("当前播放队列为空或索引非法")
        }
    }

    //插放完毕事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioCompleteEvent(
        event: AudioCompleteEvent?
    ) {
        playNext()
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioErrorEvent(event: AudioErrorEvent?) {
        playNext()
    }

}
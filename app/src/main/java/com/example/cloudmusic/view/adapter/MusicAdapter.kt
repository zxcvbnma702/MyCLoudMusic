package com.example.cloudmusic.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.R
import com.example.cloudmusic.model.News
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.model.AudioBean

/**
 * @author:SunShibo
 * @date:2022-07-03 14:12
 * @feature:
 */
class MusicAdapter(private var list: ArrayList<AudioBean>) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemTitle: TextView = view.findViewById(R.id.item_music_title)
        val itemAuthor: TextView = view.findViewById(R.id.item_music_author)
        val itemOrder: TextView = view.findViewById(R.id.item_music_order)
        val itemLength: TextView = view.findViewById(R.id.item_music_length)
        val item: RelativeLayout = view.findViewById(R.id.item_music)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicAdapter.ViewHolder, position: Int) {
        val audio = list[position]
        holder.itemAuthor.text = album(audio)
        holder.itemOrder.text = (position + 1).toString()
        holder.itemTitle.text = audio.name
        holder.itemLength.text = timeCau(audio.totalTime)
        holder.item.setOnClickListener {
            AudioHelper.setIndex(position)
        }
    }

    private fun album(audio: AudioBean): CharSequence {
        return if(audio.albumInfo.isBlank() || audio.albumInfo == "unknown"){
            audio.author
        }else{
            "${audio.author} · ${audio.albumInfo}"
        }
    }

    private fun timeCau(totalTime: String): CharSequence {
        val all = totalTime.toInt()
        val minute = all / 60000
        val second = all % 60
        return if(all == 0){
            " "
        }else{
            "${minute}:${second}"
        }
    }

    override fun getItemCount(): Int =
        list.size

    fun resetDates(list: ArrayList<AudioBean>){
        this.list = list
        notifyDataSetChanged()
    }

    fun addDate(news: AudioBean){
        this.list.add(news)
        notifyItemInserted(0)
    }

}
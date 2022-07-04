package com.example.cloudmusic.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.R
import com.example.cloudmusic.application.MusicApplication
import com.example.cloudmusic.view.callback.ItemTouchMoveListener
import com.example.cloudmusic.model.News
import com.example.cloudmusic.view.ui.web.NewActivity
import com.example.lib_image_loader.app.ImageLoaderManager
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author:SunShibo
 * @date:2022-07-01 22:41
 * @feature:
 */
class NewsAdapter(private var list: ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(),
    ItemTouchMoveListener {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemTitle: TextView = view.findViewById(R.id.item_title)
        val itemAuthor: TextView = view.findViewById(R.id.item_author)
        val frame: FrameLayout = view.findViewById(R.id.frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val new = list[position]
        ImageLoaderManager.getInstance().displayImageForView(holder.itemImage, new.picUrl)
        holder.itemTitle.text = new.title
        holder.itemAuthor.text = "来源: " + new.source
        holder.frame.setOnClickListener {
            new.url?.let { it1 -> NewActivity.startLoginActivity(MusicApplication.context, it1) }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onItemMove(fromPosition: Int, endPosition: Int): Boolean {
        Collections.swap(list, fromPosition, endPosition)
        notifyItemMoved(fromPosition, endPosition)
        return true
    }

    override fun onItemRemove(position: Int): Boolean {
        list.removeAt(position)
        notifyItemRemoved(position)
        return true
    }

    fun resetDates(list: ArrayList<News>){
        this.list = list
        notifyDataSetChanged()
    }

    fun addDate(news: News){
        this.list.add(news)
        notifyItemInserted(0)
    }
}
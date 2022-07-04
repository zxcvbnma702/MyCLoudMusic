package com.example.cloudmusic.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.R
import com.example.cloudmusic.model.Time
import com.example.cloudmusic.utils.TimeUtil

/**
 * @author:SunShibo
 * @date:2022-07-03 14:12
 * @feature:
 */
class TimeAdapter(private var list: ArrayList<Time>) : RecyclerView.Adapter<TimeAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemTitle: TextView = view.findViewById(R.id.item_time_title)
        val itemAuthor: TextView = view.findViewById(R.id.item_time_author)
        val itemOrder: TextView = view.findViewById(R.id.item_time_order)
        val itemLength: TextView = view.findViewById(R.id.item_time_length)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeAdapter.ViewHolder, position: Int) {
        val time = list[position]
        holder.itemOrder.text = time.number.toString()
        holder.itemTitle.text = "Tick:" + TimeUtil(time.tick)
        holder.itemLength.text = TimeUtil(time.interval)
    }

    private fun TimeUtil(tick: Long): CharSequence {
            var tick = tick
            val timeString: String
            if (tick < 0) {
                tick = 0L
            }
            val seconds = tick / 1000
            val millsec = tick % 1000 / 10
            val mins = seconds / 60
            timeString = String.format(
                "%02d:%02d:%02d",
                mins, seconds % 60, millsec
            )
            return timeString
    }

    override fun getItemCount(): Int =
        list.size

    fun insert(list: ArrayList<Time>){
        this.list = list
        notifyDataSetChanged()
    }

    fun addDate(news: Time){
        this.list.add(0, news)
        notifyDataSetChanged()
    }

    fun clear(){
        this.list.clear()
        notifyDataSetChanged()
    }

}
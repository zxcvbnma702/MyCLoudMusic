package com.example.cloudmusic.view.ui.home.fragment

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudmusic.api.NewService
import com.example.cloudmusic.api.ServiceCreator
import com.example.cloudmusic.view.adapter.NewsAdapter
import com.example.cloudmusic.view.callback.MyItemTouchHelperCallback
import com.example.cloudmusic.databinding.FragmentDiscoveryBinding
import com.example.cloudmusic.model.DatabaseHelper
import com.example.cloudmusic.model.News
import com.example.cloudmusic.model.NewsBean
import com.example.cloudmusic.utils.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class DiscoveryFragment : Fragment() {

    private lateinit var binding: FragmentDiscoveryBinding
    private val list by lazy{
        ArrayList<News>()
    }
    private val adapter by lazy{
        NewsAdapter(list)
    }
    private val itemTouchHelper by lazy{
        ItemTouchHelper(MyItemTouchHelperCallback(adapter))
    }
    private val handler by lazy{
        Handler(Looper.getMainLooper())
    }
    private val dbHelper by lazy{
        DatabaseHelper(requireContext(), "News.db", 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getDataBase()
        requestData()
    }

    private fun initDataBase() {
        val db = dbHelper.writableDatabase
        for(value in list){
            val v = ContentValues().apply {
                put("title", value.title)
                put("description", value.description)
                put("source", value.source)
                put("picUrl", value.picUrl)
                put("url", value.url)
            }
            db.insert("News", null, v)
        }
        ToastUtil.MyToast("数据库插入成功", ToastUtil.LONG)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        binding.recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            requestData()
            handler.postDelayed({
                 binding.swipe.isRefreshing = false
            },1000)
        }
        return binding.root
    }

    private fun requestData(){
        val app = ServiceCreator.create<NewService>()
        val callback = app.get()
        callback.enqueue(object:Callback<NewsBean>{
            override fun onResponse(
                call: Call<NewsBean>,
                response: Response<NewsBean>
            ) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    list.clear()
                    list.addAll(response.body()!!.newslist!!)
                    initDataBase()
                    adapter.resetDates(list)
                }else{
                    ToastUtil.MyToast("网络数据请求失败", ToastUtil.LONG)
                    getDataBase()
                }
            }
            override fun onFailure(call: Call<NewsBean>, t: Throwable) {
                t.printStackTrace()
                getDataBase()
            }
        })
    }

    private fun getDataBase() {
        val db = dbHelper.writableDatabase
        val cursor = db.query("News", null, null, null, null, null, null)
        if(cursor.moveToNext()){
            do{
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val source = cursor.getString(cursor.getColumnIndexOrThrow("source"))
                val picUrl = cursor.getString(cursor.getColumnIndexOrThrow("picUrl"))
                val url = cursor.getString(cursor.getColumnIndexOrThrow("url"))
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                Log.e("db", "$title ~ $description  ~ $source" )
                val n = News(id, title, description, source, picUrl, url)
                list.add(n)
            }while (cursor.moveToNext())
            adapter.resetDates(list)
            ToastUtil.MyToast("数据库读取成功", ToastUtil.LONG)
        }
        cursor.close()
    }
}
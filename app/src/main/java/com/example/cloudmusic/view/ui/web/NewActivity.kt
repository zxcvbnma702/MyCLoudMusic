package com.example.cloudmusic.view.ui.web

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity
import com.example.cloudmusic.R
import com.example.cloudmusic.databinding.ActivityNewBinding
import com.example.cloudmusic.utils.ToastUtil
import com.example.cloudmusic.view.ui.login.LoginActivity

class NewActivity : FragmentActivity() {
    private lateinit var binding: ActivityNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val url = intent.getStringExtra("webUrl")
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            if (url != null) {
                loadUrl(url)
            }else{
                ToastUtil.MyToast("数据请求失败", ToastUtil.LONG)
            }
        }
    }

    companion object{
        fun startLoginActivity(context: Context, url: String){
            val intent = Intent(context, NewActivity::class.java)
            intent.putExtra("webUrl", url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
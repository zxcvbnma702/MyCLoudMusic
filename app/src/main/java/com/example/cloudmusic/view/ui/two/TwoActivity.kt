package com.example.cloudmusic.view.ui.two

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.cloudmusic.R
import com.example.cloudmusic.databinding.ActivityTwoBinding
import com.example.cloudmusic.utils.ToastUtil
import com.example.cloudmusic.view.ui.login.LoginActivity

class TwoActivity : FragmentActivity() {
    private lateinit var binding: ActivityTwoBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        val message = intent.getStringExtra("value")
        binding.count.text = message
    }

    private fun initView() {
        binding.toastShow.setOnClickListener {
            ToastUtil.MyToast("数字是:${count}", ToastUtil.SHORT)
        }
        binding.addCount.setOnClickListener {
            count++
            binding.count.text = count.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("COUNT_VALUE", count)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        count = savedInstanceState.getInt("COUNT_VALUE")
        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object{
        fun startTwoActivity(context: Context, value: String){
            val intent = Intent(context, TwoActivity::class.java)
            intent.putExtra("value", value)
            context.startActivity(intent)
        }
    }
}
package com.example.lib_audio.mediaplayer.service

import android.app.ActivityManager
import android.content.Context

/**
 * @author:SunShibo
 * @date:2022-07-03 20:39
 * @feature:
 */
object ServiceUtil {
    private var isRunning = false

    fun isServiceRunning(context: Context, serviceName:String): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val serviceInfoList = manager.getRunningServices(200)
        if(serviceInfoList.size <= 0){
            return false
        }
        for(service in serviceInfoList){
            if(service.service.className == serviceName){
                isRunning = true
                break
            }
        }
        return isRunning
    }

}
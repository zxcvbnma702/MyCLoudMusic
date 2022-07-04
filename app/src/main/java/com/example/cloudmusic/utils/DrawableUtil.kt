package com.example.cloudmusic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.example.cloudmusic.R
import java.lang.reflect.Field

/**
 * @author:SunShibo
 * @date:2022-07-01 23:25
 * @feature:
 */
object DrawableUtil {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getImageResourable(context: Context, imgName: String?): List<Drawable> {
        val imgList: MutableList<Drawable> = ArrayList()
        val resources: Resources = context.resources
        val packagename: String = context.packageName
        val fields: Array<Field> = R.drawable::class.java.getDeclaredFields()
        for (i in fields.indices) {
            fields[i].isAccessible = true
            val name: String = fields[i].name
            if (name.contains(imgName!!)) {
                val resId: Int = resources.getIdentifier(name, "drawable", packagename)
                val drawable: Drawable = resources.getDrawable(resId)
                imgList.add(drawable)
            }
        }
        return imgList
    }
}
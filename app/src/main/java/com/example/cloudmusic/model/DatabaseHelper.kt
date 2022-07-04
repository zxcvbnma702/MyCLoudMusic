package com.example.cloudmusic.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cloudmusic.utils.ToastUtil

/**
 * @author:SunShibo
 * @date:2022-07-04 16:12
 * @feature:
 */
class DatabaseHelper(context: Context, name: String, version: Int):SQLiteOpenHelper(context, name, null ,version){
    private val createTable = "create table News(" + "id integer primary key autoincrement," + "title text," + "description text," + "source text," + "picUrl text," + "url text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTable)
        ToastUtil.MyToast("数据库创建成功", ToastUtil.LONG)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
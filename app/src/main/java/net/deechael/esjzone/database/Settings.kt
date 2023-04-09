package net.deechael.esjzone.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.GsonBuilder
import net.deechael.esjzone.config.CacheConfig

class Settings(context: Context) : SQLiteOpenHelper(context, "settings.db", null, 1) {

    private val GSON = GsonBuilder().create()

    private val CREATE_GENERAL_TABLE =
        "create table if not exists `settings` ('key' text, 'value' text);"

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_GENERAL_TABLE)
        database.execSQL("insert or ignore into `settings` ('key', 'value') values ('cache', '{}');")
    }

    @SuppressLint("Range")
    fun getCache(): CacheConfig {
        val database = readableDatabase
        val cursor = database.query(
            "settings",
            arrayOf("value"),
            "key=?",
            arrayOf("cache"),
            null,
            null,
            null
        )
        val config = if (cursor.moveToNext()) {
            GSON.fromJson(cursor.getString(cursor.getColumnIndex("value")), CacheConfig::class.java)
        } else {
            CacheConfig()
        }
        cursor.close()
        return config
    }

    fun saveCache(config: CacheConfig) {
        val database = writableDatabase
        database.execSQL("insert or ignore into `settings` ('key', 'value') values ('cache', '{}');")
        val values = ContentValues()
        values.put("value", GSON.toJson(config))
        database.update("settings", values, "key=?", arrayOf("cache"))
    }

    fun hasCache(): Boolean {
        val database = readableDatabase
        val cursor = database.query(
            "settings",
            arrayOf("value"),
            "key=?",
            arrayOf("cache"),
            null,
            null,
            null
        )
        val hasNext = cursor.moveToNext()
        cursor.close()
        return hasNext
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}
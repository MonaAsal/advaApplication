package com.salamtak.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.salamtak.app.data.entities.Notification
import com.salamtak.app.data.entities.dao.NotificationsDao
import com.salamtak.app.utils.Constants


@Database(entities = arrayOf(Notification::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationsDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
//              Room.databaseBuilder(this, AppDatabase::class.java, "MyDatabase").allowMainThreadQueries().build()
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}
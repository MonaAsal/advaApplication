package com.salamtak.app.data.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.salamtak.app.data.entities.Notification

@Dao
interface NotificationsDao {
    @Query("SELECT * FROM notification")
    fun getAll(): List<Notification>

    @Query("SELECT * FROM notification WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Notification>

//    @Query("SELECT * FROM notification WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): Notification

    @Insert
    fun insertAll(vararg notification: Notification)

    @Delete
    fun delete(notification: Notification)

    @Query("DELETE FROM notification")
    fun deleteAll()

//    @Query("DROP TABLE notification")
//    fun dropTable()

}
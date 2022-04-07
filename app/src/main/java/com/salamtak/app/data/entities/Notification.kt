package com.salamtak.app.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.salamtak.app.utils.getCurrentDate
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class Notification(
//    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "titleAR") var titleAr: String?,
    @ColumnInfo(name = "onClick") var onClick: String?,
    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "body") var body: String,
    @ColumnInfo(name = "bodyAR") var bodyAr: String?) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "time")
    var time: String = getCurrentDate()
}
//{
//    fun create(
//        type: String?,
//        content: String?,
//        title: String?,
//        onClick: String?,
//        image: String?,
//        body: String?
//    ): Notification? {
//        return Notification(id, type, content, title, onClick, image, body)
//    }
//}
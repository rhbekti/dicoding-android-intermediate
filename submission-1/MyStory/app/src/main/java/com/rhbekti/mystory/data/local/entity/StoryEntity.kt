package com.rhbekti.mystory.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "stories")
@Parcelize
data class StoryEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String,

    @ColumnInfo(name = "createAt")
    var createAt: String,

    @ColumnInfo(name = "lat")
    var lat: String,

    @ColumnInfo(name = "lon")
    var lon: String

): Parcelable
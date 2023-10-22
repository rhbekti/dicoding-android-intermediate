package com.rhbekti.mystory.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rhbekti.mystory.data.local.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 1, exportSchema = false)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        private var instance: StoryRoomDatabase? = null

        fun getInstance(context: Context): StoryRoomDatabase {
            if (instance == null) {
                synchronized(StoryRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StoryRoomDatabase::class.java, "story_database"
                    ).build()
                }
            }
            return instance as StoryRoomDatabase
        }
    }
}
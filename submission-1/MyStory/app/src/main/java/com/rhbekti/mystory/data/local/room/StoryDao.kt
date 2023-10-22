package com.rhbekti.mystory.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rhbekti.mystory.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: List<StoryEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): LiveData<List<StoryEntity>>

    @Query("DELETE FROM stories")
    fun deleteAll()
}
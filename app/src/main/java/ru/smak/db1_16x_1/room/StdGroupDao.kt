package ru.smak.db1_16x_1.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StdGroupDao {
    @Insert(StdGroup::class)
    suspend fun addGroup(group: StdGroup)

    @Delete(StdGroup::class)
    suspend fun deleteGroup(group: StdGroup)

    @Query("SELECT * FROM std_group WHERE group_name = :name")
    fun getGroupByName(name: String): Flow<StdGroup>

    @Query("SELECT * FROM std_group")
    fun getAllGroups(): Flow<List<StdGroup>>
}
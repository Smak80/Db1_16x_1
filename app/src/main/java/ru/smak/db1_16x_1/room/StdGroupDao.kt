package ru.smak.db1_16x_1.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StdGroupDao {
    @Insert(StdGroup::class)
    fun addGroup(group: StdGroup)

    @Delete(StdGroup::class)
    fun deleteGroup(group: StdGroup)

    @Query("SELECT * FROM std_group WHERE group_name = :name")
    fun getGroupByName(name: String): StdGroup

    @Query("SELECT * FROM std_group")
    fun getAllGroups(): List<StdGroup>
}
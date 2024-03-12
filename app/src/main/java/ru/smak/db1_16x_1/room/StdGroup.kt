package ru.smak.db1_16x_1.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "STD_GROUP")
data class StdGroup(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "group_name")
    var groupName: String = "",

    var direction: String = "",
)

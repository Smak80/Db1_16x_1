package ru.smak.db1_16x_1.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "STUDENT", foreignKeys = [
    ForeignKey(
        entity = StdGroup::class,
        parentColumns = ["id"],
        childColumns = ["group_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.RESTRICT
    )
])
data class Student(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "full_name")
    var fullName: String = "",

    @ColumnInfo(name = "group_id")
    var groupId: Long,
)

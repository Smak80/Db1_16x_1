package ru.smak.db1_16x_1.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    [StdGroup::class, Student::class],
    version = 1,
    exportSchema = false,
)
abstract class StudentsDatabase : RoomDatabase(){
    abstract fun getStdGroupDao(): StdGroupDao
}
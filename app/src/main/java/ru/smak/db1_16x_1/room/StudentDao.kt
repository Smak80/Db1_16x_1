package ru.smak.db1_16x_1.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(Student::class)
    suspend fun addStudent(student: Student)

    @Query("SELECT * from STUDENT WHERE group_id = :groupId")
    fun getStudents(groupId: Long) : Flow<List<Student>>

}
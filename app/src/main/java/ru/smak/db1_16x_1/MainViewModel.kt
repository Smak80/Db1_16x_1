package ru.smak.db1_16x_1

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.smak.db1_16x_1.room.StdGroup
import ru.smak.db1_16x_1.room.Student
import ru.smak.db1_16x_1.room.StudentsDatabase

class MainViewModel(app: Application) : AndroidViewModel(app) {

    //private val dbHelper = DbHelper(getApplication())

    private val groupsDao by lazy{
        Room
            .databaseBuilder(getApplication(), StudentsDatabase::class.java, "STUD_DB")
            .build()
            .getStdGroupDao()
    }

    var groups by mutableStateOf(listOf<StdGroup>())
        private set

    var students by mutableStateOf(listOf<Student>())
        private set

    var newStud by mutableStateOf("")
    var selectedGroup by mutableStateOf<StdGroup?>(null)

    init{
        //groups = dbHelper.getAllGroups()
        viewModelScope.launch {
            groupsDao.addGroup(
                StdGroup(
                    groupName = "09-161",
                    direction = "Информационные системы и технологии"
                )
            )
            groupsDao.addGroup(
                StdGroup(
                    groupName = "09-162",
                    direction = "Информационные системы и технологии"
                )
            )
            groupsDao.addGroup(
                StdGroup(
                    groupName = "09-163",
                    direction = "Информационные системы и технологии"
                )
            )
            groupsDao.addGroup(
                StdGroup(
                    groupName = "09-111",
                    direction = "Прикладная математика и информатика"
                )
            )
        }

        viewModelScope.launch {
            groupsDao.getAllGroups().collect {
                groups = it
            }
        }

    }

    fun addStudent() {
        /*selectedGroup?.let {
            dbHelper.addStudent(newStud, it.groupName)
            students = dbHelper.getStudentsByGroup(it.groupName)
        }*/
    }

    fun selectGroup(group: StdGroup) {
        /*
        selectedGroup = group
        students = dbHelper.getStudentsByGroup(group.groupName)
        */
    }
}
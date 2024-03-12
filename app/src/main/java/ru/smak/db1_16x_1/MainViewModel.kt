package ru.smak.db1_16x_1

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ru.smak.db1_16x_1.database.DbHelper
import ru.smak.db1_16x_1.database.StdGroup
import ru.smak.db1_16x_1.database.Student

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val dbHelper = DbHelper(getApplication())

    var groups by mutableStateOf(listOf<StdGroup>())
        private set

    var students by mutableStateOf(listOf<Student>())
        private set

    var newStud by mutableStateOf("")
    var selectedGroup by mutableStateOf<StdGroup?>(null)

    init{
        groups = dbHelper.getAllGroups()
    }

    fun addStudent() {
        selectedGroup?.let {
            dbHelper.addStudent(newStud, it.groupName)
            students = dbHelper.getStudentsByGroup(it.groupName)
        }
    }

    fun selectGroup(group: StdGroup) {
        selectedGroup = group
        students = dbHelper.getStudentsByGroup(group.groupName)
    }
}
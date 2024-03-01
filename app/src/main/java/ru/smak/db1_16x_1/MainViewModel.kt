package ru.smak.db1_16x_1

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ru.smak.db1_16x_1.database.StdGroup

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val dbHelper = DbHelper(getApplication())

    var groups by mutableStateOf(listOf<StdGroup>())
        private set

    init{
        groups = dbHelper.getAllGroups()
    }
}
package ru.smak.db1_16x_1

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.smak.db1_16x_1.room.StdGroup
import ru.smak.db1_16x_1.room.Student
import ru.smak.db1_16x_1.room.StudentsDatabase

class MainViewModel(app: Application) : AndroidViewModel(app) {

    /**
     * Представляет объект базы данных для работы с приложении
     */
    private val db = Room
            .databaseBuilder(getApplication(), StudentsDatabase::class.java, "STUD_DB")
            .build()

    /**
     * Объект доступа к данным в таблице студенческих групп
     */
    private val groupsDao = db.getStdGroupDao()

    /**
     * Объект доступа к данным в таблице студентов
     */
    private val studentsDao = db.getStudentDao()

    /**
     * Список студенческих групп, отображаемый в активности
     */
    var groups by mutableStateOf(listOf<StdGroup>())
        private set

    /**
     * Список студентов, отображаемый в активности
     */
    var students by mutableStateOf(listOf<Student>())

    /**
     * Имя нового студента, введенное пользователем приложения
     */
    var newStud by mutableStateOf("")

    /**
     * Группа, которая была выбрана пользователем приложения.
     * Для выбранной группы будет отображаться список студентов, а также
     * выполняться добавление новых студентов
     */
    var selectedGroup by mutableStateOf<StdGroup?>(null)

    /**
     * Работа по получению списков студентов для заданной группы
     * Работа начинает выполняться при выборе группы и прерывается при
     * смене выбранной группы.
     */
    private var studentCollector: Job? = null

    /**
     * Поток, содержащий списки студентов для выбранной группы
     * При обращении к свойству будет возвращаться новый поток
     * с текущим номером выбранной группы. Поэтому при смене потока лучше
     * выполнять прерывание работы корутины, получающей данные из предыдущего
     * потока
     */
    private val studentsFlow: Flow<List<Student>>
        get() {
            return studentsDao.getStudents(selectedGroup?.id ?: -1)
        }

    /*
     * Инициализация модели представления. Создание списка из 4х групп
     * Для исключения повторного добавления одинаковых групп,
     * им присваиваются конкретные идентификаторы.
     * При повторном запуске приложения такие группы не получится добавить
     * в базу и операция будет прервана из-за исключения.
     * Обработка исключения без реакции не даст приложению "упасть"
     */
    init{
        //groups = dbHelper.getAllGroups()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                groupsDao.addGroup(
                    StdGroup(
                        id = 1,
                        groupName = "09-161",
                        direction = "Информационные системы и технологии"
                    )
                )
                groupsDao.addGroup(
                    StdGroup(
                        id = 2,
                        groupName = "09-162",
                        direction = "Информационные системы и технологии"
                    )
                )
                groupsDao.addGroup(
                    StdGroup(
                        id = 3,
                        groupName = "09-163",
                        direction = "Информационные системы и технологии"
                    )
                )
                groupsDao.addGroup(
                    StdGroup(
                        id = 4,
                        groupName = "09-111",
                        direction = "Прикладная математика и информатика"
                    )
                )
            } catch (_: Throwable){}
        }

        // Запуск процесса получения списка групп
        viewModelScope.launch {
            groupsDao.getAllGroups().collect {
                groups = it
            }
        }

    }

    /**
     * Добавление нового студента в базу данных
     */
    fun addStudent() {
        selectedGroup?.let{
            if (newStud.isNotBlank()) {
                Student(id = 0, newStud, it.id).also {
                    viewModelScope.launch(Dispatchers.IO) {
                        studentsDao.addStudent(it)
                    }
                }
            }
        }
        /*selectedGroup?.let {
            dbHelper.addStudent(newStud, it.groupName)
            students = dbHelper.getStudentsByGroup(it.groupName)
        }*/
    }

    /**
     * Выбор группы из списка групп
     * @param group Выбранная студенческая группа
     */
    fun selectGroup(group: StdGroup) {
        // Устанавливаем группу group в качестве выбранной
        selectedGroup = group
        // Запускаем процесс получения списка студентов выбранной группы
        viewModelScope.launch {
            try {
                // Пробуем остановить задачу, выполняющую получения списков
                // студентов ранее выбранной группы
                studentCollector?.cancelAndJoin()
            } catch (_:Throwable){}
            // Запускаем новую задачу по получению списков студентов для
            // группы, которая выбрана на данный момент. Сохраняем информацию
            // о задаче в переменной studentCollecor типа Job для
            // обеспечения возможности прерывания работы при последующей
            // смене группы
            studentCollector = launch {
                studentsFlow.collect{
                    students = it
                }
            }
        }
        /*
        students = dbHelper.getStudentsByGroup(group.groupName)
        */
    }

}
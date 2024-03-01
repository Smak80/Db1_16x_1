package ru.smak.db1_16x_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ru.smak.db1_16x_1.database.StdGroup

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    companion object{
        const val DB_NAME = "STUDENTS_DB"
        const val TBL_STUD = "STUD"
        const val TBL_GROUP = "STUD_GRP"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let{ db ->
            db.beginTransaction()
            try{
                try {
                    db.execSQL(
                        "CREATE TABLE $TBL_GROUP(" +
                                "id INTEGER PRIMARY KEY, " +
                                "group_name TEXT NOT NULL, " +
                                "direction TEXT NOT NULL " +
                                ")"
                    )
                    db.execSQL(
                        "CREATE TABLE $TBL_STUD(" +
                                "id INTEGER PRIMARY KEY, " +
                                "fullname TEXT NOT NULL, " +
                                "group_id INTEGER NOT NULL, " +
                                "FOREIGN KEY (group_id) REFERENCES $TBL_GROUP(id)" +
                                ")"
                    )
                    db.setTransactionSuccessful()
                }
                finally {
                    db.endTransaction()
                }
                addGroup("09-161", "Информационные системы и технологии", db)
                addGroup("09-162", "Информационные системы и технологии", db)
                addGroup("09-163", "Информационные системы и технологии", db)
                addGroup("09-111", "Прикладная математика и информатика", db)
            } catch (e: Throwable){
                Log.e("DB_ERROR", e.message.toString())
            }
        }
    }

    fun addGroup(groupName: String, direction: String, database: SQLiteDatabase? = null){
        with(database ?: writableDatabase){
            beginTransaction()
            val values = ContentValues()
            values.put("group_name", groupName)
            values.put("direction", direction)
            try{
                insert(TBL_GROUP, "", values)
                setTransactionSuccessful()
            } catch (_: Throwable){
            } finally {
                endTransaction()
            }
        }
    }

    fun getGroupId(groupName: String): Int{
        with(readableDatabase){
            beginTransaction()
            var res: Int
            try{
                query(
                    TBL_GROUP,
                    arrayOf("id"),
                    "group_name = ?",
                    arrayOf(groupName),
                    null,
                    null,
                    null
                ).apply {
                    try {
                        if (moveToNext()) {
                            res = getInt(0)
                            setTransactionSuccessful()
                            return res
                        } else {
                            throw Exception("Не найден идентификатор группы")
                        }
                    } finally {
                        close()
                    }
                }
            } finally {
                endTransaction()
            }
        }
    }

    fun addStudent(fullName: String, groupName: String){
        with(writableDatabase){
            beginTransaction()
            try{
                val values = ContentValues()
                values.put("fullname", fullName)
                values.put("group_id", getGroupId(groupName))
                insert(TBL_STUD, "", values)
                setTransactionSuccessful()
            } catch (_: Throwable){

            } finally {
                endTransaction()
            }
        }
    }

    fun getAllGroups(): List<StdGroup> {
        val groups = mutableListOf<StdGroup>()
        with (readableDatabase){
            beginTransaction()
            try{
                query(TBL_GROUP,
                    arrayOf("id", "group_name", "direction"),
                    null,
                    null,
                    null,
                    null,
                    null
                ).apply {
                    while (moveToNext()){
                        groups.add(StdGroup(
                            getLong(0),
                            getString(1),
                            getString(2)
                        ))
                    }
                    close()
                }
                setTransactionSuccessful()
                return groups
            }catch (_: Throwable){
                groups.clear()
                return groups
            }
            finally {
                endTransaction()
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}
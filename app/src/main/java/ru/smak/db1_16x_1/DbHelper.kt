package ru.smak.db1_16x_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    companion object{
        const val DB_NAME = "STUDENTS_DB"
        const val TBL1_NAME = "STUD"
        const val TBL2_NAME = "STUD_GRP"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let{ db ->
            db.beginTransaction()
            try{
                db.execSQL("CREATE TABLE $TBL2_NAME(" +
                        "id INTEGER PRIMARY KEY, " +
                        "group_name TEXT NOT NULL, " +
                        "direction TEXT NOT NULL " +
                        ")")
                db.execSQL("CREATE TABLE $TBL1_NAME(" +
                        "id INTEGER PRIMARY KEY, " +
                        "fullname TEXT NOT NULL, " +
                        "group_id INTEGER NOT NULL, " +
                        "FOREIGN KEY (group_id) REFERENCES $TBL2_NAME(id)" +
                        ")")
                db.setTransactionSuccessful()
            } catch (_: Throwable){

            } finally {
                db.endTransaction()
            }
        }
    }

    fun addGroup(groupName: String, direction: String){
        with(writableDatabase){
            beginTransaction()
            val values = ContentValues()
            values.put("group_name", groupName)
            values.put("direction", direction)
            try{
                insert(TBL2_NAME, "", values)
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
                    TBL2_NAME,
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
                insert(TBL1_NAME, "", values)
                setTransactionSuccessful()
            } catch (_: Throwable){
            } finally {
                endTransaction()
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}
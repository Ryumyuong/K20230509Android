package com.example.test17_crud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DatabaseHelper(context: Context?) : SQLiteOpenHelper
// version이 변경이 되면 아래에 있는 onUpgrade 함수가 재호출 됨
    (context, DATABASE_NAME, null, 1) {
    //물리 경로 data -> data -> 패키지명-> database에 있음
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PHONE TEXT, ADDRESS TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // 데이터베이스 추가하기 insert
    fun insertData(name: String?, phone: String?, address: String?): Boolean {
        // db사용시 쓰기, 수정, 삭제는 writableDatabase를 사용
        val db = this.writableDatabase
        // execSQL 대신에 ContentValues()를 이용하면 SQL문장없이 매서드 값만 인자로 넣어서 쉽게 insert 가능
        val contentValues = ContentValues()
        contentValues.put(COL_2, name)
        contentValues.put(COL_3, phone)
        contentValues.put(COL_4, address)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return if (result == -1L) false else true
    }

    //데이터베이스 항목 읽어오기 Read
    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $TABLE_NAME", null)
        }

    // 데이터베이스 삭제하기
    fun deleteData(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "ID = ? ", arrayOf(id))
    }

    //데이터베이스 수정하기
    fun updateData(id: String, name: String?, phone: String?, address: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, name)
        contentValues.put(COL_3, phone)
        contentValues.put(COL_4, address)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    companion object {
        const val DATABASE_NAME = "STUDENT.db" // 데이터베이스 명
        const val TABLE_NAME = "student_table" // 테이블 명

        // 테이블 항목
        const val COL_1 = "ID"
        const val COL_2 = "Name"
        const val COL_3 = "Phone"
        const val COL_4 = "Address"
    }
}
package com.example.test17_crud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.test17_crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var myDB: DatabaseHelper? = null
    lateinit var binding: ActivityMainBinding

    var editTextName: EditText? = null
    var editTextPhone: EditText? = null
    var editTextAddress: EditText? = null
    var editTextID: EditText? = null
    var buttonInsert: Button? = null
    var buttonView: Button? = null
    var buttonUpdate: Button? = null
    var buttonDelete: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
        myDB = DatabaseHelper(this)
        editTextName = binding.editTextName
        editTextPhone = binding.editTextPhone
        editTextAddress = binding.editTextAddress
        editTextID = binding.editTextID
        buttonInsert = binding.buttonInsert
        buttonView = binding.buttonView
        buttonUpdate = binding.buttonUpdate
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonDelete = binding.buttonDelete
        AddData()
        viewAll()
        UpdateData()
        DeleteData()
    }

    //데이터베이스 추가하기
    fun AddData() {
        buttonInsert!!.setOnClickListener {
            val isInserted = myDB!!.insertData(
                editTextName!!.text.toString(),
                editTextPhone!!.text.toString(),
                editTextAddress!!.text.toString()
            )
            if (isInserted == true) Toast.makeText(this@MainActivity, "데이터추가 성공", Toast.LENGTH_LONG)
                .show() else Toast.makeText(this@MainActivity, "데이터추가 실패", Toast.LENGTH_LONG).show()
        }
    }

    // 데이터베이스 읽어오기
    fun viewAll() {
        buttonView!!.setOnClickListener(View.OnClickListener {
            val res = myDB!!.allData
            if (res.count == 0) {
                ShowMessage("실패", "데이터를 찾을 수 없습니다.")
                return@OnClickListener
            }
            val buffer = StringBuffer()
            while (res.moveToNext()) {
                buffer.append(
                    """
    ID: ${res.getString(0)}
    
    """.trimIndent()
                )
                buffer.append(
                    """
    이름: ${res.getString(1)}
    
    """.trimIndent()
                )
                buffer.append(
                    """
    전화번호: ${res.getString(2)}
    
    """.trimIndent()
                )
                buffer.append(
                    """
    주소: ${res.getString(3)}
    
    
    """.trimIndent()
                )
            }
            ShowMessage("데이터", buffer.toString())
        })
    }

    //데이터베이스 수정하기
    fun UpdateData() {
        buttonUpdate!!.setOnClickListener {
            val isUpdated = myDB!!.updateData(
                editTextID!!.text.toString(),
                editTextName!!.text.toString(),
                editTextPhone!!.text.toString(),
                editTextAddress!!.text.toString()
            )
            if (isUpdated == true) Toast.makeText(this@MainActivity, "데이터 수정 성공", Toast.LENGTH_LONG)
                .show() else Toast.makeText(this@MainActivity, "데이터 수정 실패", Toast.LENGTH_LONG)
                .show()
        }
    }

    // 데이터베이스 삭제하기
    fun DeleteData() {
        buttonDelete!!.setOnClickListener {
            val deleteRows = myDB!!.deleteData(editTextID!!.text.toString())
            if (deleteRows > 0) Toast.makeText(this@MainActivity, "데이터 삭제 성공", Toast.LENGTH_LONG)
                .show() else Toast.makeText(this@MainActivity, "데이터 삭제 실패", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun ShowMessage(title: String?, Message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }
}
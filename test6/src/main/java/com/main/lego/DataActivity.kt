package com.main.lego

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.main.lego.databinding.ActivityDataBinding
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class DataActivity : AppCompatActivity() {
    lateinit var binding: ActivityDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSelectFile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

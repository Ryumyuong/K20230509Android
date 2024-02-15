package com.main.lego

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readExcelFile()
    }

    private fun readExcelFile() {
        try {
            val inputStream: InputStream = assets.open("kang.xlsx")
            val workbook: Workbook = XSSFWorkbook(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0)

            for (row in sheet) {
                for (cell in row) {
                    // 셀의 값을 가져와서 처리
                    val cellValue: String = getCellValue(cell)
                    // 여기에서 셀 값에 대한 처리를 수행
                    binding.textView.text = cellValue
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCellValue(cell: Cell): String {
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> ""
        }
    }
}
package com.main.lego

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var cellValue: String? = ""

    companion object {
        const val READ_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSelectFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            // 파일을 선택하는 화면으로 이동
            startActivityForResult(intent, READ_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 선택된 파일의 Uri를 가져옴
            val uri = resultData?.data

            // Uri를 통해 엑셀 파일 읽기
            readExcelFile(uri)
        }
    }

    private fun readExcelFile(uri: Uri?) {
        try {
            // 엑셀 파일을 열기 위해 InputStream을 가져옵니다.
            val inputStream: InputStream? = uri?.let { contentResolver.openInputStream(it) }
            if (inputStream != null) {
                val workbook: Workbook = XSSFWorkbook(inputStream)
                val sheet: Sheet = workbook.getSheetAt(0)

                var rowIndex = 1 // 첫 번째 행의 인덱스 (0부터 시작)
                var columnIndex = 1 // 첫 번째 열의 인덱스 (0부터 시작)

                var row = sheet.getRow(rowIndex)
                var cell = row?.getCell(columnIndex)

                cellValue = getCellValue(cell)

                if (cellValue != null) {
                    // 셀의 값을 출력
                    binding.name.text = "이름 : $cellValue"
                }

                rowIndex = 5
                columnIndex = 2

                row = sheet.getRow(rowIndex)
                cell = row?.getCell(columnIndex)

                cellValue = getCellValue(cell)

                if (cellValue != null) {
                    if(cellValue=="*미성년*"){
                        val words = cellValue!!.split(" ")
                        val lastWord = words.lastOrNull()?.lastOrNull()
                        cellValue = lastWord.toString()
                    } else {
                        cellValue = "0"
                    }
                    // 셀의 값을 출력
                    binding.tip2.text = "특이 : 미성년 자녀 ${cellValue}명"
                }

                rowIndex = 10
                columnIndex = 2

                row = sheet.getRow(rowIndex)
                cell = row?.getCell(columnIndex)

                cellValue = getCellValue(cell)

                if (cellValue != null) {
                        val words = cellValue!!.split(" ")
                        val lastWord = words.lastOrNull()
                        cellValue = lastWord.toString()
                    // 셀의 값을 출력
                    binding.earn.text = "소득 : $cellValue"
                }

                // 파일을 사용한 후에는 InputStream을 닫아줍니다.
                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCellValue(cell: Cell?): String {
        return when (cell?.cellType) {
            CELL_TYPE_STRING -> cell.stringCellValue
            CELL_TYPE_NUMERIC -> cell.numericCellValue.toString()
            CELL_TYPE_BOOLEAN -> ""
            else -> ""
        }
    }
}

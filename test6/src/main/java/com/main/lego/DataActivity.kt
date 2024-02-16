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
    private var value: String = ""

    companion object {
        const val READ_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
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

//                val rowIndex = 1 첫 번째 행의 인덱스 (0부터 시작)
//                val columnIndex = 1 첫 번째 열의 인덱스 (0부터 시작)

                for(i in 1..40) {
                    for (j in 1..4) {
                        val row = sheet.getRow(i)
                        val cell = row?.getCell(j)
                        val cellValue = getCellValue(cell)

                        // 이름
                        if ((i == 1) and (j == 1)) {
                            binding.name.text = "이름 : $cellValue"
                        }

                        // 미성년자 수
                        if ((i == 5) and (j == 2)) {
                            if(cellValue=="*미성년*"){
                                val words = cellValue!!.split(" ")
                                val lastWord = words.lastOrNull()?.lastOrNull()
                                value = lastWord.toString()
                            } else {
                                value = "0"
                            }

                            binding.tip2.text = "특이 : 미성년 자녀 ${value}명"

                            // 단기
                            if(value == "0") {
                                value = "195만 3~5년납"
                                binding.shortT.text = "단기 : $value"
                            } else if(value == "1") {
                                value = "110만 3~5년납"
                                binding.shortT.text = "단기 : $value"
                            } else if(value == "2") {
                                value = "45만 3~5년납"
                                binding.shortT.text = "단기 : $value"
                            } else if(value == "3") {
                                value = "85만 3~5년납"
                                binding.shortT.text = "단기 : $value"
                            } else if(value == "4") {
                                value = "-70만 3~5년납"
                                binding.shortT.text = "단기 : $value"
                            }
                        }

                        if((i == 10) and (j == 2)) {
                            // 소득
                            val words = cellValue!!.split(" ")
                            val lastWord = words.lastOrNull()
                            value = lastWord.toString()
                            val modifiedValue = value!!.replace("만", "")
                            binding.earn.text = "소득 : $modifiedValue"

                            // 장기
                            val word = binding.tip2.text!!.split(" ")
                            val last = word.lastOrNull()
                            val modified = last!!.replace("명", "")

                            if(modified=="0") {
                                value = "135"
                            } else if(modified == "1") {
                                value = "220"
                            } else if(modified == "2") {
                                value = "285"
                            } else if(modified == "3") {
                                value = "245"
                            } else if(modified == "4") {
                                value = "400"
                            }

                            val money = ((modifiedValue.toInt()?.minus(value.toInt())?.minus(50))?.div(3)?.times(2)).toString()

                            binding.longT.text = "장기 : ${money}만 00년납"
                        }
                        // 60세 이상 부모 여부
                        if((i == 18) and (j == 2)) {
                            if((cellValue == "모") and (cellValue == "부")) {
                                value = "특이 : 60세 이상 부모 O"
                            } else {
                                value = "특이 : 60세 이상 부모 X"
                            }
                            binding.tip3.text = value
                        }

                    }
                }

                binding.tip1.text = "6개월 내 채무 00%"

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

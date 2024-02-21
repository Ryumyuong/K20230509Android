package com.main.lego

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var value: String = ""

    companion object {
        const val READ_REQUEST_CODE = 42
        const val CREATE_FILE_REQUEST_CODE = 43
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.down.setOnClickListener {
            copyExcelFileToInternalStorage()
            downloadExcelFile()
        }

        binding.buttonSelectFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            // 파일을 선택하는 화면으로 이동
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        binding.download.setOnClickListener {
            createFile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        when (requestCode) {
            READ_REQUEST_CODE -> {
                // 선택된 파일의 Uri를 가져옴
                val uri = resultData?.data

                // Uri를 통해 엑셀 파일 읽기
                readExcelFile(uri)
            }
            CREATE_FILE_REQUEST_CODE -> {
                // 선택된 파일의 Uri를 가져옴
                val uri = resultData?.data

                // 결과값을 파일로 저장하고 공유
                if (uri != null) {
                    saveResultToFileAndShare(uri)
                }
            }
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
                        if ((i == 3) and (j == 2)) {
                            binding.name.text = "이름 : $cellValue"
                        }

                        // 미성년자 수
                        if ((i == 7) and (j == 2)) {
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

                        if((i == 12) and (j == 2)) {
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

                binding.tip1.text = "특이 : 6개월 내 채무 00%"

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

    private fun saveResultToFileAndShare(uri: Uri) {
        try {
            val outputStream = contentResolver.openOutputStream(uri)
            outputStream?.use { stream ->
                val fileContents = buildResultText()
                stream.write(fileContents.toByteArray())
            }

            showToast("파일로 저장되었습니다.")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("저장에 실패하였습니다.")
        }
    }

    private fun buildResultText(): String {

        val name = binding.name.text.toString()
        val tip1 = binding.tip1.text.toString()
        val tip2 = binding.tip2.text.toString()
        val tip3 = binding.tip3.text.toString()
        val shortT = binding.shortT.text.toString()
        val longT = binding.longT.text.toString()
        val earn = binding.earn.text.toString()

        return "$name\n\n$tip1\n$tip2\n$tip3\n\n$shortT\n$longT\n\n$earn"
    }

    private fun showToast(message: String) {
        // 간단한 Toast 메시지 표시
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/plain"  // 파일 타입에 따라 변경
        intent.putExtra(Intent.EXTRA_TITLE, "result.txt")

        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }

    private fun copyExcelFileToInternalStorage() {
        val excelFileName = "your_excel_file.xlsx"

        val internalStoragePath = ContextWrapper(applicationContext).filesDir
        val internalFilePath = File(internalStoragePath, excelFileName)

        try {
            val inputStream: InputStream = assets.open(excelFileName)
            val outputStream = FileOutputStream(internalFilePath)
            val buffer = ByteArray(1024)
            var length: Int
            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun downloadExcelFile() {
        val internalFilePath = "https://download.blog.naver.com/close/35a0299a8bd1d10d20c7ae93a34f3548eaba41a8f6/nxhokvYc1BU1N3AQJhnqKsqYe3E5Ja3EnbiNGEkv-BEUd2kuk052x6F3e82CmuHfAAAMLQceyTbLBvi1ESpZgWFMvFO83ilC5JBjUnhMNCg/inquiry.xlsx"

        val request = DownloadManager.Request(Uri.parse(internalFilePath))
            .setTitle("Excel 다운로드")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_file.xlsx")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    //

}
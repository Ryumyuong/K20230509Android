package com.main.lego

import android.app.Activity
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.service.autofill.Validators.or
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.main.lego.MainActivity.Companion.REQUEST_PICK_IMAGE
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var value: String = ""

    companion object {
        const val READ_REQUEST_CODE = 42
        const val CREATE_FILE_REQUEST_CODE = 43
        const val REQUEST_PICK_IMAGE = 44
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

    @RequiresApi(Build.VERSION_CODES.O)
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

            REQUEST_PICK_IMAGE -> {
                val selectedImageUri: Uri? = resultData?.data
                val selectedImageBitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                processImage(selectedImageBitmap)
            }
        }

    }

    //이미지 스캔
    @RequiresApi(Build.VERSION_CODES.O)
    private fun processTextResult(visionText: Text) {

        val textBlocks = visionText.textBlocks
        if (textBlocks.isEmpty()) {
            showToast("No text found in the image.")
            return
        }

        val currentDate = LocalDate.now()

        val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        val recognizedText1 = StringBuilder()
        val recognizedText2 = StringBuilder()


        var blockText = textBlocks[0].text
        val listText = blockText.split("\n")
        var rows = 0

        val twoArray = ArrayList<ArrayList<Any>>()

        for (text in listText) {

            val date = text.replace(",", ".")
            val fourText = date.take(4)

            try {
                if(fourText.contains(".")) {
                    twoArray[rows].add(date)
                    rows++

                } else {
                    if (fourText.toInt() > 1000) {
                        val array = ArrayList<Any>()
                        val ddate = date.replace(" ",".")
                        array.add(ddate)
                        twoArray.add(array)
                    }
                }

            } catch (e:Exception){

            }
        }
        var value = 0
        var bValue = 0

        for(i in 0 until twoArray.size) {
            val date = twoArray[i].get(0).toString()
            val beforeDate = LocalDate.parse(date, dateFormat)
            val plusMonth = beforeDate.plusMonths(6)
            val clean = twoArray[i].get(1).toString().replace(".","")
            if (currentDate >= plusMonth) {
                // 6개월 이상
                if(clean.toInt().mod(10) >= 6) {
                    value += clean.toInt().div(10) + 1
                } else {
                    value += clean.toInt().div(10)
                }


            } else {
                // 6개월 이하
                if(clean.toInt().mod(10) >= 6) {
                    bValue += clean.toInt().div(10) + 1
                } else {
                    bValue += clean.toInt().div(10)
                }
            }

        }
        var cost = 0
        var bCost = 0.0
        cost = value + bValue
        binding.test1.text = cost.toString()
        binding.test2.text = bValue.toString()

        binding.cost.setOnClickListener {
            if(binding.bill.text.toString()  != "") {
                    bCost = bValue/(cost+binding.card.text.toString().toInt()).toDouble()*100
                    val bcost = round(bCost * 10) / 10.0
                    binding.co.text = "6개월 내 채무 $bcost%"
                    binding.test1.text = (binding.test1.text.toString().toInt() - binding.bill.text.toString().toDouble().toInt() + binding.card.text.toString().toInt()).toString()
            } else{
                showToast("담보 값을 입력하세요")
            }


        }

        showToast("진단이 완료되었습니다.")

    }


    private fun convertToNumber(input: String): Double? {
        return try {
            input.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient()

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Process the recognized text
                processTextResult(visionText)
            }
            .addOnFailureListener { e ->
                // Handle text recognition failure
                showToast("Text recognition failed: ${e.message}")
            }
    }

    // 엑셀파일
    private fun readExcelFile(uri: Uri?) {
        try {
            // 엑셀 파일을 열기 위해 InputStream을 가져옵니다.
            val inputStream: InputStream? = uri?.let { contentResolver.openInputStream(it) }
            if (inputStream != null) {
                val workbook: Workbook = XSSFWorkbook(inputStream)
                val sheet: Sheet = workbook.getSheetAt(0)
                var card = 0

//                val rowIndex = 1 첫 번째 행의 인덱스 (0부터 시작)
//                val columnIndex = 1 첫 번째 열의 인덱스 (0부터 시작)
                val recognizedText = StringBuilder()
                val recognizedText2 = StringBuilder()
                val recognizedText3 = StringBuilder()
                for(j in 1..9) {
                    for (i in 1..30) {
                        val row = sheet.getRow(i)
                        val cell = row?.getCell(j)
                        val cellValue = getCellValue(cell)

                        // 이름
                        if ((i == 3) and (j == 2)) {
                            binding.name.text = "[이름] $cellValue"
                        }

                        // 미성년자 수
                        if ((i == 6) and (j == 2)) {
                            if(cellValue.contains("미성년")){
                                binding.baby.text = "결혼 여부 : $cellValue"
                            }


                        }

                        if((i == 9) and (j == 2)) {
                                val words = cellValue.split(" ")
                                for(a in words.indices) {

                                    if(words[a].contains("보증금")) {
                                        val cell = "본인재산 : $cellValue"
                                        recognizedText.append(cell).append("\n")
                                        binding.money.text = recognizedText
                                    }

                                    if(words[a].contains("담보")) {
                                        binding.houseb.text = "주택담보 : $cellValue"
                                    }
                                }
                            if(binding.money.text == "") {
                                recognizedText.append("본인재산 : 0").append("\n")
                            }

                            if(binding.houseb.text == "") {
                                binding.houseb.text = "주택담보 : 0"
                            }
                        }

                        if((i == 10) and (j == 2)) {
                                val words = cellValue.split(" ")
                                for(a in words.indices) {
                                    if(words[a].contains("보증금")) {
                                        val cell = "배우자재산 : $cellValue"
                                        recognizedText.append(cell).append("\n")
                                        binding.money.text = recognizedText

                                    }
                                }
                        }

                        if((i == 11) and (j == 2)) {
                            val words = cellValue.split(" ")
                            if(cellValue.contains("시세")) {
                                val cell = "본인차시세 : $cellValue"
                                recognizedText2.append(cell).append("\n")
                                binding.car.text = recognizedText2
                            }
                            if(cellValue.contains("담보")) {
                                for(a in words.indices) {
                                    if(words[a].contains("담보")) {
                                        val cell = "본인차담보 : $cellValue"
                                        recognizedText3.append(cell).append("\n")
                                        binding.carb.text = recognizedText3
                                    }
                                }
                            }

                            if(binding.car.text == "") {
                                binding.car.text = "차량시세 : 0"
                            }

                            if(binding.carb.text == "") {
                                binding.carb.text = "차량담보 : 0"
                            }
                        }
                        if((i == 12 ) and (j == 2)) {
                            val words = cellValue.split(" ")
                            if(cellValue.contains("시세")) {
                                for(a in words.indices) {
                                    if(words[a] == "시세") {
                                        val cell = "배우자차시세 : $cellValue"
                                        recognizedText2.append(cell).append("\n")
                                        binding.car.text = "차량 : $recognizedText2"
                                    }
                                }
                            }
                            if(cellValue.contains("담보")) {
                                for(a in words.indices) {
                                    if(words[a].contains("담보")) {
                                        val cell = "배우자차담보 : $cellValue"
                                        recognizedText3.append(cell).append("\n")
                                        binding.carb.text = "차량담보 : $recognizedText3"
                                    }
                                }
                            }
                        }

                        if((i == 13) and (j == 2)) {
                            val words = cellValue.split(" ")
                            for(a in words.indices) {
                                if(words[a] == "세전") {
                                    binding.ymoney.text = "연봉: $cellValue"
                                }

                                if(words[a] == "월") {
                                    binding.mmoney.text = "월급 : $cellValue"
                                }

                            }

                            if(binding.ymoney.text == "") {
                                binding.ymoney.text = "연봉 : 0"
                            }
                            if(binding.mmoney.text == "") {
                                binding.mmoney.text = "월급 : 0"
                            }
                        }
                        // 60세 이상 부모 여부
                        if((i == 21) and (j == 2)) {

                            value = if(cellValue.contains("60세 이상")) {
                                "60세 이상 부모 : O"
                            } else {
                                "60세 이상 부모 : X"
                            }
                            binding.parent.text = value
                        }

                        if((i == 25) and (j == 2)) {
                            if(cellValue != "") {
                                card = cellValue.replace("만", "").toDouble().toInt()
                            }
                        }

                        if((i == 26) and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()
                            }
                        }
                        if((i == 27) and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()
                            }
                        }
                        if((i == 28) and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()
                            }
                        }
                        if((i == 29) and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()
                            }
                        }
                        if((i == 30) and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()

                            }
                        }

                        if((i == 19) and (j == 8)) {
                            if(cellValue != "") {
                                binding.bill.setText(cellValue)
                            } else {
                                binding.bill.setText(binding.carb.text)
                            }
                        }

                    }
                }
                binding.card.text = card.toString()

//                val co = binding.test1.text.toString().toInt() + card - binding.bill.text.toString().toDouble().toInt()
                // 파일을 사용한 후에는 InputStream을 닫아줍니다.
                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.copy.setOnClickListener {
            // 클립보드 매니저 가져오기
            val clipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // 복사할 텍스트 가져오기
            val textToCopy = buildResultText()

            // 클립 데이터 생성
            val clipData = ClipData.newPlainText("resultText", textToCopy)

            // 클립보드에 데이터 설정
            clipboardManager.setPrimaryClip(clipData)

            // 사용자에게 알림
            showToast("텍스트가 클립보드에 복사되었습니다.")
        }

        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE)
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
        val check = binding.check.text.toString()
        val name = binding.name.text.toString()
        val tip1 = binding.baby.text.toString()
        val tip2 = binding.parent.text.toString()
        val tip3 = binding.money.text.toString()
        val shortT = binding.houseb.text.toString()
        val longT = binding.car.text.toString()
        val earn = binding.carb.text.toString()
        val ymoney = binding.ymoney.text.toString()
        val mmoney = binding.mmoney.text.toString()
        val test1 = binding.test1.text
        val test2 = binding.test2.text
        val card = binding.card.text

        return "$check\n$name\n\n$tip1\n$tip2\n$tip3\n\n$shortT\n$longT\n\n$earn\n$ymoney\n$mmoney\n$test1\n$test2\n$card"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
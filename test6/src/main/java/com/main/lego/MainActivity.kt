package com.main.lego

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.lang.Math.round
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var card = 0
    private var value = 0
    private var bValue = 0
    private var baby = "0"


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

        binding.mhouse.setText("0")
        binding.bhouse.setText("0")
        binding.mcar.setText("0")
        binding.bcar.setText("0")
        binding.bill.setText("0")
        binding.moneyy.setText("0")
        binding.moneym.setText("0")


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

                if (selectedImageUri != null) {
                    val selectedImageBitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    binding.imageView.setImageBitmap(selectedImageBitmap)

                    val layoutParams = binding.imageView.layoutParams
                    layoutParams.width = selectedImageBitmap.width * 2
                    layoutParams.height = selectedImageBitmap.height * 2
                    binding.imageView.layoutParams = layoutParams
                }

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


        var blockText = textBlocks[0].text
        val listText = blockText.split("\n")
        var rows = 0

        val twoArray = ArrayList<ArrayList<Any>>()
        val recognizedText = StringBuilder()

        try {
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

            for(i in 0 until twoArray.size) {
                val date = twoArray[i].get(0).toString()
                val beforeDate = LocalDate.parse(date, dateFormat)
                val plusMonth = beforeDate.plusMonths(6)
                val clean = twoArray[i].get(1).toString().replace(".","")
                if (currentDate >= plusMonth) {
                    // 6개월 이상
                    if(clean.toInt().mod(10) >= 5) {
                        value += clean.toInt().div(10) + 1
                    } else {
                        value += clean.toInt().div(10)
                    }


                } else {
                    // 6개월 이하
                    if(clean.toInt().mod(10) >= 5) {
                        bValue += clean.toInt().div(10) + 1
                    } else {
                        bValue += clean.toInt().div(10)
                    }
                }

            }
        } catch(e:Exception) {
            showToast("이미지를 인식할 수 없습니다.")
        }


        var cost = 0
        var bCost = 0.0
        var acost = 0
        cost = value + bValue + card
        binding.test1.text = cost.toString()
        binding.test2.text = bValue.toString()

        binding.cost.setOnClickListener {
            binding.dat.text = "[대상] ${cost - binding.bhouse.text.toString().toInt() - binding.bcar.text.toString().toInt() - binding.bill.text.toString().toInt()}"
            binding.je.text = "[재산] ${binding.mhouse.text}"
            if(binding.bhouse.text.toString()  != "") {
                    bCost = bValue/cost.toDouble()*100
                    val bcost = round(bCost * 10) / 10.0
                    binding.co.text = "[특이] 6개월 내 채무 $bcost%"
            } else {

            }

            if(baby == "0") {
                baby = "135"
            } else if(baby == "1"){
                baby = "220"
            } else if(baby == "2") {
                baby = "285"
            } else if(baby == "3") {
                baby = "245"
            } else if(baby == "4") {
                baby = "400"
            }

            if(binding.moneyy.text.toString().toInt()*0.8/12 > binding.moneym.text.toString().toInt()) {
                if(binding.parent.text == "[특이] 60세 이상 부모 : O") {
                    binding.test2.text = "[장기] ${(binding.moneyy.text.toString().toInt()/12 - baby.toInt()-50)*2/3}만"
                    acost = (binding.moneyy.text.toString().toInt()/12 - baby.toInt()-50)*2/3
                    binding.test1.text = "[단기] ${binding.moneyy.text.toString().toInt()*0.8/12-baby.toInt()}만 3~5년납"
                    binding.card.text = "[소득] ${binding.moneyy.text.toString().toInt()*0.8/12}"
                } else {
                    binding.test2.text = "[장기] ${(binding.moneyy.text.toString().toInt()/12 - baby.toInt())*2/3}만"
                    acost = (binding.moneyy.text.toString().toInt()/12 - baby.toInt())*2/3
                    binding.test1.text = "[단기] ${binding.moneyy.text.toString().toInt()*0.8/12-baby.toInt()}만 3~5년납"
                    binding.card.text = "[소득] ${binding.moneyy.text.toString().toInt()*0.8/12}"
                }
            } else {
                if(binding.parent.text == "[특이] 60세 이상 부모 : O") {
                    binding.test2.text = "[장기] ${(binding.moneym.text.toString().toInt() - baby.toInt()-50)*2/3}만"
                    acost = (binding.moneym.text.toString().toInt() - baby.toInt()-50)*2/3
                    binding.test1.text = "[단기] ${binding.moneym.text.toString().toInt()-baby.toInt()}만 3~5년납"
                    binding.card.text = "[소득] ${binding.moneym.text.toString().toInt()}"
                } else {
                    binding.test2.text = "[장기] ${(binding.moneym.text.toString().toInt() - baby.toInt())*2/3}만"
                    acost = (binding.moneym.text.toString().toInt() - baby.toInt())*2/3
                    binding.test1.text = "[단기] ${binding.moneym.text.toString().toInt()-baby.toInt()}만 3~5년납"
                    binding.card.text = "[소득] ${binding.moneym.text.toString().toInt()}"
                }
            }

            binding.test2.text = " ${binding.test2.text} ${((cost-binding.bhouse.text.toString().toInt()-binding.bcar.text.toString().toInt()-binding.bill.text.toString().toInt() )/ acost /12)}년납"




        }

        showToast("진단이 완료되었습니다.")

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun readExcelFile(uri: Uri?) {
        try {
            // 엑셀 파일을 열기 위해 InputStream을 가져옵니다.
            val inputStream: InputStream? = uri?.let { contentResolver.openInputStream(it) }
            if (inputStream != null) {
                val workbook: Workbook = XSSFWorkbook(inputStream)
                val sheet: Sheet = workbook.getSheetAt(0)


//                val rowIndex = 1 첫 번째 행의 인덱스 (0부터 시작)
//                val columnIndex = 1 첫 번째 열의 인덱스 (0부터 시작)
                val recognizedText = StringBuilder()
                val recognizedText2 = StringBuilder()
                val recognizedText3 = StringBuilder()
                val recognizedText4 = StringBuilder()
                val recognizedText5 = StringBuilder()
                val recognizedText6 = StringBuilder()
                val currentDate = LocalDate.now()
                val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                var dmoney = 0
                var hmoney = 0
                var omoney = 0


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
                        if ((i == 7) and (j == 2)) {
                            if(cellValue.contains("미성년")){
                                val cell = cellValue.replace("미성년","").trim().replace("명","")
                                binding.baby.text = "[특이] 미성년 자녀 ${cell}명"
                                baby = cell
                            } else {
                                binding.baby.text = "[특이] 미성년 자녀 0명"
                            }


                        }

                        if((i == 8) and (j == 2)) {
                            binding.local.text = "지역 : $cellValue"
                        }

                        if((i == 10) and (j == 2)) {
                            binding.money.text = "본인재산 : $cellValue"
                        }

                        if((i == 11) and (j == 2)) {
                           binding.moneyn.text = "배우자재산 : $cellValue"
                        }

                        if((i == 12) and (j == 2)) {
                            binding.car.text = "본인차량 : $cellValue"
                        }
                        if((i == 13 ) and (j == 2)) {
                            binding.carb.text = "배우자 차량 : $cellValue"
                        }

                        if((i == 14) and (j == 2)) {
                            binding.ymoney.text = "본인소득 : $cellValue"
                        }

                        if((i == 15) and (j == 2)) {
                            binding.mmoney.text = "배우자소득 : $cellValue"
                        }




                        // 60세 이상 부모 여부
                        if((i == 22) and (j == 2)) {
                            if(cellValue.contains("60세 이상")) {
                                binding.parent.text = "[특이] 60세 이상 부모 : O"
                            } else {
                                binding.parent.text = "[특이] 60세 이상 부모 : X"
                            }
                        }

                        if(((i >= 27) and (i<=32))and (j == 2)) {
                            if(cellValue != "") {
                                card = cellValue.replace("만", "").toDouble().toInt()
                            }
                        }

                        if(((i>=5) and (i <= 14)) and (j==6)) {
                            if(cellValue !="") {
                                recognizedText.append(cellValue).append("\n")
                                binding.bname.text = recognizedText
                            }

                        }

                        if(((i>=5) and (i <= 14)) and (j==7)) {
                            if(cellValue !="") {
                                recognizedText2.append(cellValue).append("\n")
                                binding.bco.text = recognizedText2

                                val cells = row?.getCell(j+1)
                                val cellValues = getCellValue(cells)
                                if(cellValue.contains("차량담보") || cellValue.contains("차담보")) {
                                    dmoney += cellValues.replace(",","").toDouble().toInt()
                                    binding.bcar.setText(dmoney.toString())
                                } else if(cellValue.contains("주택담보")){
                                    hmoney += cellValues.replace(",","").toDouble().toInt()
                                    binding.bhouse.setText(hmoney.toString())
                                } else if(cellValue.contains("신차")) {
                                    omoney += cellValues.replace(",","").toDouble().toInt()
                                    binding.bill.setText(omoney.toString())
                                }

                            }
                        }

                        if(((i>=5) and (i <= 14)) and (j==8)) {
                            if(cellValue !="") {
                                recognizedText3.append(cellValue.replace(",","").toDouble().toInt()).append("\n")
                                binding.bmon.text = recognizedText3
                            }
                        }

                        if((i>=25) and (i<=37) and (j==6)) {
                            if(cellValue !="") {
                                recognizedText4.append(cellValue).append("\n")
                                binding.pname.text = recognizedText4
                            }
                        }

                        if((i>=25) and (i<=37) and (j==7)) {
                            if(cellValue !="") {
                                recognizedText5.append(cellValue).append("\n")
                                binding.pco.text = recognizedText5
                                val beforeDate = LocalDate.parse(cellValue, dateFormat)
                                val plusMonth = beforeDate.plusMonths(6)
                                val cells = row?.getCell(j+1)
                                val cellValues = getCellValue(cells)

                                if(currentDate >= plusMonth) {
                                    value += cellValues.toInt()
                                } else {
                                    bValue += cellValues.toInt()
                                }
                            }
                        }

                        if((i>=25) and (i<=37) and (j==8)) {
                            if(cellValue !="") {
                                recognizedText6.append(cellValue).append("\n")
                                binding.pmon.text = recognizedText6
                            }
                        }

                    }
                }
                binding.card.text = card.toString()

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
        val name = binding.name.text.toString()
        val tip1 = binding.baby.text.toString()
        val tip2 = binding.parent.text.toString()
        val tip3 = binding.money.text.toString()
        val longT = binding.car.text.toString()
        val earn = binding.carb.text.toString()
        val ymoney = binding.ymoney.text.toString()
        val mmoney = binding.mmoney.text.toString()
        val test1 = binding.test1.text
        val test2 = binding.test2.text
        val card = binding.card.text
        val co = binding.co.text
        val dat = binding.dat.text
        val je = binding.je.text

        return "$name\n\n$card\n$dat\n$je\n\n$co\n$tip1\n$tip2\n[특이]\n[특이]\n[특이]\n\n$test1\n$test2\n\n[진단]\n[진단]"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
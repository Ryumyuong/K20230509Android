package com.main.lego

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.main.lego.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.util.IOUtils
import org.apache.poi.xssf.usermodel.XSSFPicture
import org.apache.poi.xssf.usermodel.XSSFSheet
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Math.round
import java.nio.file.Files
import java.nio.file.Paths
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var card = 0
    private var value = 0
    private var bValue = 0
    private var baby = "0"
    private var cost = 0
    private var bCost = 0.0
    private var acost = 0
    private var korea = "X"


    companion object {
        const val READ_REQUEST_CODE = 42
        const val CREATE_FILE_REQUEST_CODE = 43
        val recognizedText7 = StringBuilder()
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

        binding.cost.setOnClickListener {
            try {
                val dam =
                    binding.bhouse.text.toString().toDouble().toInt() + binding.bcar.text.toString()
                        .toDouble().toInt() + binding.bill.text.toString().toDouble().toInt()
                val cos =
                    binding.mhouse.text.toString().toDouble().toInt()
                val dat = cost - dam
                binding.dat.text = "[대상] $dat"
                binding.je.text = "[재산] $cos"
                if (binding.bhouse.text.toString() != "") {
                    bCost = bValue / cost.toDouble() * 100
                    val bcost = round(bCost * 10) / 10.0
                    binding.co.text = "[특이] 6개월 내 채무 $bcost%"
                }

                if (baby == "0") {
                    baby = "135"
                } else if (baby == "1") {
                    baby = "220"
                } else if (baby == "2") {
                    baby = "285"
                } else if (baby == "3") {
                    baby = "245"
                } else if (baby == "4") {
                    baby = "400"
                }

                if (binding.moneyy.text.toString().toDouble()
                        .toInt() * 0.8 / 12 > binding.moneym.text.toString().toDouble().toInt()
                ) {
                    if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                        acost = ((binding.moneyy.text.toString().toDouble()
                            .toInt() * 0.8 / 12 - baby.toInt() - 50) * 2 / 3).toInt()
                        binding.test2.text = "[장기] ${acost}만"
                        binding.test1.text = "[단기] ${
                            (binding.moneyy.text.toString().toDouble()
                                .toInt() * 0.8 / 12 - baby.toInt()).toInt()
                        }만 3~5년납"
                        binding.card.text = "[소득] ${
                            (binding.moneyy.text.toString().toDouble().toInt() * 0.8 / 12).toInt()
                        }"
                    } else {
                        acost = ((binding.moneyy.text.toString().toDouble()
                            .toInt() * 0.8 / 12 - baby.toInt()) * 2 / 3).toInt()
                        binding.test2.text = "[장기] ${acost}만"
                        binding.test1.text = "[단기] ${
                            (binding.moneyy.text.toString().toDouble()
                                .toInt() * 0.8 / 12 - baby.toInt()).toInt()
                        }만 3~5년납"
                        binding.card.text = "[소득] ${
                            (binding.moneyy.text.toString().toDouble().toInt() * 0.8 / 12).toInt()
                        }"
                    }
                } else {
                    if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                        acost = ((binding.moneym.text.toString().toDouble()
                            .toInt() - baby.toInt() - 50) * 2 / 3).toDouble().toInt()
                        binding.test2.text = "[장기] ${acost}만"
                        binding.test1.text = "[단기] ${
                            (binding.moneym.text.toString().toDouble()
                                .toInt() - baby.toInt()).toDouble().toInt()
                        }만 3~5년납"
                        binding.card.text =
                            "[소득] ${binding.moneym.text.toString().toDouble().toInt()}"
                    } else {
                        acost = ((binding.moneym.text.toString().toDouble()
                            .toInt() - baby.toInt()) * 2 / 3).toDouble().toInt()
                        binding.test2.text = "[장기] ${acost}만"
                        binding.test1.text = "[단기] ${
                            (binding.moneym.text.toString().toDouble()
                                .toInt() - baby.toInt()).toDouble().toInt()
                        }만 3~5년납"
                        binding.card.text =
                            "[소득] ${binding.moneym.text.toString().toDouble().toInt()}"
                    }
                }

                val year = round(dat / acost / 12.0)

                binding.test2.text = "${binding.test2.text} ${year}년납"
                val local = binding.local.text


                if (binding.check.text.equals("연체기록 : 없음")) {
                    binding.testing.text = "[진단] 신회워"
                    binding.bae2.text = "[특이]"
                    if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                        binding.testing.text = "[진단] 신유워"
                    }
                    if (local.contains("서울")) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 13200) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 16500) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 19800) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }

                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 11600) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 14500) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 17400) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6800) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 8500) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 10200) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6000) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 7500) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 9000) {
                                binding.testing.text = "[진단] 신유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    }

                } else if (binding.check.text == "연체기록 : 1개월 ~ 2개월") {
                    binding.testing.text = "[진단] 프회워"
                    if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                        binding.testing.text = "[진단] 프유워"
                    }

                    if (local.contains("서울")) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 13200) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 16500) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 19800) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 11600) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 14500) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 17400) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6800) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 8500) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 10200) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6000) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 7500) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 9000) {
                                binding.testing.text = "[진단] 프유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    }
                } else if (binding.check.text == "연체기록 : 3개월 ~ 6개월") {
                    binding.testing.text = "[진단] 회워"

                    if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                        binding.testing.text = "[진단] 유워"
                    }

                    if (local.contains("서울")) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 13200) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 16500) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 19800) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }

                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 11600) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 14500) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 17400) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6800) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 8500) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 10200) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    } else {
                        if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                            if ((cos - dam) > 6000) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("3")) {
                            if ((cos - dam) > 7500) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        } else if (binding.group.text.contains("4")) {
                            if ((cos - dam) > 9000) {
                                binding.testing.text = "[진단] 유워"
                                binding.bae2.text = "[특이] 재산초과"
                            }
                        }
                    }
                } else if (binding.check.text == "연체기록 : 6개월 이상") {
                    binding.testing.text = "[진단] 단순워크"
                } else {
                    binding.testing.text = "[진단]"

                }

            } catch(e:Exception){
                showToast("${e.message}")
            }

        }

        binding.mhouse.setText("0")
        binding.bhouse.setText("0")
        binding.mcar.setText("0")
        binding.bcar.setText("0")
        binding.bill.setText("0")
        binding.moneyy.setText("0")
        binding.moneym.setText("0")
        binding.total.setText("0")
        binding.btotal.setText("0")
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

        }

    }

    //이미지 스캔
    @RequiresApi(Build.VERSION_CODES.O)
    private fun processTextResult(visionText: Text) {
        try {
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
                if(!recognizedText7.contains(date)) {
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
            }

            cost = value + bValue + card
            binding.total.setText(cost.toString())
            binding.btotal.setText(bValue.toString())
            showToast("채무량이 등록되었습니다.")
        } catch(e:Exception) {
            showToast("채무량을 수정해주세요.")
        }

        val dam =
            binding.bhouse.text.toString().toDouble().toInt() + binding.bcar.text.toString()
                .toDouble().toInt() + binding.bill.text.toString().toDouble().toInt()
        val cos =
            binding.mhouse.text.toString().toDouble().toInt()
        val dat = cost - dam
        binding.dat.text = "[대상] $dat"
        binding.je.text = "[재산] $cos"
        if (binding.bhouse.text.toString() != "") {
            bCost = bValue / cost.toDouble() * 100
            val bcost = round(bCost * 10) / 10.0
            binding.co.text = "[특이] 6개월 내 채무 $bcost%"
        }

        val year = round(dat / acost / 12.0)

        binding.test2.text = "${binding.test2.text} ${year}년납"
        val local = binding.local.text


        if (binding.check.text.equals("연체기록 : 없음")) {
            binding.testing.text = "[진단] 신회워"
            binding.bae2.text = "[특이]"
            if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                binding.testing.text = "[진단] 신유워"
            }
            if (local.contains("서울")) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 13200) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 16500) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 19800) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }

            } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                    "김포"
                )
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 11600) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 14500) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 17400) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                    "이천"
                ) || local.contains("평택")
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6800) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 8500) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 10200) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6000) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 7500) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 9000) {
                        binding.testing.text = "[진단] 신유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            }

        } else if (binding.check.text == "연체기록 : 1개월 ~ 2개월") {
            binding.testing.text = "[진단] 프회워"
            if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                binding.testing.text = "[진단] 프유워"
            }

            if (local.contains("서울")) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 13200) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 16500) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 19800) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                    "김포"
                )
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 11600) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 14500) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 17400) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                    "이천"
                ) || local.contains("평택")
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6800) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 8500) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 10200) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6000) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 7500) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 9000) {
                        binding.testing.text = "[진단] 프유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            }
        } else if (binding.check.text == "연체기록 : 3개월 ~ 6개월") {
            binding.testing.text = "[진단] 회워"

            if (binding.work.text.equals("직업 : 무직") || binding.work.text.equals("직업 : 사업자") || binding.bae.text.equals("[특이] 배우자 모르게") || korea == "O") {
                binding.testing.text = "[진단] 유워"
            }

            if (local.contains("서울")) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 13200) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 16500) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 19800) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }

            } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                    "김포"
                )
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 11600) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 14500) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 17400) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                    "이천"
                ) || local.contains("평택")
            ) {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6800) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 8500) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 10200) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            } else {
                if (binding.group.text.contains("1") || binding.group.text.contains("2")) {
                    if ((cos - dam) > 6000) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("3")) {
                    if ((cos - dam) > 7500) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                } else if (binding.group.text.contains("4")) {
                    if ((cos - dam) > 9000) {
                        binding.testing.text = "[진단] 유워"
                        binding.bae2.text = "[특이] 재산초과"
                    }
                }
            }
        } else if (binding.check.text == "연체기록 : 6개월 이상") {
            binding.testing.text = "[진단] 단순워크"
        } else {
            binding.testing.text = "[진단]"

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
                val recognizedText8 = StringBuilder()
                val currentDate = LocalDate.now()
                val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                val dateformat = DateTimeFormatter.ofPattern("yyyy.M.d")
                var dmoney = 0
                var hmoney = 0
                var omoney = 0
                value = 0
                bValue = 0
                card = 0
                cost = 0

                for(j in 1..9) {
                    for (i in 1..40) {
                        val row = sheet.getRow(i)
                        val cell = row?.getCell(j)
                        val cellValue = getCellValue(cell)

                        if(((i >= 34) and (i<=43))and (j == 1)) {
                            if(cellValue != "") {
                                recognizedText6.append(cellValue).append("\n")
                                binding.test.text = recognizedText6
                            }
                        }

                        // 이름
                        if ((i == 3) and (j == 2)) {
                            binding.name.text = "[이름] $cellValue"
                        }

                        if ((i == 7) and (j == 2)) {
                            binding.group.text = "가구원 수 : ${cellValue.toDouble().toInt()}"
                        }

                        // 미성년자 수
                        if ((i == 8) and (j == 2)) {
                            if(cellValue!=""){
                                val cell = cellValue.replace("명","").replace("인","").trim().toDouble().toInt().toString()
                                binding.baby.text = "[특이] 미성년 자녀 ${cell}명"
                                baby = cell
                                if (baby == "0") {
                                    baby = "135"
                                } else if (baby == "1") {
                                    baby = "220"
                                } else if (baby == "2") {
                                    baby = "285"
                                } else if (baby == "3") {
                                    baby = "245"
                                } else if (baby == "4") {
                                    baby = "400"
                                }
                            } else {
                                binding.baby.text = "[특이] 미성년 자녀 0명"
                                baby="135"
                            }
                        }

                        if((i == 10) and (j == 2)) {
                            binding.local.text = "지역 : $cellValue"
                        }

                        if((i == 11) and (j == 2)) {
                            binding.work.text = "직업 : $cellValue"
                        }

                        if((i == 12) and (j == 2)) {
                            if(cellValue!="") {
                                val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()
                                binding.money.text = "$ccellValue"
                            } else {
                                binding.money.text = "0"
                            }
                        }

                        if((i == 13) and (j == 2)) {
                            if(cellValue !="") {
                                val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()
                                binding.moneyn.text = "배우자재산 : $ccellValue"
                            } else {
                                binding.moneyn.text = "배우자재산 : 0"
                            }
                        }
                        if((i == 14) and (j == 2)) {
                            if(cellValue != "") {
                                val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()
                                val money = binding.money.text.toString().toInt()
                                val moneyn = binding.moneyn.text.toString().replace("배우자재산 : ","").toInt()

                                binding.money.text = "[재산] ${(money + moneyn/2) - ccellValue}"
                                binding.mhouse.setText("${(money + moneyn/2)}")
                                binding.bhouse.setText("$ccellValue")
                            } else {
                                val money = binding.money.text.toString().toInt()
                                val moneyn = binding.moneyn.text.toString().replace("배우자재산 : ","").toInt()

                                binding.money.text = "[재산] ${(money + moneyn/2)}"
                                binding.mhouse.setText("${(money + moneyn/2)}")
                                binding.bhouse.setText("0")
                            }

                        }

                        if((i == 15) and (j == 2)) {
                            try {
                                if(cellValue !="") {
                                    val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()

                                    binding.car.text = "본인차량 : $ccellValue"
                                    binding.mcar.setText("$ccellValue")

                                } else {
                                    binding.car.text = "본인차량 : 0"
                                }
                            }catch (e:Exception) {
                                binding.car.text = "본인차량 : $cellValue"
                            }

                        }
                        if((i == 16) and (j == 2)) {
                            try {
                                if(cellValue != "") {
                                    val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()
                                    binding.carb.text = "배우자 차량 : $ccellValue"
                                    val car = binding.mcar.text.toString().toInt()
                                    binding.mcar.setText("${car + ccellValue/2}")
                                } else {
                                    binding.carb.text = "배우자 차량 : 0"
                                }
                            } catch(e:Exception) {
                                binding.carb.text = "배우자 차량 : $cellValue"
                            }
                        }

                        if((i == 17) and (j == 2)) {
                            if(cellValue != "" ) {
                                val ccellValue = cellValue.replace("만","").replace("억","").trim().toDouble().toInt()
                                binding.bcar.setText("$ccellValue")
                            }
                        }

                        if((i == 18) and (j == 2)) {
                            if(cellValue != "") {
                                val ccellValue = cellValue.replace("만","").replace("억","").replace("월","").trim().toDouble().toInt()
                                binding.moneym.setText("$ccellValue")
                            }
                        }

                        if((i == 19) and (j == 2)) {
                            if(cellValue != "") {
                                val ccellValue = cellValue.replace("만","").replace("억","").replace("연","").trim().toDouble().toInt()
                                    binding.moneyy.setText("${ccellValue*0.8/12}")

                            }
                        }

                        if((i == 20) and (j == 2)) {
                            if(cellValue != "") {
                                val ccellValue = cellValue.replace("만","").replace("억","").replace("월","").trim().toDouble().toInt()
                                binding.mmoney.text = "배우자소득 : $ccellValue"
                            }
                        }

                        if((i == 21) and (j == 2)) {
                            if(cellValue != "") {
                                val ccellValue = cellValue.replace("만","").replace("억","").replace("연","").trim().toDouble().toInt()

                                if(binding.mmoney.text.toString().replace("배우자소득 : ","").toInt() < ccellValue*0.8/12) {
                                    binding.mmoney.text = "배우자소득 : ${ccellValue*0.8/12}"
                                }
                            }
                        }

                        if((i == 24) and (j == 2)) {
                            binding.before.text = "채무조정이력 : $cellValue"
                            if(cellValue.contains("2020") || cellValue.contains("2021") ||cellValue.contains("2022") ||cellValue.contains("2023") ||cellValue.contains("2024")) {
                                binding.before.text = "[특이] 5년내 면책이력"
                                korea = "O"
                            } else {
                                binding.before.text = "채무조정이력 : $cellValue"
                            }
                        }

                        if((i == 25) and (j == 2)) {
                            binding.use.text = "대출사용처 : $cellValue"
                        }

                        if((i == 26) and (j == 2)) {
                            binding.check.text = "연체기록 : $cellValue"
                        }

                        if((i == 27) and (j == 2)) {
                            binding.unable.text = "장애여부 : $cellValue"
                        }

                        if((i == 28) and (j == 2)) {
                            if(cellValue !="") {
                                binding.some.text = "특이사항 : $cellValue"
                            } else {
                                binding.some.text = "특이사항 : 없음"
                            }

                        }

                        if((i == 34) and (j == 5)) {
                            if (cell != null) {
                                val drawing = (cell.sheet as XSSFSheet).createDrawingPatriarch()
                                val picture = drawing.shapes.firstOrNull { it is XSSFPicture } as XSSFPicture?
                                if (picture != null) {
                                    val pictureData = picture.pictureData
                                    if (pictureData != null) {
                                        val ext = pictureData.mimeType.split("/").last()
                                        val imageData = pictureData.data
                                        val uniqueFileName = "${System.currentTimeMillis()}.$ext"
                                        var imagePath = "/storage/emulated/0/Pictures/$uniqueFileName" // 이미지를 저장할 경로
                                        var outputFile = File(imagePath)

                                        try {
                                            val fileOutputStream = FileOutputStream(outputFile)
                                            fileOutputStream.write(imageData)
                                            fileOutputStream.close()
                                            val file = File(imagePath)
                                            if (file.exists()) {
                                                val bitmap = BitmapFactory.decodeFile(imagePath)
                                                if (bitmap != null) {
                                                    val layoutParams = binding.imageview.layoutParams
                                                    layoutParams.width = bitmap.width * 3
                                                    layoutParams.height = bitmap.height * 3
                                                    binding.imageview.layoutParams = layoutParams
                                                    val imageView = binding.imageview
                                                    if (bitmap != null) {
                                                        imageView.setImageBitmap(bitmap)
                                                        processImage(bitmap)
                                                    } else {
                                                        // 비트맵이 null인 경우에 대한 처리 (예: 디폴트 이미지 설정 등)
                                                    }
                                                } else {
                                                    // 파일을 로드하여 비트맵으로 변환하는 데 실패한 경우에 대한 처리
                                                }
                                            } else {
                                                // 파일이 존재하지 않는 경우에 대한 처리
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            }
                        }



                        // 60세 이상 부모 여부
                        if((i == 29) and (j == 4)) {
                            val boovalue = cell?.booleanCellValue
                            if(boovalue == true) {
                                binding.parent.text = "[특이] 60세 이상 부모 O"
                            } else {
                                binding.parent.text = "[특이] 60세 이상 부모 X"
                            }
                            val moneym = binding.moneym.text.toString().toDouble().toInt()
                            val moneyy = (binding.moneyy.text.toString().toDouble()*0.8/12).toInt()

                            if (moneyy > moneym) {
                                if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                                    acost = ((moneyy - baby.toInt() - 50) * 2 / 3).toDouble().toInt()
                                    binding.test2.text = "[장기] ${acost}만"
                                    binding.test1.text = "[단기] ${moneyy - baby.toInt()}만 3~5년납"
                                    binding.card.text = "[소득] $moneyy"
                                } else {
                                    acost = ((moneyy - baby.toInt()) * 2 / 3).toDouble().toInt()
                                    binding.test2.text = "[장기] ${acost}만"
                                    binding.test1.text = "[단기] ${moneyy - baby.toInt()}만 3~5년납"
                                    binding.card.text = "[소득] $moneyy"
                                }
                            } else {
                                if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                                    acost = ((moneym - baby.toInt() - 50) * 2 / 3).toDouble().toInt()
                                    binding.test2.text = "[장기] ${acost}만"
                                    binding.test1.text = "[단기] ${(moneym - baby.toInt()).toDouble().toInt()}만 3~5년납"
                                    binding.card.text ="[소득] $moneym"
                                } else {
                                    acost = ((moneym - baby.toInt()) * 2 / 3).toDouble().toInt()
                                    binding.test2.text = "[장기] ${acost}만"
                                    binding.test1.text = "[단기] ${(moneym - baby.toInt()).toDouble().toInt()}만 3~5년납"
                                    binding.card.text ="[소득] $moneym"
                                }
                            }
                        }

                        if((i == 30) and (j == 4)) {
                            val boovalue = cell?.booleanCellValue
                            if(boovalue == true) {
                                binding.bae.text = "[특이] 배우자 모르게"
                            } else {
                                binding.bae.text = "[특이]"
                            }
                        }

                        if(((i >= 34) and (i<=43))and (j == 2)) {
                            if(cellValue != "") {
                                card += cellValue.replace("만", "").toDouble().toInt()
                                recognizedText.append(cellValue.replace(",","").replace("만","").toDouble().toInt()).append("만\n")
                                binding.ccost.text = recognizedText
                            }
                        }

                        if(((i>=3) and (i <= 30)) and (j==5)) {
                            if(cellValue !="") {
                                val cells = row?.getCell(j+1)
                                val cellValues = getCellValue(cells)
                                if(cellValue.contains("한국주택") && (cellValues.contains("담보") || cellValues.contains("신차"))) {
                                    korea = "O"
                                } else {
                                    for(a in 3..22) {
                                        val rowd = sheet.getRow(a)
                                        val celld = rowd?.getCell(j)
                                        val cellValued = getCellValue(celld)
                                        val cellds = rowd?.getCell(j+1)
                                        val cellValueds = getCellValue(cellds)
                                        if((cellValue == cellValued) &&
                                            ((cellValues.contains("신용") && (cellValueds.contains("담보") || cellValueds.contains("신차")))
                                                    || (cellValueds.contains("신용") && (cellValues.contains("담보") || cellValues.contains("신차"))))) {
                                            korea = "O"
                                        }
                                    }
                                }
                                recognizedText2.append(cellValue).append("\n")
                                binding.bname.text = recognizedText2
                            }

                        }

                        if(((i>=3) and (i <= 30)) and (j==6)) {
                            if(cellValue !="") {
                                recognizedText3.append(cellValue).append("\n")
                                binding.bco.text = recognizedText3

                                val cells = row?.getCell(j+2)
                                val cellValues = getCellValue(cells)
                                if(cellValue.contains("차량담보") || cellValue.contains("차담보")) {
                                    dmoney += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                    binding.bcar.setText(dmoney.toString())
                                } else if(cellValue.contains("주택담보") || cellValue.contains("집담보")){
                                    hmoney += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                    binding.bhouse.setText(hmoney.toString())
                                } else if(cellValue.contains("신차")) {
                                    omoney += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                    binding.bill.setText(omoney.toString())
                                }

                            }
                        }

                        if(((i>=3) and (i<=30)) and (j==7)) {
                            if(cellValue !="") {
                                recognizedText4.append(cellValue).append("일\n")
                                binding.pco.text = recognizedText4
                                try {
                                    val beforeDate = LocalDate.parse(cellValue, dateFormat)
                                    recognizedText7.append(cellValue).append("\n")
                                    val plusMonth = beforeDate.plusMonths(6)
                                    val cells = row?.getCell(j+1)
                                    val cellValues = getCellValue(cells)
                                        if(currentDate >= plusMonth) {
                                            value += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                        } else {
                                            bValue += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                        }


                                } catch(e:Exception) {
                                    val beforeDate = LocalDate.parse(cellValue, dateformat)
                                    val newBeforeDate = LocalDate.parse(beforeDate.format(dateFormat),dateFormat)
                                    recognizedText7.append(beforeDate.format(dateFormat)).append("\n")
                                    val plusMonth = newBeforeDate.plusMonths(6)
                                    val cells = row?.getCell(j+1)
                                    val cellValues = getCellValue(cells)

                                        if(currentDate >= plusMonth) {
                                            value += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                        } else {
                                            bValue += cellValues.replace(",","").replace("만","").toDouble().toInt()
                                        }
                                }


                            }
                        }

                        if(((i>=3) and (i <= 30)) and (j==8)) {
                            if(cellValue !="") {
                                recognizedText5.append(cellValue.replace(",","").replace("만","").toDouble().toInt()).append("만\n")
                                binding.bmon.text = recognizedText5
                            }
                        }

                    }
                }

                cost = value + bValue + card
                binding.total.setText(cost.toString())
                binding.btotal.setText(bValue.toString())

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
        val tip3 = binding.bae.text.toString()
        var longT = binding.before.text.toString()
        if(!binding.before.text.contains("특이")) {
           longT = "[특이]"
        }

        val bae2 = binding.bae2.text.toString()
        val ymoney = binding.ymoney.text.toString()
        val testing = binding.testing.text.toString()
        val test1 = binding.test1.text
        val test2 = binding.test2.text
        val card = binding.card.text
        val co = binding.co.text
        val dat = binding.dat.text
        val je = binding.je.text

        return "$name\n\n$card\n$dat\n$je\n\n$co\n$tip1\n$tip2\n$tip3\n$bae2\n$longT\n\n$test1\n$test2\n\n$testing\n[진단]"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
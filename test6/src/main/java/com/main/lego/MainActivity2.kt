package com.main.lego

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.main.lego.databinding.ActivityMain2Binding
import kr.dogfoot.hwplib.`object`.HWPFile
import kr.dogfoot.hwplib.reader.HWPReader
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor
import org.apache.poi.ss.formula.functions.Index
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import java.lang.Math.round
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    private val PICK_FILE_REQUEST_CODE = 1
    private var card = 0
    private var value = 0
    private var bValue = 0
    private var cost = 0
    private var bCost = 0.0
    private var acost = 0
    private var baby = 0
    private var je = 0
    private var bje = 0
    private var group = 0
    private var babys = 0
    private var work = ""
    private var check = ""
    private var local = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSelectFile.setOnClickListener {
            openFilePicker()

            binding.mhouse.setText("0")
            binding.bhouse.setText("0")
            binding.mcar.setText("0")
            binding.bcar.setText("0")
            binding.bill.setText("0")
            binding.moneyy.setText("0")
            binding.moneym.setText("0")
            binding.total.setText("0")
            binding.btotal.setText("0")

            binding.card.text = "0"
            binding.bae.text = ""
            binding.bae2.text = ""
            binding.use.text = ""
            binding.testing.text = ""
            binding.before.text = ""
            card = 0
            value = 0
            bValue = 0
            cost = 0
            bCost = 0.0
            acost = 0
            baby = 0
            je = 0
            bje = 0
            check = "없음"
            local = ""
            binding.testimage.setImageBitmap(null)
            binding.testimage2.setImageBitmap(null)
        }

        binding.cost.setOnClickListener {
            val dam =
                binding.bhouse.text.toString().toDouble().toInt() + binding.bcar.text.toString()
                    .toDouble().toInt() + binding.bill.text.toString().toDouble().toInt()
            val hdam = binding.bhouse.text.toString().toDouble().toInt()
            val dat = binding.total.text.toString().toDouble().toInt() - dam
            val year = round(dat / acost / 12.0)
            binding.money.text = "[재산] $je-$bje"
            val cos =
                binding.mhouse.text.toString().toDouble().toInt() + binding.mcar.text.toString().toDouble().toInt()
            binding.dat.text = "[대상] $dat"
            if (binding.bhouse.text.toString() != "") {
                bCost = binding.btotal.text.toString().toDouble() / binding.total.text.toString().toDouble() * 100
                val bcost = round(bCost * 10) / 10.0
                binding.co.text = "[특이] 6개월 내 채무 $bcost%"
            }

            acost = ((binding.moneyy.text.toString().toDouble()
                .toInt() * 0.8 / 12 - baby - 50) * 2 / 3).toInt()
            if(acost <0 || year < 0 || year >= 8) {
                binding.test2.text = "[장기] ${dat/96}만 8년납"
            } else {
                binding.test2.text = "[장기] ${acost}만 ${year}년납"
            }


            if(acost <0 || year < 0 || year >= 8) {
                binding.test2.text = "[장기] ${dat/96}만 8년납"
            } else {
                binding.test2.text = "[장기] ${acost}만 ${year}년납"
            }

            val aacost = binding.test2.text.split(" ")
            var jangcos = aacost[1].replace("만","").toInt()

            if(binding.half.text.contains("과반수")) {

                if(jangcos.mod(10) < 5) {
                    jangcos -= jangcos.mod(10) - 5
                    binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
                } else {
                    jangcos -= jangcos.mod(10) - 10
                    binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
                }
            } else {
                if(jangcos.mod(10) < 5) {
                    jangcos -= jangcos.mod(10)
                    binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
                } else {
                    jangcos -= jangcos.mod(10) - 5
                    binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
                }

            }


            if (check == "없음") {
                binding.testing.text = "[진단] 신회워"
                if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                    binding.testing.text = "[진단] 신유워"
                }

                if(cos-hdam > dat) {
                    binding.test1.text = "[단기] 재산초과"
                    if (local.contains("서울")) {
                        if (group == 1|| group == 2) {
                            if ((cos - hdam - 13200) < dat) {
                                binding.testing.text = "[진단] 신유워"

                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam - 16500) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam - 19800) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }

                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam - 11600) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam - 14500) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam - 17400) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam - 6800) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 8500 < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 10200 < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam - 6000) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam - 7500) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam - 9000) < dat) {
                                binding.testing.text = "[진단] 신유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    }
                }

            } else if (check == "1개월" || check == "2개월") {
                binding.testing.text = "[진단] 프회워"
                if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                    binding.testing.text = "[진단] 프유워"
                } else {

                }

                if(cos - hdam > dat) {
                    binding.test1.text = "[단기] 재산초과"
                    if (local.contains("서울")) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 13200 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 16500 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 19800 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 11600 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 14500 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 17400 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 6800 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 8500 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 10200 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 6000 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 7500 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 9000 < dat) {
                                binding.testing.text = "[진단] 프유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    }
                }
            } else if (check == "3개월" || check == "4개월" || check == "5개월" || check == "6개월") {
                binding.testing.text = "[진단] 회워"

                if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                    binding.testing.text = "[진단] 유워"
                }

                if(cos - hdam > dat) {
                    binding.test1.text = "[단기] 재산초과"
                    if (local.contains("서울")) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 13200 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 16500 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 19800 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }

                    } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                            "김포"
                        )
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 11600 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 14500 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 17400 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                            "이천"
                        ) || local.contains("평택")
                    ) {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 6800 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 8500 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 10200 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    } else {
                        if (group == 1 || group == 2) {
                            if ((cos - hdam) - 6000 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 3) {
                            if ((cos - hdam) - 7500 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        } else if (group == 4) {
                            if ((cos - hdam) - 9000 < dat) {
                                binding.testing.text = "[진단] 유워"
                            } else {
                                binding.test2.text = "[장기] 재산초과"
                            }
                        }
                    }
                }
            } else if (check == "6개월 이상") {
                binding.testing.text = "[진단] 단순워크"
            }

            binding.money.text = "[재산] ${cos-hdam}"

            if(binding.before.text == "[특이] 5년내 면책이력") {
                binding.test1.text = "[단기] 면책이력 불가"
            }

            if(binding.bae.text == "[특이] 배우자 모르게") {
                binding.test1.text = "[단기] 배우자 모르게"
            }

            val dan = binding.test1.text.toString().replace("[단기]","").replace("만 3~5년납","").trim()
            try {
                if((dan.toInt() > 0) and (dan.toInt() < jangcos)) {
                    binding.testing.text = "[진단] 회생유리"
                }
            }catch (e:Exception) {
//                showToast("진단오류")
            }
            showToast("값이 변경되었습니다.")
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                handleFile(uri)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleFile(uri: Uri) {
        val contentResolver = applicationContext.contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        val hwpFile: HWPFile = HWPReader.fromInputStream(inputStream)
        val hwpText = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText)

        val lines = hwpText.split("\n")
        val recognizedText = StringBuilder()
        binding.name.text = "[이름] ${lines[2]}"
        binding.use.text = ""

        for (line in lines) {
            recognizedText.append("$line\n")

            val keyword = "세전"
            val keyword2 = "세후"
            val keyword3 = "실소득"
            val keyword4 = "시세"
            val keyword5 = "만"
            val keyword6 = "대출"
            val keyword7 = "보증금"

            val linet = line.replace(" ", "")

            try {
                if(linet.contains("재직")) {
                    val startIndex = linet.indexOf(":")
                    val word = linet.substring(startIndex + 1, linet.length)

                    work = word
                }
            }catch(e:Exception) {
//                showToast("직업오류")
            }
            try {
                if(!linet.contains("없음")) {

                    if (linet.contains("현대")) {
                        val startIndex = linet.indexOf("현대")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("롯데")) {
                        val startIndex = linet.indexOf("롯데")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("신한")) {
                        val startIndex = linet.indexOf("신한")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("하나")) {
                        val startIndex = linet.indexOf("하나")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("삼성")) {
                        val startIndex = linet.indexOf("삼성")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("우리")) {
                        val startIndex = linet.indexOf("우리")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("비씨")) {
                        val startIndex = linet.indexOf("비씨")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("국민")) {
                        val startIndex = linet.indexOf("국민")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }

                    if (linet.contains("농협")) {
                        val startIndex = linet.indexOf("농협")
                        val endIndex = linet.indexOf("만")
                        val word = linet.substring(startIndex + keyword4.length, endIndex).replace("카드","").replace("총","")
                        card += word.toInt()
                    }
                }
            }catch(e:Exception) {

            }

            if (linet.contains("채무조정")) {
                if (linet.contains("면책")) {
                    val words = linet.substring(5, linet.length)
                    binding.use.text = "[특이] $words"
                    if(words.contains("20-") || words.contains("21-") || words.contains("22-") || words.contains("23-") || words.contains("24-") || words.contains("20년") || words.contains("21년") || words.contains("22년") || words.contains("23년") || words.contains("24년")) {
                        binding.before.text = "[특이] 5년내 면책이력"
                    }
                }
            }

            try {
                if(linet.contains("재산")) {
                   if(linet.length > 4) {
                       if(linet.contains("본인명의")) {
                           val startIndex = linet.indexOf(":")
                           val endIndex = linet.indexOf("만")
                           val word = linet.substring(startIndex + keyword4.length, endIndex)

                           val words = word.replace("적금","")
                           je += words.toInt()
                           binding.mhouse.setText("$je")
                       } else if(linet.contains("배우자명의")) {
                           val startIndex = linet.indexOf("시세")
                           val endIndex = linet.indexOf("만")
                           val word = linet.substring(startIndex + keyword4.length, endIndex)

                           if(!word.contains("천")) {
                               val money = word.substring(0,1).toInt()*10000


                               if (linet.contains(keyword6)) {
                                   val startIndex2 = linet.indexOf(keyword6)
                                   val word2 = linet.substring(startIndex2 + keyword6.length, linet.length)
                                   val endIndex2 = word2.indexOf(keyword5)
                                   val word3 = word2.substring(0, endIndex2)


                                   val bnworth = word3.replace("억", "")

                                   val bmoney = (money - bnworth.toInt()) / 2

                                   je += bmoney
                                   bje += bnworth.toInt()
                                   binding.mhouse.setText("$je")
                                   binding.bhouse.setText("$bje")
                               }

                           }
                           if(word.contains("적금")) {
                               val words = word.replace("적금","")
                               je += words.toInt()/2
                               binding.mhouse.setText("$je")
                           }

                       } else {
                           val startIndex = linet.indexOf(":")
                           val endIndex = linet.indexOf("만")
                           val word = linet.substring(startIndex + 1, endIndex).replace("적금","")
                           je += word.toInt()
                           binding.mhouse.setText("$je")
                       }

                   }
                }
            } catch(e:Exception) {
//                showToast("재산 오류")
            }
            try {
                if(linet.contains("차량:")) {
                    if(!linet.contains("X")) {
                        if(!linet.contains("배우자명의")) {
                            if(linet.contains(keyword4)) {
                                val startIndex = linet.indexOf(keyword4)
                                val endIndex = linet.indexOf("만")
                                val word = linet.substring(startIndex + keyword4.length, endIndex).replace("-","")
                                binding.mcar.setText("$word")
                            }

                            if(linet.contains("할부")) {
                                val Index = linet.indexOf("할부")
                                val wordss = linet.substring(Index + 2, linet.length)
                                val eIndex = wordss.indexOf("만")
                                val words = wordss.substring(0, eIndex).replace("-","")

                                binding.bcar.setText("$words")
                            }

                            if(linet.contains("담보")) {
                                val Index = linet.indexOf("담보")
                                val wordss = linet.substring(Index + 2, linet.length)
                                val eIndex = wordss.indexOf("만")
                                val words = wordss.substring(0, eIndex).replace("-","").replace(":","")

                                binding.bcar.setText("$words")
                            }

                            if(linet.contains(keyword6)) {
                                val startIndex = linet.indexOf(keyword6)
                                val wordss = linet.substring(startIndex + keyword6.length, linet.length)
                                val endIndex = wordss.indexOf("만")
                                val word = wordss.substring(0, endIndex).replace("-","")
                                binding.bcar.setText("$word")
                            }
                        } else {
                            if(linet.contains(keyword4)) {
                                val startIndex = linet.indexOf(keyword4)
                                val endIndex = linet.indexOf("만")
                                val word = linet.substring(startIndex + keyword4.length, endIndex).replace("-","")
                                val mon = word.toInt()/2
                                binding.mcar.setText("$mon")
                            }

                            if(linet.contains("할부")) {
                                val Index = linet.indexOf("할부")
                                val wordss = linet.substring(Index + 2, linet.length)
                                val eIndex = wordss.indexOf("만")
                                val words = wordss.substring(0, eIndex).replace("-","")
                                val mon = words.toInt()/2
                                binding.bcar.setText("$mon")
                            }

                            if(linet.contains("담보")) {
                                val Index = linet.indexOf("담보")
                                val wordss = linet.substring(Index + 2, linet.length)
                                val eIndex = wordss.indexOf("만")
                                val words = wordss.substring(0, eIndex).replace("-","").replace(":","")
                                val mon = words.toInt()/2
                                binding.bcar.setText("$mon")
                            }

                            if(linet.contains(keyword6)) {
                                val startIndex = linet.indexOf(keyword6)
                                val wordss = linet.substring(startIndex + keyword6.length, linet.length)
                                val endIndex = wordss.indexOf("만")
                                val word = wordss.substring(0, endIndex).replace("-","")
                                val mon = word.toInt()/2
                                binding.bcar.setText("$mon")
                            }
                        }

                    }
                }
            }catch(e:Exception) {
//                showToast("차량 측정 오류")
            }
        try {
            if (linet.contains("지역")) {
                local = linet
                if (linet.contains("배우자명의")) {
                    val startIndex = linet.indexOf(keyword4)
                    if(linet.contains(keyword5)) {
                        val endIndex = linet.indexOf(keyword5)
                        val word = linet.substring(startIndex + keyword4.length, endIndex)
                        val bworth = word
                        if(!word.contains("천")) {
                            val money = bworth.substring(0,1).toInt()*10000

                            if (linet.contains(keyword6)) {
                                val startIndex2 = linet.indexOf(keyword6)
                                val word2 = linet.substring(startIndex2 + keyword6.length, linet.length)
                                val endIndex2 = word2.indexOf(keyword5)
                                val word3 = word2.substring(0, endIndex2)


                                val bnworth = word3.replace("억", "")

                                val bmoney = (money - bnworth.toInt()) / 2
                                je += bmoney
                                bje += bnworth.toInt()
                                binding.mhouse.setText("$je")
                                binding.bhouse.setText("$bje")
                            }

                        } else {

                        }

                        if (binding.mhouse.text.toString() == "") {
                            val bmoney = bworth.toInt() / 2
                            je += bmoney
                            binding.mhouse.setText("$je")
                        }
                    } else {
                        val Index = linet.indexOf("월세")
                        val word = linet.substring(Index + 2, Index + 7).replace("월","").replace("만","").replace("/","").replace(",","").replace(":","")
                        val bmoney = word.toInt()/2
                        je += bmoney
                        binding.mhouse.setText("$je")
                    }


                } else if (linet.contains("본인명의")) {
                    if (linet.contains(keyword4)) {
                        val startIndex = linet.indexOf(keyword4)
                        val endIndex = linet.indexOf(keyword5)
                        val word = linet.substring(startIndex + keyword4.length, endIndex)

                        val startIndex2 = linet.indexOf(keyword6)
                        val word2 = linet.substring(startIndex2 + keyword6.length, linet.length)
                        val endIndex2 = word2.indexOf(keyword5)
                        val word3 = word2.substring(0, endIndex2)
                        var bworth = word
                        var bnworth = word3

                        if(word2.contains("억") || word2.contains("만")) {
                            val end = word2.indexOf(keyword5)
                            val va = word2.substring(2,end).toInt()

                            val va1 = word2.substring(0,1).toInt()
                            val com = va1 * 10000 + va
                            bje += com
                            binding.bhouse.setText("$bje")

                        }
//                        if(bworth.substring(0,1).toInt() <100) {
//                            if(word.substring(0,1).contains("천")) {
//                                val words = word.substring(0,1).replace("천","").replace("억","").replace("만","")
//                                bworth = (words.toInt()*1000).toString()
//                                binding.mhouse.setText("$je")
//                            } else {
//                                bworth = (word.toInt()*10000).toString()
//                            }
//                        }

                        val vpn = word.substring(0,1).toInt()*10000

                        je += vpn
                        binding.mhouse.setText("$je")
                    } else if (linet.contains(keyword7)) {
                        val startIndex = linet.indexOf(keyword7)
                        val endIndex = linet.indexOf(keyword5)
                        val word = linet.substring(startIndex + keyword7.length, endIndex)

                        val money = word.replace("억", "").replace(":", "")
                        je += money.toInt()
                        binding.mhouse.setText("$je")
                    } else if(linet.contains("전세금:")) {
                        val startIndex = linet.indexOf("전세금:")
                            val words = linet.substring(startIndex + 4, linet.length)
                        val endIndex = words.indexOf("천")
                            val word = words.substring(0, endIndex)
                            var bworth = word.replace("억", "").replace("만","").replace("천","")

                            bworth = (bworth.toInt()*1000).toString()

                            je += bworth.toInt()
                            binding.mhouse.setText("$je")
                    }
                } else if (linet.contains("월세:")) {
                    val startIndex = linet.indexOf("월세:")
                    val endIndex = linet.indexOf("/")
                    val word = linet.substring(startIndex + 3, endIndex)

                    je += word.toInt()
                    binding.mhouse.setText("$je")
                }
            }
        } catch(e:Exception) {
//            showToast("재산 측정 오류")
        }

            if (linet.contains("특이사항")) {
                if (linet.contains("모르게")) {
                    binding.bae.text = "[특이] 배우자 모르게"
                }

                if( linet.contains("연체")) {
                    if(linet.contains("1개월")) {
                        check = "1개월"
                    } else if(linet.contains("2개월")) {
                        check = "2개월"
                    } else if(linet.contains("3개월")) {
                        check = "3개월"
                    } else if(linet.contains("4개월")) {
                        check = "4개월"
                    } else if(linet.contains("5개월")) {
                        check = "5개월"
                    } else if(linet.contains("6개월")) {
                        check = "6개월"
                    } else {
                        check = "6개월 이상"
                    }
                }
            }

            if (linet.contains("만60세부모") || linet.contains("60세이상")) {
                val words = linet.substring(6, linet.length - 1)
                if (words.contains("부") || words.contains("모") || words.contains("60세이상")) {
                    binding.parent.text = "[특이] 60세 이상 부모 O"
                } else {
                    binding.parent.text = "[특이] 60세 이상 부모 X"
                }

            }

            try {
                if (linet.contains("연봉")) {
                    if (linet.contains(keyword) && linet.contains(keyword2)) {
                        val startIndex = linet.indexOf(keyword)
                        val endIndex = linet.indexOf(keyword5)
                        if (startIndex != -1 && startIndex + keyword.length <= linet.length) {
                            val words = linet.substring(startIndex + keyword.length, endIndex)

                            val word = words.replace("만", "").replace("/", "").replace("평균", "")
                                .replace("예상", "").replace("연", "").replace("월", "").toInt()

                            if (word > 1000) {
                                binding.moneyy.setText(word.toString())
                            } else {
                                binding.moneym.setText(word.toString())
                            }
                        }

                        val Index = linet.indexOf(keyword2)

                        if (Index != -1 && Index + keyword2.length <= linet.length) {
                            val words = linet.substring(Index + keyword2.length, linet.length)

                            if (words.contains("~")) {
                                val Indexs = words.indexOf("~")
                                val wordss = words.substring(Indexs + 1, Indexs + 5)

                                val word =
                                    wordss.replace("만", "").replace("월", "").replace("평균", "")
                                        .replace("예상", "").replace(")", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }


                            } else if (words.contains("/")) {
                                val word = words.replace("만", "").replace("월", "").replace("평균", "")
                                    .replace("예상", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }


                            } else {
                                val word = words.replace("만", "").replace("월", "").replace("평균", "")
                                    .replace("예상", "").replace("연", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }


                            }
                        }
                    } else if (linet.contains(keyword3)) {
                        val startIndex = linet.indexOf(keyword3)
                        if (startIndex != -1 && startIndex + keyword3.length + 5 <= linet.length) {
                            val words = linet.substring(
                                startIndex + keyword3.length,
                                startIndex + keyword3.length + 5
                            )

                            if (linet.contains("/")) {
                                val Index = linet.indexOf("/")
                                val wordss = linet.substring(0, Index - 1)

                                val word =
                                    wordss.replace("만", "").replace("월", "").replace("평균", "")
                                        .replace("예상", "").replace("22년", "").replace("소득금액", "")
                                        .replace("연봉:", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }

                                if (words.contains("~")) {
                                    val Indexs = linet.indexOf("~")
                                    val wordsss = linet.substring(Indexs + 1, Indexs + 5)

                                    val sword =
                                        wordsss.replace("만", "").replace("월", "").replace("평균", "")
                                            .replace("예상", "").toInt()

                                    if (sword > 1000) {
                                        binding.moneyy.setText(sword.toString())
                                    } else {
                                        binding.moneym.setText(sword.toString())
                                    }
                                }
                            } else if (linet.contains(",")) {
                                val Index = linet.indexOf(",")
                                val wordss = linet.substring(0, Index - 1)

                                val word =
                                    wordss.replace("만", "").replace("월", "").replace("평균", "")
                                        .replace("예상", "").replace("22년", "").replace("소득금액", "")
                                        .replace("연봉:", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }

                                if (words.contains("~")) {
                                    val Indexs = linet.indexOf("~")
                                    val wordsss = linet.substring(Indexs + 1, Indexs + 5)

                                    val sword =
                                        wordsss.replace("만", "").replace("월", "").replace("평균", "")
                                            .replace("예상", "").toInt()

                                    if (sword > 1000) {
                                        binding.moneyy.setText(sword.toString())
                                    } else {
                                        binding.moneym.setText(sword.toString())
                                    }
                                }
                            } else {
                                val word = words.replace("만", "").replace("월", "").replace("평균", "")
                                    .replace("예상", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }
                            }


                        }
                    } else if (linet.contains(keyword2)) {
                        val startIndex = linet.indexOf(keyword2)
                        if (startIndex != -1 && startIndex + keyword2.length <= linet.length) {
                            val words = linet.substring(
                                startIndex + keyword2.length, linet.length
                            )

                            if (words.contains("~")) {
                                val Index = linet.indexOf("~")
                                val wordss = linet.substring(Index + 1, Index + 5)

                                val word =
                                    wordss.replace("만", "").replace("평균", "").replace("예상", "")
                                        .toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }


                                val words = linet.substring(3, 7)

                                binding.moneyy.setText(words)


                            } else if (linet.contains("/")) {
                                val word = words.replace("만", "").replace("월", "").replace("평균", "")
                                    .replace("예상", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }


                                val wordss = linet.substring(3, 7).replace("만", "")

                                binding.moneyy.setText(wordss)
                            } else {
                                val endIndex = words.indexOf(keyword5)
                                val wordss = words.substring(0, endIndex)
                                val word = wordss.replace("만", "").replace("월", "").replace("평균", "")
                                    .replace("예상", "").toInt()

                                if (word > 1000) {
                                    binding.moneyy.setText(word.toString())
                                } else {
                                    binding.moneym.setText(word.toString())
                                }

                                if (linet.contains("+")) {
                                    val Index = linet.indexOf("+")
                                    val wordss = linet.substring(Index + 1, Index + 10)
                                    val Index2 = wordss.indexOf("만")
                                    val words = wordss.substring(Index2 - 4, Index2)

                                    val word2 =
                                        words.replace("만", "").replace("월", "").replace("평균", "")
                                            .replace("예상", "").toInt()

                                    val all = binding.moneym.text.toString().toInt() + word2

                                    if (all > 1000) {
                                        binding.moneyy.setText(all.toString())
                                    } else {
                                        binding.moneym.setText(all.toString())
                                    }
                                }

                            }
                        }
                    } else if (linet.contains("~")) {
                        val Index = linet.indexOf("~")
                        val wordss = linet.substring(Index + 1, Index + 5)

                        val word =
                            wordss.replace("만", "").replace("평균", "").replace("예상", "").toInt()


                        if (word > 1000) {
                            binding.moneyy.setText(word.toString())
                        } else {
                            binding.moneym.setText(word.toString())
                        }
                    } else {
                        val word =
                            linet.replace("월", "").replace("만", "").replace("예상", "")
                                .replace("평균", "")
                                .replace("구직하면", "")
                                .replace("연봉:", "").toInt()

                        if (word > 1000) {
                            binding.moneyy.setText(word.toString())
                        } else {
                            binding.moneym.setText(word.toString())
                        }


                    }

                }
            } catch (e: Exception) {
//                showToast("연봉 계산 오류")
            }
            try {
                if (linet.contains("결혼여부")) {

                    if (linet.contains("미혼")) {
                        group = 1
                        binding.co.text = group.toString()
                    } else if (linet.contains("기혼")) {
                        group = 2
                        if(!linet.contains("+")) {
                            if (linet.contains("대학생")) {
                                val Index = linet.indexOf("대학생")

                                val word = linet.substring(Index + 3, linet.length)

                                val words = word.replace("자녀", "").replace("명", "").toInt()

                                binding.parent.text = words.toString()

                                group += words
                            }

                            if (linet.contains("미성년")) {
                                val Index = linet.indexOf("미성년")

                                val word = linet.substring(Index + 3, 4)

                                val words = word.replace("자녀", "").replace("명", "").toInt()

                                group += words

                                babys = words

                            }
                        } else {
                            val startIndex = linet.indexOf("+")
                            val Index = linet.indexOf("기혼")
                            val word2 = linet.substring(Index + 2,startIndex)

                            if(word2.contains("미성년")) {
                                val words = word2.replace("미성년","").replace("자녀","").replace("명", "").replace("/","").toInt()
                                group += words

                                babys = words
                                binding.test3.text = words.toString()
                            } else {
                                val words = word2.replace("성인","").replace("대학생","").replace("자녀","").replace("명", "").replace("/","").toInt()
                                group += words
                            }

                            val word = linet.substring(startIndex + 1,linet.length).replace("자녀","").replace("명","")
                            if(word.contains("미성년")) {
                                val words = word.replace("미성년","").replace("자녀","").replace("명", "").toInt()
                                group += words

                                babys = words
                            } else if(word.contains("대학생") || word.contains("성인")){
                                val words = word.replace("성인","").replace("대학생","").replace("자녀","").replace("명", "").toInt()
                                group += words
                            }

                        }

                    } else if (linet.contains("자녀없음")) {
                        group = 2
                    }

                    if (babys == 0) {
                        baby = 135
                    } else if (babys == 1) {
                        baby = 220
                    } else if (babys == 2) {
                        baby = 285
                    } else if (babys == 3) {
                        baby = 245
                    } else if (babys == 4) {
                        baby = 400
                    }

                    binding.baby.text = "[특이] 미성년 자녀 ${babys}명"
                }
            } catch(e:Exception) {
//                showToast("자녀 측정 오류")
            }
        }



        var moneym = binding.moneym.text.toString()
        var moneyy = binding.moneyy.text.toString()

        var real = 0
        if(moneyy != "0") {
            real = (moneyy.toDouble().toInt()*10-2400)/12
        } else {
            real = moneym.toDouble().toInt()*10-200
        }


        val excelResourceId = resources.getIdentifier("earn", "raw", packageName)

        val inputStreams: InputStream = resources.openRawResource(excelResourceId)

        val workbooks: Workbook = WorkbookFactory.create(inputStreams)
        val sheets: Sheet = workbooks.getSheetAt(0)

        var realmoney = 0

        for (x in 5..651) {
            val rows: Row = sheets.getRow(x) // 행 선택
            val cells: Cell = rows.getCell(0) // 셀 선택
            val Value = getCellValue(cells).toDoubleOrNull()?.toInt()

            if (Value != null) {
                if(real <= 1499) {
                    if ((Value <= real) && (real < Value + 5)) {
                        val cellsd: Cell = rows.getCell(group + 1)
                        var Values =
                            getCellValue(cellsd).toDoubleOrNull()?.toInt()
                        if (Values != null) {
                            if (babys == 1) {
                                Values = -12500
                            } else if (babys == 2) {
                                Values = -29160
                            } else if (babys >= 3) {
                                Values = -29160 + (babys - 2) * 25000
                            }

                            if (Values < 0) {
                                Values = 0
                            }

                            if(moneyy!="0") {
                                realmoney =
                                    (moneyy.toInt() * 10000 / 12 - (real * 94 + Values * 1.1).toInt()) / 10000
                            } else {
                                realmoney =
                                    (moneym.toInt() * 10000 - (real * 94 + Values * 1.1).toInt()) / 10000
                            }

                        }
                    }
                } else if(real <= 2999) {
                    if ((Value <= real) && (real < Value + 10)) {
                        val cellsd: Cell = rows.getCell(group + 1)
                        var Values =
                            getCellValue(cellsd).toDoubleOrNull()?.toInt()
                        if (Values != null) {
                            if (babys == 1) {
                                Values = -12500
                            } else if (babys == 2) {
                                Values = -29160
                            } else if (babys >= 3) {
                                Values = -29160 + (babys - 2) * 25000
                            }

                            if (Values < 0) {
                                Values = 0
                            }
                            if(moneyy!="0") {
                                realmoney =
                                    (moneyy.toInt() * 10000 / 12 - (real * 94 + Values * 1.1).toInt()) / 10000
                            } else {
                                realmoney =
                                    (moneym.toInt() * 10000 - (real * 94 + Values * 1.1).toInt()) / 10000
                            }
                        }
                    }
                } else {
                    if ((Value <= real) && (real < Value + 20)) {
                        val cellsd: Cell = rows.getCell(group + 1)
                        var Values =
                            getCellValue(cellsd).toDoubleOrNull()?.toInt()
                        if (Values != null) {
                            if (babys == 1) {
                                Values = -12500
                            } else if (babys == 2) {
                                Values = -29160
                            } else if (babys >= 3) {
                                Values = -29160 + (babys - 2) * 25000
                            }

                            if (Values < 0) {
                                Values = 0
                            }
                            if(moneyy!="0") {
                                realmoney =
                                    (moneyy.toInt() * 10000 / 12 - (real * 94 + Values * 1.1).toInt()) / 10000
                            } else {
                                realmoney =
                                    (moneym.toInt() * 10000 - (real * 94 + Values * 1.1).toInt()) / 10000
                            }
                        }
                    }
                }
            }
        }

        inputStreams.close()

        if (realmoney > moneym.toDouble().toInt()) {
            if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                acost = ((realmoney - baby - 50) * 2 / 3).toDouble().toInt()
                binding.test2.text = "[장기] ${acost}만"
                binding.test1.text = "[단기] ${realmoney - baby}만 3~5년납"
                binding.card.text = "[소득] $realmoney"
            } else {
                acost = ((moneyy.toDouble().toInt() - baby) * 2 / 3).toDouble().toInt()
                binding.test2.text = "[장기] ${acost}만"
                binding.test1.text = "[단기] ${realmoney - baby}만 3~5년납"
                binding.card.text = "[소득] $realmoney"
            }
        } else {
            if (binding.parent.text == "[특이] 60세 이상 부모 O") {
                acost = ((moneym.toDouble().toInt() - baby - 50) * 2 / 3).toDouble().toInt()
                binding.test2.text = "[장기] ${acost}만"
                binding.test1.text = "[단기] ${(moneym.toDouble().toInt() - baby).toDouble().toInt()}만 3~5년납"
                binding.card.text ="[소득] $moneym"
            } else {
                acost = ((moneym.toDouble().toInt() - baby) * 2 / 3).toDouble().toInt()
                binding.test2.text = "[장기] ${acost}만"
                binding.test1.text = "[단기] ${(moneym.toDouble().toInt() - baby).toDouble().toInt()}만 3~5년납"
                binding.card.text ="[소득] $moneym"
            }
        }

        binding.content.text = recognizedText


        try {
            val binData = hwpFile.binData
            if (binData.embeddedBinaryDataList.size > 0) {
                val embeddedBinaryData = binData.embeddedBinaryDataList[0]
                val bitmap = BitmapFactory.decodeByteArray(embeddedBinaryData.data, 0, embeddedBinaryData.data.size)
                binding.testimage2.setImageBitmap(bitmap)
                processImage(bitmap)
            }
            if (binData.embeddedBinaryDataList.size > 1) {
                // 두 번째 이미지

                val embeddedBinaryData2 = binData.embeddedBinaryDataList[1]
                val bitmap2 = BitmapFactory.decodeByteArray(embeddedBinaryData2.data, 0, embeddedBinaryData2.data.size)
                binding.testimage.setImageBitmap(bitmap2)
                processImage(bitmap2)
            }
        } catch(e:Exception) {
//            showToast("이미지오류")
        }

    }

    private fun getCellValue(cell: Cell?): String {
        return when (cell?.cellType) {
            Cell.CELL_TYPE_STRING -> cell.stringCellValue
            Cell.CELL_TYPE_NUMERIC -> cell.numericCellValue.toString()
            Cell.CELL_TYPE_BOOLEAN -> ""
            else -> ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient()
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                processTextResult(visionText)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processTextResult(visionText: Text) {
        try {

            val textBlocks = visionText.textBlocks
            if (textBlocks.isEmpty()) {
                return
            }
            val currentDate = LocalDate.now()

            val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            var blockText = textBlocks[0].text
            val listText = blockText.split("\n")
            var rows = 0

            val twoArray = ArrayList<ArrayList<Any>>()

            val recognizedText = StringBuilder()
            val recognizedText2 = StringBuilder()


            for (text in listText) {
                val date = text.replace(",", ".").replace(" ","")
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
                        } else if(fourText.toInt() > 0){
                            twoArray[rows].add(date)
                            rows++
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


                    recognizedText.append(date).append("일").append("\n")

                    if (currentDate >= plusMonth) {
                        // 6개월 이상
                        if(clean.toInt().mod(10) >= 5) {
                            value += clean.toInt().div(10) + 1
                            recognizedText2.append(clean.toInt().div(10)+1).append("만").append("\n")
                        } else {
                            value += clean.toInt().div(10)
                            recognizedText2.append(clean.toInt().div(10)).append("만").append("\n")
                        }

                    } else {
                        // 6개월 이하
                        if(clean.toInt().mod(10) >= 5) {
                            bValue += clean.toInt().div(10) + 1
                            recognizedText2.append(clean.toInt().div(10)+1).append("만").append("\n")
                        } else {
                            bValue += clean.toInt().div(10)
                            recognizedText2.append(clean.toInt().div(10)).append("만").append("\n")
                        }
                    }
            }

            cost = value + bValue + card
            binding.total.setText(cost.toString())
            binding.btotal.setText(bValue.toString())
        } catch(e:Exception) {
//            showToast("대상채무오류")
        }

        val dam =
            binding.bhouse.text.toString().toDouble().toInt() + binding.bcar.text.toString()
                .toDouble().toInt() + binding.bill.text.toString().toDouble().toInt()
        val hdam = binding.bhouse.text.toString().toDouble().toInt()
        val dat = cost- dam
        val year = round(dat / acost / 12.0)
        val cos =
            binding.mhouse.text.toString().toDouble().toInt() + binding.mcar.text.toString().toDouble().toInt()
        binding.money.text = "[재산] $dat-$bje"
        binding.dat.text = "[대상] $dat"
        if (binding.bhouse.text.toString() != "") {
            bCost = bValue / cost.toDouble() * 100
            val bcost = round(bCost * 10) / 10.0
            binding.co.text = "[특이] 6개월 내 채무 $bcost%"
        }

        acost = ((binding.moneyy.text.toString().toDouble()
            .toInt() * 0.8 / 12 - baby - 50) * 2 / 3).toInt()
        if(acost <0 || year < 0 || year >= 8) {
            binding.test2.text = "[장기] ${dat/96}만 8년납"
        } else {
            binding.test2.text = "[장기] ${acost}만 ${year}년납"
        }


        if(acost <0 || year < 0 || year >= 8) {
            binding.test2.text = "[장기] ${dat/96}만 8년납"
        } else {
            binding.test2.text = "[장기] ${acost}만 ${year}년납"
        }

        val aacost = binding.test2.text.split(" ")
        var jangcos = aacost[1].replace("만","").toInt()

        if(binding.half.text.contains("과반수")) {

            if(jangcos.mod(10) < 5) {
                jangcos -= jangcos.mod(10) - 5
                binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
            } else {
                jangcos -= jangcos.mod(10) - 10
                binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
            }
        } else {
            if(jangcos.mod(10) < 5) {
                jangcos -= jangcos.mod(10)
                binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
            } else {
                jangcos -= jangcos.mod(10) - 5
                binding.test2.text = "[장기] ${jangcos}만 ${aacost[2]}"
            }

        }


        if (check == "없음") {
            binding.testing.text = "[진단] 신회워"
            if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                binding.testing.text = "[진단] 신유워"
            }

            if(cos-hdam > dat) {
                binding.test1.text = "[단기] 재산초과"
                if (local.contains("서울")) {
                    if (group == 1|| group == 2) {
                        if ((cos - hdam - 13200) < dat) {
                            binding.testing.text = "[진단] 신유워"

                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam - 16500) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam - 19800) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }

                } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                        "김포"
                    )
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam - 11600) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam - 14500) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam - 17400) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                        "이천"
                    ) || local.contains("평택")
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam - 6800) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 8500 < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 10200 < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam - 6000) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam - 7500) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam - 9000) < dat) {
                            binding.testing.text = "[진단] 신유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                }
            }

        } else if (check == "1개월" || check == "2개월") {
            binding.testing.text = "[진단] 프회워"
            if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                binding.testing.text = "[진단] 프유워"
            } else {

            }

            if(cos - hdam > dat) {
                binding.test1.text = "[단기] 재산초과"
                if (local.contains("서울")) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 13200 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 16500 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 19800 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                        "김포"
                    )
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 11600 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 14500 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 17400 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                        "이천"
                    ) || local.contains("평택")
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 6800 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 8500 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 10200 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 6000 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 7500 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 9000 < dat) {
                            binding.testing.text = "[진단] 프유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                }
            }
        } else if (check == "3개월" || check == "4개월" || check == "5개월" || check == "6개월") {
            binding.testing.text = "[진단] 회워"

            if (work.contains("무직") || work.contains("사업자") || binding.bae.text.equals("[특이] 배우자 모르게") ||  binding.before.text == "[특이] 5년내 면책이력") {
                binding.testing.text = "[진단] 유워"
            }

            if(cos - hdam > dat) {
                binding.test1.text = "[단기] 재산초과"
                if (local.contains("서울")) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 13200 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 16500 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 19800 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }

                } else if (local.contains("용인") || local.contains("화성") || local.contains("세종") || local.contains(
                        "김포"
                    )
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 11600 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 14500 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 17400 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else if (local.contains("안산") || local.contains("광주") || local.contains("파주") || local.contains(
                        "이천"
                    ) || local.contains("평택")
                ) {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 6800 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 8500 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 10200 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                } else {
                    if (group == 1 || group == 2) {
                        if ((cos - hdam) - 6000 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 3) {
                        if ((cos - hdam) - 7500 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    } else if (group == 4) {
                        if ((cos - hdam) - 9000 < dat) {
                            binding.testing.text = "[진단] 유워"
                        } else {
                            binding.test2.text = "[장기] 재산초과"
                        }
                    }
                }
            }
        } else if (check == "6개월 이상") {
            binding.testing.text = "[진단] 단순워크"
        }

        binding.money.text = "[재산] ${cos-hdam}"

        if(binding.before.text == "[특이] 5년내 면책이력") {
            binding.test1.text = "[단기] 면책이력 불가"
        }

        if(binding.bae.text == "[특이] 배우자 모르게") {
            binding.test1.text = "[단기] 배우자 모르게"
        }

        val dan = binding.test1.text.toString().replace("[단기]","").replace("만 3~5년납","").trim()
        try {
            if((dan.toInt() > 0) and (dan.toInt() < jangcos)) {
                binding.testing.text = "[진단] 회생유리"
            }
        }catch (e:Exception) {
//            showToast("진단오류")
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
            showToast("텍스트가 복사되었습니다.")
        }
    }

    private fun buildResultText(): String {
        val name = binding.name.text.toString()
        val baby = binding.baby.text.toString()
        val parent = binding.parent.text.toString()
        val bae = binding.bae.text.toString()
        val longT = binding.before.text.toString()
        val bae2 = binding.bae2.text.toString()
        val testing = binding.testing.text.toString()
        val test1 = binding.test1.text
        val test2 = binding.test2.text
        val card = binding.card.text
        val co = binding.co.text
        val dat = binding.dat.text
        val je = binding.money.text
        val use = binding.use.text

        return "$name\n\n$card\n$dat\n$je\n\n$co\n$baby\n$parent\n$bae\n$use\n$bae2\n$longT\n\n$test1\n$test2\n\n$testing"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




}
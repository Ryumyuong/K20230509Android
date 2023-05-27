package com.example.test000

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test000.databinding.ActivityMain2Binding
import com.example.test000.databinding.FragmentOneBinding
import com.example.test000.databinding.FragmentThreeBinding
import com.example.test000.databinding.FragmentTwoBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity2 : AppCompatActivity() {
    var myDB: DatabaseHelper? = null
    lateinit var binding: ActivityMain2Binding
    var datas: MutableList<String>? = null
    lateinit var filePath: String

    var editTextName: EditText? = null
    var editTextPassword: EditText? = null
    var editTextEmail: EditText? = null
    var editTextID: EditText? = null
    var buttonInsert: Button? = null
    var buttonView: Button? = null
    var buttonUpdate: Button? = null
    var buttonDelete: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        myDB = DatabaseHelper(this)
        // findViewById 대신 binding 사용
        editTextName = binding.editTextName
        editTextPassword = binding.editTextPassword
        editTextEmail = binding.editTextEmail
        editTextID = binding.editTextID
        buttonInsert = binding.buttonInsert
        buttonView = binding.buttonView
        buttonUpdate = binding.buttonUpdate
//        buttonDelete = findViewById(R.id.buttonDelete)
        buttonDelete = binding.buttonDelete
        // 최초 1회 실행시 만든 함수 호출
        AddData()
        viewAll()
        UpdateData()
        DeleteData()

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null

                bitmap?.let {
                    //resizing된 사진 할당
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("lmj", "bitmap null")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }


        binding.galleryButton.setOnClickListener {
            //gallery app........................
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.userImageView.setImageBitmap(bitmap)
            }
        }


        binding.cameraButton.setOnClickListener {
            //camera app......................
            //파일 준비...............
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            //실제 물리 경로 storage/emulated/0/Android/data/files/Pictures
            filePath = file.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.test000.fileprovider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)

        }

        binding.memberList.setOnClickListener {
            val intent = intent

            val res = myDB!!.allData
            val buffer = StringBuffer()
            var count = 0
            //res 형 -> Cursor, 0행부터 시작
            while (res.moveToNext()) {
                count = count + 1
                intent.putExtra("memberList${count}",
                    """
${res.getString(0)}
${res.getString(1)}
${res.getString(2)}
${res.getString(3)}
                """.trimIndent())
                intent.putExtra("count","${count}")
                Log.d("lmj","${count}")
            }

            setResult(Activity.RESULT_OK, intent)
            finish()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)


        return super.onCreateOptionsMenu(menu)
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // options = 사진 관련 옵션을 정한다.
        val options = BitmapFactory.Options()
        //inJustDecodeBounds = true가 되면 옵션만 처리한다.
        options.inJustDecodeBounds = true
        try {
            //contentResolver.openInputStream = 바이트로 읽어서 바이트로 반환
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun AddData() {
        buttonInsert!!.setOnClickListener {
            val isInserted = myDB!!.insertData(
                editTextName!!.text.toString(),
                editTextPassword!!.text.toString(),
                editTextEmail!!.text.toString()
            )
            if (isInserted == true) Toast.makeText(
                this@MainActivity2,
                "데이터추가 성공",
                Toast.LENGTH_LONG
            )
                .show() else Toast.makeText(this@MainActivity2, "데이터추가 실패", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun viewAll() {
        buttonView!!.setOnClickListener(View.OnClickListener {
            val res = myDB!!.allData
            if (res.count == 0) {
                ShowMessage("실패", "데이터를 찾을 수 없습니다.")
                return@OnClickListener
            }
            //StringBuffer - 하나의 객체에 해당 문자열을 추가만 하는 형태여서 주소를 새로 생성안함
            val buffer = StringBuffer()
            //res 형 -> Cursor, 0행부터 시작
            while (res.moveToNext()) {
                buffer.append(
                    //trimIndent - 공백제거
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
    비밀번호: ${res.getString(2)}
    
    """.trimIndent()
                )
                buffer.append(
                    """
    이메일: ${res.getString(3)}
    
    
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
                editTextPassword!!.text.toString(),
                editTextEmail!!.text.toString()
            )
            if (isUpdated == true) Toast.makeText(this@MainActivity2, "데이터 수정 성공", Toast.LENGTH_LONG)
                .show() else Toast.makeText(this@MainActivity2, "데이터 수정 실패", Toast.LENGTH_LONG)
                .show()
        }
    }

    // 데이터베이스 삭제하기
    fun DeleteData() {
        buttonDelete!!.setOnClickListener {
            val deleteRows = myDB!!.deleteData(editTextID!!.text.toString())
            if (deleteRows > 0) Toast.makeText(this@MainActivity2, "데이터 삭제 성공", Toast.LENGTH_LONG)
                .show() else Toast.makeText(this@MainActivity2, "데이터 삭제 실패", Toast.LENGTH_LONG)
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
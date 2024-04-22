package lunamall.example.test18

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import lunamall.example.test18.databinding.ActivityMainBinding
import lunamall.example.test18.fragment.EightFragment
import lunamall.example.test18.fragment.EventFragment
import lunamall.example.test18.fragment.FiveFragment
import lunamall.example.test18.fragment.FourFragment
import lunamall.example.test18.fragment.NineFragment
import lunamall.example.test18.fragment.OneFragment
import lunamall.example.test18.fragment.SevenFragment
import lunamall.example.test18.fragment.SixFragment
import lunamall.example.test18.fragment.ThreeFragment
import lunamall.example.test18.fragment.TwoFragment
import lunamall.example.test18.model.Version
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val ALARM_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.main.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        checkAlarmPermission()

        val tabLayout = binding.Tabs

        val viewPager = binding.Viewpager

        viewPager.adapter= PagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "EVENT"
                }

                1 -> {
                    tab.text = "전자/기기"
                }

                2 -> {
                    tab.text = "생활가전"
                }

                3 -> {
                    tab.text = "식음료"
                }

                4 -> {
                    tab.text = "운동"
                }

                5 -> {
                    tab.text = "여가문화"
                }

                6 -> {
                    tab.text = "여행"
                }

                7 -> {
                    tab.text = "업무"
                }

                8 -> {
                    tab.text = "VVIP"
                }

                9 -> {
                    tab.text = "웰컴키트"
                }
            }
        }.attach()

        binding.Tabs.tabMode = TabLayout.MODE_SCROLLABLE

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        binding.home.setOnClickListener{
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.cart.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경") {
                val intent = Intent(this, InsertProduct::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

        binding.list.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LunaActivity::class.java)
                startActivity(intent)
            }
        }

        binding.profile.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, LoginAdmin::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginDetail::class.java)
                startActivity(intent)
            }
        }

        var pInfo = packageManager.getPackageInfo(packageName, 0)
        var versionCode = pInfo.versionCode
        val networkService = (applicationContext as MyApplication).networkService
        val versionCall = networkService.version()
        Log.d("lmj", "버전 : $versionCode")

        versionCall.enqueue(object : Callback<Version> {
            override fun onResponse(call: Call<Version>, response: Response<Version>) {
                var item = response.body()?.version
                val version = item
                if (version != null) {
                    if(version.equals(versionCode.toString())) {

                    } else {
                        showUpdateDialog()
                    }
                }

            }

            override fun onFailure(call: Call<Version>, t: Throwable) {
                Log.d("lmj", "버전오류 : ${t.message}")
                call.cancel()
            }

        })
    }

    private fun checkAlarmPermission() {
        val notificationManager = NotificationManagerCompat.from(this)
        if (!notificationManager.areNotificationsEnabled()) {
            Log.d("lmj","알람을 허용하세요")
            showAlarmPermissionDialog()
        } else {
            Log.d("lmj","알람이 허용되었습니다.")
        }
    }

    private fun showAlarmPermissionDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("알람 권한 요청")
        dialogBuilder.setMessage("알람을 설정하여 관련 소식을 받아보세요.")
        dialogBuilder.setPositiveButton("동의") { dialog, which ->
            // 사용자가 동의를 클릭하면 알람 권한을 요청합니다.
            ActivityCompat.requestPermissions(
                this,
                arrayOf("com.android.alarm.permission.SET_ALARM"),
                ALARM_PERMISSION_REQUEST_CODE
            )
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }

            startActivity(intent)

            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("거부") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("lmj", "Request code: ${PackageManager.PERMISSION_GRANTED}")
        if (requestCode == ALARM_PERMISSION_REQUEST_CODE) {
            val notificationManager = NotificationManagerCompat.from(this)
            if (notificationManager.areNotificationsEnabled()) {
                Toast.makeText(this, "알람 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 알람 권한이 거부됨
                Log.d("lmj", "Requesting alarm permission3")
                Toast.makeText(this, "알람 권한이 거부되었습니다.${grantResults[0]}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class PagerAdapter(activity: MainActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(EventFragment(), OneFragment(), TwoFragment(), ThreeFragment(), FourFragment(), FiveFragment(), SixFragment(), SevenFragment(), EightFragment(), NineFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

    }

    fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("업데이트 필요")
        builder.setMessage("새로운 버전이 있습니다. 업데이트가 필요합니다.")
        builder.setPositiveButton("업데이트") { _, _ ->
            val packageName = "lunamall.example.test18"
            // 플레이스토어로 이동하는 인텐트 실행
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${packageName}"))
            startActivity(intent)
        }
        builder.setNegativeButton("나중에") { _, _ ->
            // 사용자가 나중에 업데이트할지 선택한 경우
        }
        builder.show()
    }
}
package com.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test18.Model.CsrfToken
import com.example.test18.databinding.ActivityMainBinding
import com.example.test18.fragment.EightFragment
import com.example.test18.fragment.FiveFragment
import com.example.test18.fragment.FourFragment
import com.example.test18.fragment.NineFragment
import com.example.test18.fragment.OneFragment
import com.example.test18.fragment.SevenFragment
import com.example.test18.fragment.SixFragment
import com.example.test18.fragment.ThreeFragment
import com.example.test18.fragment.TwoFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "루나몰"


        val tabLayout = binding.Tabs

        val viewPager = binding.Viewpager

        viewPager.adapter= PagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "전자/기기"
                }

                1 -> {
                    tab.text = "생활가전"
                }

                2 -> {
                    tab.text = "식음료"
                }

                3 -> {
                    tab.text = "운동"
                }

                4 -> {
                    tab.text = "여가문화"
                }

                5 -> {
                    tab.text = "여행"
                }

                6 -> {
                    tab.text = "업무"
                }

                7 -> {
                    tab.text = "VVIP"
                }

                8 -> {
                    tab.text = "웰컴키트"
                }
            }
        }.attach()

        binding.Tabs.tabMode = TabLayout.MODE_SCROLLABLE

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)


        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                R.id.second_tab -> {
                    Log.d("lmj", "username==$username==")
                    if(username.equals("")) {
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                    }else {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.third_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.fourth_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else if(username.equals("admin")){
                        val intent = Intent(this, LoginAdmin::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LoginDetail::class.java)
                        startActivity(intent)
                    }
                }
            }
            true
        }
    }

    class PagerAdapter(activity: MainActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(OneFragment(), TwoFragment(), ThreeFragment(), FourFragment(), FiveFragment(), SixFragment(), SevenFragment(), EightFragment(), NineFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

    }
}
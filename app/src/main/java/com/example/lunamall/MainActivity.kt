package com.example.lunamall

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lunamall.databinding.ActivityMainBinding
import com.example.lunamall.fragment.OneFragment
import com.example.lunamall.fragment.ThreeFragment
import com.example.lunamall.fragment.TwoFragment
import com.google.android.material.tabs.TabLayoutMediator


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
                    tab.text = "방문예정"
                }

                1 -> {
                    tab.text = "방문완료"
                }

                2 -> {
                    tab.text = "취소/노쇼"
                }
            }
        }.attach()


        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.second_tab -> {
                    Toast.makeText(this@MainActivity, "미구현", Toast.LENGTH_SHORT).show()
                }
                R.id.third_tab -> {
                    Toast.makeText(this@MainActivity, "미구현", Toast.LENGTH_SHORT).show()
                }
                R.id.fourth_tab -> {
                    Toast.makeText(this@MainActivity, "미구현", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    class PagerAdapter(activity: MainActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(OneFragment(), TwoFragment(), ThreeFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

    }
}
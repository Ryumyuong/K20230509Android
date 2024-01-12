package com.example.lunamall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lunamall.databinding.FragmentTwoBinding
import com.google.android.material.tabs.TabLayoutMediator

class ThreeFragment : Fragment() {
    lateinit var binding: FragmentTwoBinding
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
            binding = FragmentTwoBinding.inflate(inflater, container, false)

//            val tabLayout = binding.AlarmTabs
//
//            //뷰 페이저 2 추가 .-> xml 에서 , viewpager 부분을 추가해야함.
//            val viewPager = binding.AlarmViewpager
//
//            viewPager.adapter= MyAlarmFragmentPagerAdapter(this)
//
//            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                when (position) {
//                    0 -> {
//                        tab.text = "빈자리 알림"
//                    }
//
//                    1 -> {
//                        tab.text = "예약 오픈 알림"
//                    }
//                }
//            }.attach()

            return binding.root
    }

//    class MyAlarmFragmentPagerAdapter(Fragment: Fragment): FragmentStateAdapter(Fragment){
//        val fragments: List<Fragment>
//        init {
//            fragments= listOf(OneAlarmFragment(), TwoAlarmFragment())
//        }
//        override fun getItemCount(): Int = fragments.size
//
//        override fun createFragment(position: Int): Fragment = fragments[position]
//
//    }
}
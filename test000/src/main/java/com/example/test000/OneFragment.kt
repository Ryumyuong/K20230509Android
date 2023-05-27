package com.example.test000



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test000.databinding.FragmentOneBinding
import com.example.test000.databinding.FragmentThreeBinding


class OneFragment : Fragment() {
    lateinit var binding: FragmentOneBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root

        binding.addLogin.setOnClickListener {
            Log.d("lmj", "시작")

        }

    }
}
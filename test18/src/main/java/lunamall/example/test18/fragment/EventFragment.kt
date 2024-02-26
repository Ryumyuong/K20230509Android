package lunamall.example.test18.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lunamall.example.test18.databinding.FragmentEventBinding

class EventFragment : Fragment() {
    lateinit var binding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        return binding.root
    }
}
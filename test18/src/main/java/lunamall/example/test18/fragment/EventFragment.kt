package lunamall.example.test18.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lunamall.example.test18.MyApplication
import lunamall.example.test18.databinding.FragmentEventBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventFragment : Fragment() {
    lateinit var binding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        var userId = ""
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (context?.applicationContext as MyApplication).networkService

        binding.event.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    val cart = InCart(username, "발렌타인 30년산", 6000, "", "")
                    val insertCall = networkService.insertCart(csrfToken, cart)

                    insertCall.enqueue(object : Callback<InCart> {
                        override fun onResponse(call: Call<InCart>, response: Response<InCart>) {
                            Toast.makeText(context, "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<InCart>, t: Throwable) {
                            call.cancel()
                        }
                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    call.cancel()
                }
            })
        }

        return binding.root
    }
}
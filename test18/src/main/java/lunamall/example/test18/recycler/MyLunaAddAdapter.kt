package lunamall.example.test18.recycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import lunamall.example.test18.R
import lunamall.example.test18.databinding.LunaAddRecyclerviewBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.User
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyLunaAddViewHolder(val binding: LunaAddRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button1: ImageView = itemView.findViewById(R.id.add)
    val button2: ImageView = itemView.findViewById(R.id.min)
}

class MyLunaAddAdapter(val context: Context, datas: MutableList<User>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var userData: MutableList<User>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyLunaAddViewHolder(LunaAddRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyLunaAddViewHolder).binding
        val user = userData?.get(position)
        binding.name.text = user?.userId
        binding.phone.text = user?.phone
        binding.price.text = user?.money.toString() + "P"

        holder.button1.setOnClickListener {
            val money = binding.addLuna.text.toString().toIntOrNull()//입력값
            var luna = user?.money

            if(money != null) {
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token

                        val addLunaCall = networkService.addLuna(csrfToken, user?.userId, money, user)

                        addLunaCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                luna = money?.let { it1 -> luna?.plus(it1) }
                                user?.money = luna

                                Toast.makeText(context, "루나가 추가되었습니다.", Toast.LENGTH_LONG).show()
                                notifyDataSetChanged()
                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                Log.d("lmj", "실패토큰 : ${t.message}")
                            }
                        })
                    }
                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                        Log.d("lmj", "실패토큰 : ${t.message}")
                    }
                })
            } else {
                Toast.makeText(context, "루나를 입력하세요", Toast.LENGTH_LONG).show()
            }

        }

        holder.button2.setOnClickListener {
            val mon = user?.money.toString()
            var luna = mon.toIntOrNull()
            val money = binding.minLuna.text
            val moneyValue = money.toString().toInt()

            if (mon != null) {
                if((luna?.minus(moneyValue))!! <0) {
                    Toast.makeText(context, "루나가 부족합니다.", Toast.LENGTH_LONG).show()
                } else {
                    if(moneyValue!=null) {
                        val csrfCall = networkService.getCsrfToken()

                        csrfCall.enqueue(object : Callback<CsrfToken> {
                            override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                                val csrfToken = response.body()?.token

                                val minLunaCall = networkService.minLuna(csrfToken, user?.userId, moneyValue, user)

                                minLunaCall.enqueue(object : Callback<Unit> {
                                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                        luna = moneyValue?.let { it1 -> luna?.minus(it1) }
                                        user?.money = luna

                                        Toast.makeText(context, "루나가 제거되었습니다.", Toast.LENGTH_LONG).show()
                                        notifyDataSetChanged()
                                    }

                                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                                        Log.d("lmj", "실패토큰 : ${t.message}")
                                    }
                                })
                            }
                            override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                                Log.d("lmj", "실패토큰 : ${t.message}")
                            }
                        })
                    } else {
                        Toast.makeText(context, "루나를 입력하세요", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(context, "mon이 null", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return userData?.size ?: 0
    }

}


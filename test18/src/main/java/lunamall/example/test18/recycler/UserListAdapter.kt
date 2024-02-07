package lunamall.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.MyApplication
import lunamall.example.test18.UpdateUser
import lunamall.example.test18.databinding.UserListRecyclerviewBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import lunamall.example.test18.model.User
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserListViewHolder(val binding: UserListRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val update: TextView = binding.update
    val delete: TextView = binding.delete
}

class UserListAdapter(val context: Context, datas: MutableList<User>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var userList: MutableList<User>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        UserListViewHolder(UserListRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as UserListViewHolder).binding
        val user = userList?.get(position)
        binding.vip.text = user?.vip.toString()
        binding.userId.text = user?.userId.toString()
        binding.money.text = user?.money.toString() + " 루나"
        binding.phone.text = user?.phone.toString()
        binding.address.text = user?.address.toString()

        holder.update.setOnClickListener {
            val intent = Intent(context, UpdateUser::class.java)
            intent.putExtra("userId", user?.userId.toString())
            context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    val deleteCall = networkService.deleteUser(csrfToken, user?.userId.toString())

                    deleteCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.d("lmj", "실패데이터 : ${t.message}")
                        }
                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                }
            })

        }
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

}


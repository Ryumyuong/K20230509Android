package lunamall.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.LunaActivity
import lunamall.example.test18.databinding.UserRecyclerviewBinding
import lunamall.example.test18.model.User
import okio.blackholeSink


class MyUserViewHolder(val binding: UserRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyUserAdapter(val context: Context, datas: MutableList<User>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var userData: MutableList<User>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyUserViewHolder(UserRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyUserViewHolder).binding
        val user = userData?.get(position)

        binding.vip.text = user?.vip
        binding.userId.text = user?.userId
        binding.money.text = user?.money.toString() + "P"
        binding.phone.text = user?.phone
        binding.address.text = user?.address

        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16 * context.resources.displayMetrics.density // 모서리 둥글게 설정 (16dp)
            setColor(Color.parseColor("#FFFF00")) // 기본 배경색 설정
        }

        when (binding.vip.text) {
            "브론즈" -> shape.setColor(Color.parseColor("#764529"))
            "플래티넘" -> shape.setColor(Color.parseColor("#4FCB8B"))
        }

        binding.vip.background = shape

        binding.money.setOnClickListener {
            val intent = Intent(context, LunaActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userData?.size ?: 0
    }

}


package com.example.test_pdtest.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.test_pdtest.Model.ItemData
import com.example.test_pdtest.databinding.ItemRecyclerviewBinding


class MyWaitingViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyWaitingAdapter(val context: Fragment, datas:MutableList<ItemData>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{
    var listDataFilter: MutableList<ItemData>? = datas
    var listDataUnFilter: MutableList<ItemData>? = datas

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filterString = constraint.toString()
                listDataFilter = if(filterString.isEmpty()){
                    Log.d("lmj", "filterString이 empty")
                    listDataUnFilter
                } else {
                    Log.d("lmj", "filterString이 NotEmpty")
                    val listDataFiltering = mutableListOf<ItemData>()
                    for (no in listDataUnFilter!!) {
                        if(no.toString().contains(filterString))
                            listDataFiltering.add(no)
                    }
                    listDataFiltering
                }
                val filterResults = FilterResults()
                Log.d("lmj", "데이터 값 : $listDataFilter")
                filterResults.values = listDataFilter
                Log.d("lmj", "함수 값 : ${filterResults.values}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listDataFilter = results?.values as MutableList<ItemData>?
                Log.d("lmj", "최종 데이터 : $listDataFilter")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyWaitingViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyWaitingViewHolder).binding
        val waiting = listDataFilter?.get(position)

        binding.itemtitle.text = waiting?.s_name
        binding.itemcontent.text = waiting?.s_description
        binding.itemprice.text = waiting?.s_price.toString()

        binding.cancelbutton.isInvisible = true

        val urlImg = waiting?.s_fileName

        if (urlImg != null) {
            val bitmap = decodeBase64(urlImg)
            binding.imageView.setImageBitmap(bitmap)
        } else {

        }
    }

    override fun getItemCount(): Int {
        return listDataFilter?.size ?: 0
    }

    private fun decodeBase64(base64Image: String?): Bitmap? {
        if (!base64Image.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
        return null
    }

}


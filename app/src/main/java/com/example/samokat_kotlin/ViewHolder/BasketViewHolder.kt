package com.example.samokat_kotlin.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samokat_kotlin.ItemClickListner
import com.example.samokat_kotlin.R

class BasketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    var txtBasketName: TextView
    var txtBasketPrice: TextView
    var imageBasketProduct: ImageView
    var listner: ItemClickListner? = null
    var basketCount:TextView

    init {
        txtBasketName=itemView.findViewById(R.id.basket_name)
        txtBasketPrice=itemView.findViewById(R.id.basket_price)
        imageBasketProduct=itemView.findViewById(R.id.basket_image)
        basketCount=itemView.findViewById(R.id.basket_count)
    }
    fun setItemClickListner(listner: ItemClickListner?) {
        this.listner = listner
    }

    override fun onClick(view: View) {
        listner!!.onClick(view, adapterPosition, false)
    }

}

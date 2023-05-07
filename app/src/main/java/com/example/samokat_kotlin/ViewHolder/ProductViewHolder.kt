package com.example.samokat_kotlin.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samokat_kotlin.ItemClickListner
import com.example.samokat_kotlin.R

class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
    var txtName:TextView
    var txtPrice:TextView
    var imageProduct:ImageView
    var listner: ItemClickListner? = null
    var addToBasket:ImageButton
    var productKol: TextView
    var productMinus: ImageButton
    init {
        txtName=itemView.findViewById(R.id.product_name)
        txtPrice=itemView.findViewById(R.id.product_price)
        imageProduct=itemView.findViewById(R.id.product_image)
        addToBasket=itemView.findViewById(R.id.product_plus)
        productKol=itemView.findViewById(R.id.product_kol)
        productMinus=itemView.findViewById(R.id.product_minus)

    }
    fun setItemClickListner(listner: ItemClickListner?) {
        this.listner = listner
    }

    override fun onClick(view: View) {
        listner!!.onClick(view, adapterPosition, false)
    }

}
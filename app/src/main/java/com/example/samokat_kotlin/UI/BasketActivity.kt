package com.example.samokat_kotlin.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samokat_kotlin.Model.Products
import com.example.samokat_kotlin.Model.Users
import com.example.samokat_kotlin.R
import com.example.samokat_kotlin.ViewHolder.BasketViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class BasketActivity : AppCompatActivity() {
    var productsRef: DatabaseReference?=null
    private var recyclerView: RecyclerView? = null
    private var layoutManager:RecyclerView.LayoutManager?=null
    lateinit var basketSum:TextView
    lateinit var basketSetOrder:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        var s="1Сумма"
        productsRef = FirebaseDatabase.getInstance().reference.child("UsersOnline").child(Users.user_phone.toString()).child("productBasket").child("Товары")
        recyclerView = findViewById<RecyclerView>(R.id.basket_rv)
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.setLayoutManager(layoutManager)
        basketSum=findViewById(R.id.basket_sum)
        basketSetOrder=findViewById(R.id.setOrder)
        basketSetOrder.setOnClickListener {
            var intent= Intent(this@BasketActivity,ClientDataActivity::class.java)
            startActivity(intent)
        }


    }

    private fun sumOutput() {
        productsRef!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var sum=snapshot.child("1Сумма").getValue().toString()
                basketSum.setText(sum)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productsRef!!, Products::class.java).build()

        val adapter: FirebaseRecyclerAdapter<Products, BasketViewHolder> =object :FirebaseRecyclerAdapter<Products, BasketViewHolder>(options) {
            override fun onBindViewHolder(holder: BasketViewHolder, i: Int, model: Products) {
                holder.txtBasketName.setText(model.getProductName())
                holder.txtBasketPrice.setText(model.getProductPrice())
                Picasso.get().load(model.getProductImage()).into(holder.imageBasketProduct)

                var RootRef: DatabaseReference? = null
                RootRef = FirebaseDatabase.getInstance().reference.child("UsersOnline").child(Users.user_phone.toString()).child("productBasket")

                RootRef.addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child("1Сумма").exists()){
                            var s=snapshot.child("1Сумма").child("Sum").getValue().toString()
                            basketSum.setText(s).toString()
                        }
                        var count=snapshot.child("Товары").child(model.getProductName().toString()).child("productCount").getValue().toString()
                        holder.basketCount.setText(count).toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.basket_item, parent, false)
                return BasketViewHolder(view)
            }
        }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }


}



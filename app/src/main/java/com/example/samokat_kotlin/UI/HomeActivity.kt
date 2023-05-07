package com.example.samokat_kotlin.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samokat_kotlin.Model.Order.basketUserMap
import com.example.samokat_kotlin.Model.Products
import com.example.samokat_kotlin.Model.Order.items_id
import com.example.samokat_kotlin.Model.Order.userDataMap
import com.example.samokat_kotlin.Model.Users
import com.example.samokat_kotlin.R
import com.example.samokat_kotlin.ViewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class HomeActivity : AppCompatActivity() {
    var productsRef: DatabaseReference?=null
    private var recyclerView: RecyclerView? = null
    private var layoutManager:RecyclerView.LayoutManager?=null
    lateinit var basketBtn: Button
    lateinit var myAccBtn:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        productsRef = FirebaseDatabase.getInstance().reference.child("Products")
        recyclerView = findViewById<RecyclerView>(R.id.recycler_menu)
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.setLayoutManager(layoutManager)
        basketBtn=findViewById(R.id.home_basket)

        basketBtn.setOnClickListener {
            val intent=Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productsRef!!, Products::class.java).build()

        val adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder> =object :FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                override fun onBindViewHolder(holder: ProductViewHolder, i: Int, model: Products) {
                    var RootRef: DatabaseReference? = null
                    RootRef = FirebaseDatabase.getInstance().getReference().child("UsersOnline").child(Users.user_phone.toString())

                    holder.txtName.setText(model.getProductName())
                    holder.txtPrice.setText(model.getProductPrice())

                    Picasso.get().load(model.getProductImage()).into(holder.imageProduct)

                    RootRef.addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.child("productBasket").child("Товары").child(model.getProductName().toString()).child("productCount").exists()){

                            var s=snapshot.child("productBasket").child("Товары")
                                .child(model.getProductName().toString())
                                .child("productCount").getValue().toString()

                                holder.productKol.text = s
                                holder.productKol.visibility = View.VISIBLE
                                holder.productMinus.visibility=View.VISIBLE
                                holder.productMinus.isEnabled=true
                            }else {
                                holder.productKol.text = "0"
                                holder.productKol.visibility = View.INVISIBLE
                                holder.productMinus.visibility=View.INVISIBLE
                                holder.productMinus.isEnabled=false
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                    holder.addToBasket.setOnClickListener{

                        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                items_id.add(model.getProductID().toString())
                                userDataMap["productName"] = model.getProductName().toString()
                                userDataMap["productPrice"] = model.getProductPrice().toString()
                                userDataMap["productImage"] = model.getProductImage().toString()
                                if(snapshot.child("productBasket").child("1Сумма").exists()){
                                    var s=snapshot.child("productBasket").child("1Сумма").child("Sum").getValue().toString().toInt()
                                    basketUserMap["Sum"]=s+model.getProductPrice().toString().toInt()
                                    RootRef.child("productBasket").child("1Сумма").updateChildren(
                                        basketUserMap)
                                }else {
                                    basketUserMap["Sum"] = model.getProductPrice().toString().toInt()
                                    RootRef.child("productBasket").child("1Сумма").updateChildren(
                                        basketUserMap)
                                }
                                for(i in items_id){
                                    if(i.equals(model.getProductID())&&snapshot.child("productBasket").child("Товары").child(model.getProductName().toString()).child("productCount").exists()){
                                        var kol=snapshot.child("productBasket").child("Товары").child(model.getProductName().toString()).child("productCount").getValue().toString().toInt()+1
                                        userDataMap["productCount"]=kol
                                    }else
                                        userDataMap["productCount"]=1
                                }

                                RootRef.child("productBasket").child("Товары").child(model.getProductName().toString()).updateChildren(userDataMap)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(this@HomeActivity, "Товар добавлен", Toast.LENGTH_SHORT).show()

                                        } else {

                                            Toast.makeText(this@HomeActivity, "Ошибка.", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                   holder.productMinus.setOnClickListener {

                       RootRef.addListenerForSingleValueEvent(object:ValueEventListener{
                           override fun onDataChange(snapshot: DataSnapshot) {
                               var kol=snapshot.child("productBasket").child("Товары").child(model.getProductName().toString()).child("productCount").getValue().toString().toInt()
                               if(kol>1){
                                   kol--
                                   userDataMap["productCount"]=kol
                                   userDataMap["productName"] = model.getProductName().toString()
                                   userDataMap["productPrice"] = model.getProductPrice().toString()
                                   userDataMap["productImage"] = model.getProductImage().toString()
                                   RootRef.child("productBasket").child("Товары").child(model.getProductName().toString()).updateChildren(userDataMap)
                               }else{
                                   RootRef.child("productBasket").child("Товары").child(model.getProductName().toString()).removeValue()
                               }

                               var s=snapshot.child("productBasket").child("1Сумма").child("Sum").getValue().toString().toInt()
                               basketUserMap["Sum"]=s-model.getProductPrice().toString().toInt()
                               RootRef.child("productBasket").child("1Сумма").updateChildren(
                                   basketUserMap)
                           }

                           override fun onCancelled(error: DatabaseError) {
                               TODO("Not yet implemented")
                           }

                       })
                   }

                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                    val view: View = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
                    return ProductViewHolder(view)
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }


}
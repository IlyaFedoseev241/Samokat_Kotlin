package com.example.samokat_kotlin.UI

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.samokat_kotlin.Model.Order
import com.example.samokat_kotlin.Model.Order.addressUserMap
import com.example.samokat_kotlin.Model.Order.basketUserMap
import com.example.samokat_kotlin.Model.Order.orderUserMap
import com.example.samokat_kotlin.Model.Order.sumUserMap
import com.example.samokat_kotlin.Model.Users
import com.example.samokat_kotlin.R
import com.google.firebase.database.*

class ClientDataActivity : AppCompatActivity() {
    private lateinit var clientAddress:EditText
    private lateinit var clientEntrance:EditText
    private lateinit var clientApartment:EditText
    private lateinit var clientIntercom:EditText
    private lateinit var clientFloor:EditText
    private lateinit var clientComment:EditText
    private lateinit var clientCheckout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_data)
        init()
        clientCheckout.setOnClickListener {
            createOrder()
        }

    }

    private fun createOrder() {
        val str_address = clientAddress.text.toString()
        val str_entrance = clientEntrance.text.toString()
        val str_apartment = clientApartment.text.toString()
        val str_intercom = clientIntercom.text.toString()
        val str_floor = clientFloor.text.toString()
        val str_comment = clientComment.text.toString()


        validateAddress(str_address,str_entrance,str_apartment,str_intercom,str_floor,str_comment)


    }

    private fun validateAddress(strAddress: String, strEntrance: String, strApartment: String, strIntercom: String, strFloor: String, strComment: String) {
        val builder=AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setMessage("Подтвердить заказ?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton("Да") { _, _ ->
            var p:Boolean=true
            strAddress.ifEmpty{
                p=false
                clientAddress.error="Укажите значение"
            }
            strEntrance.ifEmpty{
                p=false
                clientEntrance.error="Укажите значение"
            }
            strApartment.ifEmpty{
                p=false
                clientApartment.error="Укажите значение"
            }
            strIntercom.ifEmpty{
                p=false
                clientIntercom.error="Укажите значение"
            }
            strFloor.ifEmpty{
                p=false
                clientFloor.error="Укажите значение"
            }
            if(p){
                createData(strAddress,strEntrance,strApartment,strIntercom,strFloor,strComment)
                Toast.makeText(this@ClientDataActivity,"Заказ оформлен",Toast.LENGTH_SHORT).show()
                val intent= Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }

        }
        builder.setNegativeButton("Отмена") { _, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

    private fun createData(strAddress: String, strEntrance: String, strApartment: String, strIntercom: String, strFloor: String, strComment: String) {
        var RootRef: DatabaseReference? = null
        RootRef = FirebaseDatabase.getInstance().getReference().child("UsersOnline").child(Users.user_phone.toString())

        RootRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list= mutableListOf<String>()
                for(i in snapshot.child("productBasket").child("Товары").children){
                    list.add(i.key.toString())
                }

                for(i in list){
                    orderUserMap["productCount"]=snapshot.child("productBasket").child("Товары").child(i).child("productCount").getValue().toString()
                    orderUserMap["productName"]=snapshot.child("productBasket").child("Товары").child(i).child("productName").getValue().toString()
                    orderUserMap["productImage"]=snapshot.child("productBasket").child("Товары").child(i).child("productImage").getValue().toString()
                    orderUserMap["productPrice"]=snapshot.child("productBasket").child("Товары").child(i).child("productPrice").getValue().toString()

                    RootRef.child("inProgress").child("Товары").child(i).updateChildren(orderUserMap)
                }


                sumUserMap["Sum"]=snapshot.child("productBasket").child("1Сумма").child("Sum").getValue().toString().toInt()

                RootRef.child("inProgress").child("Сумма").updateChildren(sumUserMap)

                addressUserMap["Address"]=strAddress
                addressUserMap["Entrance"]=strEntrance
                addressUserMap["Apartment"]=strApartment
                addressUserMap["Intercom"]=strIntercom
                addressUserMap["Floor"]=strFloor
                addressUserMap["Comment"]=strComment

                RootRef.child("inProgress").child("Адрес").updateChildren(addressUserMap)

                RootRef.child("productBasket").removeValue()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun init() {
        clientAddress=findViewById(R.id.client_address)
        clientEntrance=findViewById(R.id.client_entrance)
        clientApartment=findViewById(R.id.client_apartment)
        clientIntercom=findViewById(R.id.client_intercom)
        clientFloor=findViewById(R.id.client_floor)
        clientComment=findViewById(R.id.client_comment)
        clientCheckout=findViewById(R.id.client_checkout)
    }

}
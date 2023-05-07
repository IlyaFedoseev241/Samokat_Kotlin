package com.example.samokat_kotlin.Authorization

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.samokat_kotlin.UI.HomeActivity
import com.example.samokat_kotlin.Model.Users
import com.example.samokat_kotlin.R
import com.google.firebase.database.*

class CodeActivity : AppCompatActivity() {
    lateinit var codeET: EditText
    lateinit var inputBtn:Button
    private var progressDialog:ProgressDialog?=null
    lateinit var strPhone:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        init()
        codeET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val string = codeET.text.toString()
                if (string.length >= 8) {
                    inputBtn.isEnabled = true
                    inputBtn.setBackgroundResource(R.drawable.code_button)
                } else {
                    inputBtn.isEnabled = false
                    inputBtn.setBackgroundResource(R.drawable.code_button_0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        inputBtn.setOnClickListener {
            loginToAcc()
        }
    }

    private fun loginToAcc() {
        strPhone.setText(intent.getStringExtra("phone"))
        val str_phone=strPhone.text.toString()
        val str_pass=codeET.text.toString()
        progressDialog!!.setTitle("Вход в аккаунт")
        progressDialog!!.setMessage("Пожалуйста подождите...")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
        ValidatePassword(str_phone,str_pass)

    }

    private fun ValidatePassword(str_phone: String,str_pass:String) {
        var RootRef: DatabaseReference? = null
        RootRef = FirebaseDatabase.getInstance().getReference()

        RootRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Users").child(str_phone).child("password").getValue()==str_pass){
                            progressDialog!!.dismiss()
                            val intent=Intent(this@CodeActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }else {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@CodeActivity, "Неверный пароль", Toast.LENGTH_SHORT).show()
                }

                }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun init(){
        codeET=findViewById(R.id.code_et)
        inputBtn=findViewById(R.id.input_btn)
        progressDialog= ProgressDialog(this)
        strPhone=findViewById(R.id.number_tv)
    }

}
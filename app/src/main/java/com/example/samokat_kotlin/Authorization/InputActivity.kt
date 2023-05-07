package com.example.samokat_kotlin.Authorization

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.samokat_kotlin.Model.Users
import com.example.samokat_kotlin.R
import com.google.firebase.database.*

class InputActivity : AppCompatActivity() {
    lateinit var phoneEditText:EditText
    lateinit var codeBtn:Button
    private var mSelfChange = false
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        phoneEditText = findViewById(R.id.input_et_phone)
        codeBtn = findViewById(R.id.input_code_btn)
        progressDialog= ProgressDialog(this)
        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0 == null || mSelfChange) {
                    return
                }

                val preparedStr = p0.replace(Regex("(\\D*)"), "")
                var resultStr = ""
                for (i in preparedStr.indices) {
                    resultStr = when (i) {
                        0 -> resultStr.plus("+7 (")
                        1 -> resultStr.plus(preparedStr[i])
                        2 -> resultStr.plus(preparedStr[i])
                        3 -> resultStr.plus(preparedStr[i])
                        4 -> resultStr.plus(") ".plus(preparedStr[i]))
                        5 -> resultStr.plus(preparedStr[i])
                        6 -> resultStr.plus(preparedStr[i])
                        7 -> resultStr.plus("-".plus(preparedStr[i]))
                        8 -> resultStr.plus(preparedStr[i])
                        9 -> resultStr.plus("-".plus(preparedStr[i]))
                        10 -> resultStr.plus(preparedStr[i])
                        else -> resultStr
                    }
                }

                mSelfChange = true
                val oldSelectionPos = phoneEditText.selectionStart
                val isEdit = phoneEditText.selectionStart != phoneEditText.length()
                phoneEditText.setText(resultStr)
                if (isEdit) {
                    phoneEditText.setSelection(if (oldSelectionPos > resultStr.length) resultStr.length else oldSelectionPos)
                } else {
                    phoneEditText.setSelection(resultStr.length)
                }
                mSelfChange = false
                 val string = phoneEditText.text.toString()
                 if (string.length == 18) {
                 codeBtn.isEnabled = true
                 codeBtn.setBackgroundResource(R.drawable.code_button)
                 } else {
                  codeBtn.isEnabled = false
                  codeBtn.setBackgroundResource(R.drawable.code_button_0)
            }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        codeBtn.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser() {
        val strPhone=phoneEditText.text.toString()
        progressDialog!!.setTitle("Выполняется проверка")
        progressDialog!!.setMessage("Подождите пожалуйста")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
        ValidateUser(strPhone)

    }

    private fun ValidateUser(strPhone: String) {
        var RootRef: DatabaseReference? = null
        RootRef = FirebaseDatabase.getInstance().getReference()

        RootRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Users").child(strPhone).exists()){
                    progressDialog!!.dismiss()
                    val passIntent=Intent(this@InputActivity, CodeActivity::class.java)
                    Users.user_phone=strPhone
                    passIntent.putExtra("phone",strPhone)
                    startActivity(passIntent)


                }else{
                    progressDialog!!.dismiss()
                    Toast.makeText(this@InputActivity,"Номер "+strPhone+" не зарегестрирован",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this@InputActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}
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
import com.example.samokat_kotlin.R
import com.google.firebase.database.*


class RegisterActivity : AppCompatActivity() {
    lateinit var registerPhone: EditText
    lateinit var registerPassword: EditText
    lateinit var registerBtn: Button
    private var mSelfChange = false
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()



        registerPhone.addTextChangedListener(object : TextWatcher {
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
                val oldSelectionPos = registerPhone.selectionStart
                val isEdit = registerPhone.selectionStart != registerPhone.length()
                registerPhone.setText(resultStr)
                if (isEdit) {
                    registerPhone.setSelection(if (oldSelectionPos > resultStr.length) resultStr.length else oldSelectionPos)
                } else {
                    registerPhone.setSelection(resultStr.length)
                }
                mSelfChange = false
                val str_password = registerPassword.text.toString()
                val str_phone = registerPhone.text.toString()
                if (str_phone.length == 18 && str_password.length >= 8) {
                    registerBtn.isEnabled = true
                    registerBtn.setBackgroundResource(R.drawable.code_button)
                } else {
                    registerBtn.isEnabled = false
                    registerBtn.setBackgroundResource(R.drawable.code_button_0)
                }


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        registerPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val str_password = registerPassword.text.toString()
                val str_phone = registerPhone.text.toString()
                if (str_phone.length == 18 && str_password.length >= 8) {
                    registerBtn.isEnabled = true
                    registerBtn.setBackgroundResource(R.drawable.code_button)
                } else {
                    registerBtn.isEnabled = false
                    registerBtn.setBackgroundResource(R.drawable.code_button_0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        registerBtn.setOnClickListener {
            createAccount()
        }
    }

    fun init() {
        registerPassword = findViewById(R.id.register_password)
        progressDialog = ProgressDialog(this)
        registerPhone = findViewById(R.id.register_phone)
        registerBtn = findViewById(R.id.register_btn)
    }

    private fun createAccount() {
        val str_password = registerPassword.text.toString()
        val str_phone = registerPhone.text.toString()

        progressDialog!!.setTitle("Создание аккаунта")
        progressDialog!!.setMessage("Пожалуйста, подождите...")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()

        validatePhone(str_phone, str_password)

    }

    private fun validatePhone(strPhone: String, strPassword: String) {
        var RootRef: DatabaseReference? = null
        RootRef = FirebaseDatabase.getInstance().getReference()

        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(snapshot.child("Users").child(strPhone).exists())) {
                    val userDataMap = HashMap<String, Any>()
                    userDataMap["phone"] = strPhone
                    userDataMap["password"] = strPassword
                    RootRef.child("Users").child(strPhone).updateChildren(userDataMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressDialog!!.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Регистрация прошла успешно.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val loginIntent =
                                    Intent(this@RegisterActivity, InputActivity::class.java)
                                startActivity(loginIntent)
                            } else {
                                progressDialog!!.dismiss()
                                Toast.makeText(this@RegisterActivity, "Ошибка.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@RegisterActivity, "Номер " + strPhone + " уже существует", Toast.LENGTH_SHORT).show()

                    val loginIntent = Intent(this@RegisterActivity, InputActivity::class.java)
                    startActivity(loginIntent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}
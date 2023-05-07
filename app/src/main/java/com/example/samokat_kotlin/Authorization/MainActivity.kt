package com.example.samokat_kotlin.Authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.samokat_kotlin.R

class MainActivity : AppCompatActivity() {
    private lateinit var inputBtn: Button
    private lateinit var registerBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputBtn=findViewById(R.id.main_input)
        registerBtn=findViewById(R.id.main_address)
        inputBtn.setOnClickListener {
            val intent=Intent(this, InputActivity::class.java)
            startActivity(intent)
        }
        registerBtn.setOnClickListener {
            val intent=Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.example.samokat_kotlin

import android.view.View

interface ItemClickListner {
    fun onClick(view: View,position:Int,isLongClick: Boolean)
}
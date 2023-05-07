package com.example.samokat_kotlin.Model

object Order {
    var userDataMap = HashMap<String, Any>()
    var items_id= mutableListOf<String>()
    var basketUserMap= HashMap<String, Any>()
    var orderUserMap=HashMap<String, Any>()
    var sumUserMap=HashMap<String, Any>()
    var addressUserMap=HashMap<String, Any>()
}
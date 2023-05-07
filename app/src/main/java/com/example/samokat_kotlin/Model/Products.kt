package com.example.samokat_kotlin.Model

import android.net.Uri

class Products {
    private var productName:String?=null
    private var productPrice:String?=null
    private var productImage: String?=null
    private var productID: String?=null

    constructor() {}
    constructor(
        productName:String?,
        productPrice:String?,
        productImage:String?,
        productID:String?
    ) {

        this.productName=productName
        this.productPrice=productPrice
        this.productImage=productImage
        this.productID=productID
    }


    fun getProductName(): String? {
        return productName
    }

    fun setProductName(productName: String?) {
        this.productName = productName
    }

    fun getProductPrice(): String? {
        return productPrice
    }

    fun setProductPrice(productPrice: String?) {
        this.productPrice = productPrice
    }
    fun getProductImage(): String? {
        return productImage
    }

    fun setProductImage(productImage: String?) {
        this.productImage = productImage
    }
    fun getProductID(): String? {
        return productID
    }

    fun setProductID(productID: String?) {
        this.productID = productID
    }
}
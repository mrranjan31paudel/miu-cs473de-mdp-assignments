package edu.miu.cs473de.lab5.electronicsrecyclerview

import java.io.Serializable

data class Product(val productName: String, val productDescription: String, val productPrice: Double, val imgSrc: Int, val brandLogoSrc: Int): Serializable {
}

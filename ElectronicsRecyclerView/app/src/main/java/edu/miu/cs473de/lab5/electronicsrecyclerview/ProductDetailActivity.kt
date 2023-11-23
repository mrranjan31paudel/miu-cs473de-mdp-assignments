package edu.miu.cs473de.lab5.electronicsrecyclerview

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import edu.miu.cs473de.lab5.electronicsrecyclerview.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityProductDetailBinding
    private var product: Product? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        product = intent.getSerializableExtra("product", Product::class.java)
        populateActivity(product)
    }

    private fun populateActivity(product: Product?) {
        if (product == null) {
            Toast.makeText(this, "Can't load product!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewBinding.productImage.setImageResource(product.imgSrc)
        viewBinding.productName.text = product.productName
        viewBinding.productDescription.text = product.productDescription
        viewBinding.productPrice.text = "$" + product.productPrice.toString()
    }

    fun onHomeClick(view: View) {
        finish()
    }
}
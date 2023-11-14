package edu.miu.cs473de.lab5.electronicsrecyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ProductListRecyclerViewAdapter(val context: AppCompatActivity, val products: ArrayList<Product>, val cart: HashMap<String, Int>): RecyclerView.Adapter<ProductListRecyclerViewAdapter.ProductListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListRecyclerViewAdapter.ProductListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductListRecyclerViewAdapter.ProductListViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener { view ->
            val productDetailIntent = Intent(context, ProductDetailActivity::class.java)
            productDetailIntent.putExtra("product", products[position])
            context.startActivity(productDetailIntent)
        }
        holder.productImage.setImageResource(products[position].imgSrc)
        holder.brandLogo.setImageResource(products[position].brandLogoSrc)
        holder.productName.text = products[position].productName
        holder.productPrice.text = "$ ${products[position].productPrice}"
        holder.productDescription.text = products[position].productDescription
        holder.addButton.setOnClickListener { view ->
            if (cart.containsKey(products[position].productName)) {
                val count = cart[products[position].productName]?:0
                cart[products[position].productName] = count + 1
            }
            else {
                cart[products[position].productName] = 1
            }
            Toast.makeText(context, "Added ${products[position].productName} to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView
        var brandLogo: ImageView
        var productName: TextView
        var productPrice: TextView
        var productDescription: TextView
        var addButton: Button

        init {
            productImage = itemView.findViewById(R.id.product_image) as ImageView
            brandLogo = itemView.findViewById(R.id.brand_logo) as ImageView
            productName = itemView.findViewById(R.id.product_name) as TextView
            productPrice = itemView.findViewById(R.id.product_price) as TextView
            productDescription = itemView.findViewById(R.id.product_description) as TextView
            addButton = itemView.findViewById(R.id.add_to_cart_button) as Button
        }
    }
}
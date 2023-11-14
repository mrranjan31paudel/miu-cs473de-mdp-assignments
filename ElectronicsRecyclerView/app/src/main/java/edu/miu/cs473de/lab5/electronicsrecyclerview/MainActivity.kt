package edu.miu.cs473de.lab5.electronicsrecyclerview

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import edu.miu.cs473de.lab5.electronicsrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val cart = HashMap<String, Int>()

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val products = initializeProducts()

        val adapter = ProductListRecyclerViewAdapter(this, products, cart)

        viewBinding.productRcv.layoutManager = LinearLayoutManager(this)
        viewBinding.productRcv.adapter = adapter
    }

    private fun initializeProducts(): ArrayList<Product> {
        val products = ArrayList<Product>()
        products.add(Product("iPad", "iPad Pro 11-inch", 400.0, R.drawable.ipad_pro_11_inch, R.drawable.apple_logo))
        products.add(Product("MacBook M3 Pro", "12-core CPU\n18-core GPU", 2500.00, R.drawable.mac_m3_pro, R.drawable.apple_logo))
        products.add(Product("Dell Inspiron", "13th Gen Intel® Core™ i7", 1499.00, R.drawable.dell_insperion, R.drawable.dell_logo))
        products.add(Product("Logitech Keyboard", "Logitech - PRO X\nTKL LIGHTSPEED Wireless", 199.00, R.drawable.logitech_keyboard, R.drawable.logitech_logo))
        products.add(Product("MacBook M3 Max", "14-core CPU\n30-core GPU", 3499.00, R.drawable.mac_m3_max, R.drawable.apple_logo))
        return products
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onViewCart(view: View) {
        val dialogBuilder = AlertDialog.Builder(this)
        var cartString =
            cart.entries.joinToString("\n") { mutableEntry -> mutableEntry.key + ": " + mutableEntry.value }
        dialogBuilder.setTitle("Cart")
        dialogBuilder.setMessage(cartString)
        dialogBuilder.setPositiveButton("OK") {dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
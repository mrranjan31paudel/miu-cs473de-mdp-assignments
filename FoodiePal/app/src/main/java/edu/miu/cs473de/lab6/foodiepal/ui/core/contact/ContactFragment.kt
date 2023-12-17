package edu.miu.cs473de.lab6.foodiepal.ui.core.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentContactBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {

    private lateinit var viewBinding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        viewBinding = FragmentContactBinding.bind(view)

        val tel = getString(R.string.contact_tel)
        val email = getString(R.string.contact_email)
        viewBinding.callButton.text = "Call: ${formatPhoneNumber(tel)}"
        viewBinding.emailButton.text = "Email: $email"

        viewBinding.callButton.setOnClickListener { openPhoneDial(tel) }
        viewBinding.emailButton.setOnClickListener { openEmail(email) }

        return view
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return "+1 (${phoneNumber.subSequence(0, 3)}) ${phoneNumber.subSequence(3, 6)}-${phoneNumber.subSequence(6, 10)}"
    }

    private fun openPhoneDial(tel: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$tel")
        try {
            startActivity(dialIntent)
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Oops, something went wrong!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }

    private fun openEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        try {
            startActivity(emailIntent)
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Oops, something went wrong!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ContactFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
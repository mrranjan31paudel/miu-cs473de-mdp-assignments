package edu.miu.cs473de.lab6.foodiepal.ui.core.blog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentBlogReadDialogBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BlogReadDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogReadDialogFragment(val blog: Blog?) : DialogFragment() {

    private lateinit var viewBinding: FragmentBlogReadDialogBinding

    override fun getTheme(): Int {
        return R.style.Theme_FoodiePal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blog_read_dialog, container, false)

        viewBinding = FragmentBlogReadDialogBinding.bind(view)
        viewBinding.blogReadTitle.text = blog?.title ?: ""
        viewBinding.blogReadAuthor.text = "${blog?.authorName ?: ""} [${blog?.authorEmail ?: ""}]"
        viewBinding.blogReadPostedAt.text = blog?.postedAtString ?: ""
        viewBinding.blogReadContent.text = blog?.content ?: ""
        viewBinding.blogReadCloseButton.setOnClickListener { dismiss() }

        return view
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = MaterialAlertDialogBuilder(this.requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
//        builder
//            .setView(viewBinding.root)
//            .setBackgroundInsetStart(0)
//            .setBackgroundInsetEnd(0)
//            .setBackgroundInsetTop(0)
//            .setBackgroundInsetBottom(0)
//
//        return builder.create()
//    }

    companion object {
        val TAG = "blog_read"
    }
}
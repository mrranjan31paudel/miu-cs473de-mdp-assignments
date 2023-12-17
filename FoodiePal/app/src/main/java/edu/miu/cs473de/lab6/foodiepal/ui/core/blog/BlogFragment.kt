package edu.miu.cs473de.lab6.foodiepal.ui.core.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters.BlogRecyclerViewAdapter
import edu.miu.cs473de.lab6.foodiepal.listeners.OnBlogItemViewClickListener
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentBlogBinding
import edu.miu.cs473de.lab6.foodiepal.service.blog.BlogService

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment() {

    private lateinit var viewBinding: FragmentBlogBinding
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var blogs: ArrayList<Blog>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blog, container, false)

        viewBinding = FragmentBlogBinding.bind(view)
        blogRecyclerView = viewBinding.blogsRecyclerView
        blogRecyclerView.layoutManager = LinearLayoutManager(activity)
        blogs = initializeBlogs()
        blogRecyclerView.adapter = BlogRecyclerViewAdapter(blogs, object:
            OnBlogItemViewClickListener {
            override fun onBlogViewClick(blog: Blog, position: Int) {
                openFullScreenBlogReaderDialog(blog)
            }
        })

        viewBinding.addNewBlogButton.setOnClickListener{ v ->
            openNewBlogFormDialog()
        }

        return view
    }

    private fun updateListWithNewBlog(blogId: Int) {
        val blog = BlogService.getBlogById(blogId) ?: return
        blogs.add(0, blog)
        blogRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun openNewBlogFormDialog() {
        val fragmentManager = activity?.supportFragmentManager ?: return

        val preFragment = fragmentManager.findFragmentByTag(NewBlogDialogFragment.TAG)
        if (preFragment != null) return

        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val newRecipeDialogFragment = NewBlogDialogFragment.newInstance()
        newRecipeDialogFragment.show(transaction, NewBlogDialogFragment.TAG)

        fragmentManager.setFragmentResultListener("on_creation_success", viewLifecycleOwner) {requestKey, bundle ->
            val newBlogId = bundle.getInt("newBlogId")
            Snackbar.make(viewBinding.root, "Blog posted successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            if (newBlogId > 0) {
                updateListWithNewBlog(newBlogId)
            }
        }
    }

    private fun openFullScreenBlogReaderDialog(blog: Blog) {
        var blogDetails: Blog? = null

        try {
            blogDetails = BlogService.getBlogDetailsById(blog.id)

            if (blogDetails == null) {
                Snackbar.make(viewBinding.root, "Blog post does not exist!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.red_1))
                    .setActionTextColor(resources.getColor(R.color.white))
                    .show()
                return
            }
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to load the blog!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            return
        }

        val fragmentManager = activity?.supportFragmentManager ?: return

        val preFragment = fragmentManager.findFragmentByTag(BlogReadDialogFragment.TAG)
        if (preFragment != null) return

        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val newRecipeDialogFragment = BlogReadDialogFragment(blogDetails)
        newRecipeDialogFragment.show(transaction, BlogReadDialogFragment.TAG)
    }

    private fun initializeBlogs(): ArrayList<Blog> {
        return try {
            BlogService.getAllBlogs()
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to load blogs!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            ArrayList()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment BlogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            BlogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
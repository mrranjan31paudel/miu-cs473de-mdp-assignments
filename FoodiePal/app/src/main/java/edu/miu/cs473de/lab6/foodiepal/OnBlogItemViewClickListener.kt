package edu.miu.cs473de.lab6.foodiepal

import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog

interface OnBlogItemViewClickListener {
    fun onBlogViewClick(blog: Blog, position: Int)
}
package edu.miu.cs473de.lab6.foodiepal.service

import android.os.Build
import androidx.annotation.RequiresApi
import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog
import edu.miu.cs473de.lab6.foodiepal.data.blog.BlogEntity
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.format.DateTimeFormatter

class BlogService {

    companion object {

        private fun validateBlog(title: String, content: String) {
            if (title.isEmpty() || title.length < 5 || title.length > 200) {
                throw ValidationException("blogTitle", "Required with at least 5 and at most 200 characters!")
            }

            if (content.isEmpty() || content.length < 10 || content.length > 1000) {
                throw ValidationException("blogContent", "Required with at least 10 and at most 1000 characters!")
            }
        }

        fun getAllBlogs(): ArrayList<Blog> {
            val blogDao = DatabaseService.db?.blogDao()
            val blogs = runBlocking {
                return@runBlocking blogDao?.getAllBlogs()
            } ?: return ArrayList()

            return blogs as ArrayList
        }

        fun getBlogById(id: Int): Blog? {
            val blogDao = DatabaseService.db?.blogDao()
            return runBlocking {
                return@runBlocking blogDao?.getBlogById(id)
            }
        }

        fun getBlogDetailsById(id: Int): Blog? {
            val blogDao = DatabaseService.db?.blogDao()
            return runBlocking {
                return@runBlocking blogDao?.getBlogDetailsById(id)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getCurrentTimestampInSeconds(): Long {
            return System.currentTimeMillis() / 1000
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun postNewBlog(title: String, content: String, authorId: Int): Int {
            validateBlog(title, content)

            val blog = BlogEntity(0, title, content, authorId, getCurrentTimestampInSeconds())
            val blogDao = DatabaseService.db?.blogDao()
            val blogId = runBlocking {
                return@runBlocking blogDao?.createBlog(blog)
            }

            return (blogId ?: 0L).toInt()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun updateBlog(title: String, content: String, blog: Blog) {
            validateBlog(title, content)

            blog.title = title
            blog.content = content
            blog.postedAt = getCurrentTimestampInSeconds()

            val blogDao = DatabaseService.db?.blogDao()
            runBlocking {
                blogDao?.updateBlog(blog.toBlogEntity())
            }
        }

        fun deleteBlog(blog: Blog) {
            val blogDao = DatabaseService.db?.blogDao()
            runBlocking {
                blogDao?.deleteBlog(blog.toBlogEntity())
            }
        }
    }
}
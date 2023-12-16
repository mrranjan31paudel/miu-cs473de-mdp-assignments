package edu.miu.cs473de.lab6.foodiepal.db.blog

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog
import edu.miu.cs473de.lab6.foodiepal.data.blog.BlogEntity

@Dao
interface BlogDao {
    @Query("SELECT b.id as id, b.title as title, '' AS content, b.authorId as authorId, b.postedAt as postedAt, (u.first_name || ' ' || u.last_name) AS authorName, '' AS authorEmail FROM Blog b LEFT JOIN User u ON b.authorId=u.id ORDER BY b.postedAt DESC")
    suspend fun getAllBlogs(): List<Blog>?

    @Query("SELECT b.id as id, b.title as title, '' AS content, b.authorId as authorId, b.postedAt as postedAt, (u.first_name || ' ' || u.last_name) AS authorName, '' AS authorEmail FROM Blog b LEFT JOIN User u ON b.authorId=u.id WHERE b.id=:id")
    suspend fun getBlogById(id: Int): Blog?

    @Query("SELECT b.*, (u.first_name || ' ' || u.last_name) AS authorName, u.email AS authorEmail FROM Blog b LEFT JOIN User u ON b.authorId=u.id WHERE b.id=:id")
    suspend fun getBlogDetailsById(id: Int): Blog?

    @Insert
    suspend fun createBlog(blogEntity: BlogEntity): Long

    @Update
    suspend fun updateBlog(blogEntity: BlogEntity)

    @Delete
    suspend fun deleteBlog(blogEntity: BlogEntity)
}
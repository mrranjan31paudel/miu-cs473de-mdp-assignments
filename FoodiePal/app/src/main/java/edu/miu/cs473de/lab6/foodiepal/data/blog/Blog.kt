package edu.miu.cs473de.lab6.foodiepal.data.blog

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class Blog(
    var id: Int,
    var title: String,
    var content: String,
    var authorId: Int,
    var postedAt: Long,
    var authorName: String,
    var authorEmail: String
) {
    var postedAtString: String? = ""
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val zoneId = ZoneId.systemDefault()
            val date = Instant.ofEpochSecond(postedAt).atZone(zoneId).toLocalDateTime()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a")
            return date.format(formatter)
        }

    fun toBlogEntity(): BlogEntity = BlogEntity(id, title, content, authorId, postedAt)
}

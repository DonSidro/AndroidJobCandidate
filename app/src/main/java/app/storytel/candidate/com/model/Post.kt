package app.storytel.candidate.com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class holding information/data about the posts
 */
@Entity(tableName = "posts")
data class Post(
    val userId: Int,
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String
)

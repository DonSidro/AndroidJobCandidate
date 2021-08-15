package app.storytel.candidate.com.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Data class holding information/data about the comments posted
 */

@Entity(tableName = "comments")
data class Comment(
    val postId: Int,
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

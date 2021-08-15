package app.storytel.candidate.com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class holding information/data about photos posted
 */
@Entity(tableName = "photos")
data class Photo(
    val albumId: Int,
    @PrimaryKey
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)

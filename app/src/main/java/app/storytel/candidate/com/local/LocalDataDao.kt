package app.storytel.candidate.com.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post

@Dao
interface LocalDataDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<Post>

    @Query("SELECT * FROM photos WHERE id = :id LIMIT 1")
    fun getPhotoById(id: Int): Photo

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): List<Photo>

    @Query("SELECT * FROM comments WHERE postId = :id")
    fun getCommentsById(id: Int): List<Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComments(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPhotos(photos: List<Photo>)


}
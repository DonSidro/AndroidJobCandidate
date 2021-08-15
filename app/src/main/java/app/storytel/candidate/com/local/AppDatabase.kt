package app.storytel.candidate.com.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post


@Database(entities = [Comment::class, Photo::class, Post::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun localDao(): LocalDataDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "appDatabase")
                .fallbackToDestructiveMigration()
                .build()
    }
}
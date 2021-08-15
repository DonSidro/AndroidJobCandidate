package app.storytel.candidate.com.util

sealed class Resource<out T : Any> {
    class Success<out T : Any>(val data: T) : Resource<T>()
    class Loading<out T : Any>() : Resource<T>()
    class Error(val error: Throwable?) : Resource<Nothing>()
}
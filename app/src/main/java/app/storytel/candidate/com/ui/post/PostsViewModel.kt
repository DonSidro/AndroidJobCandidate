package app.storytel.candidate.com.ui.post


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.local.LocalDataDao
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.network.ApiService
import app.storytel.candidate.com.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val databaseDao: LocalDataDao
) : ViewModel() {


    private val postsAndImages = MutableLiveData<Resource<PostAndImages>>().apply {
        this.value = Resource.Loading()
    }

    fun getPostsAndImages(): MutableLiveData<Resource<PostAndImages>> {
        loadLocal()
        return postsAndImages
    }


    fun onRefreshWeb() {
        fetchRemoteRepo()
    }

    fun onRefreshLocal() {
        loadLocal()
    }

    private fun loadLocal() {
        postsAndImages.postValue(Resource.Loading())
        viewModelScope.launch {
            when (val responsePost = getPostFromLocal()) {
                is Resource.Success -> {
                    when (val responsePhoto = getPhotoFromLocal()) {
                        is Resource.Success -> {
                            postsAndImages.postValue(
                                Resource.Success(
                                    PostAndImages(
                                        responsePost.data,
                                        responsePhoto.data
                                    )
                                )
                            )
                        }
                    }
                }
                is Resource.Error -> fetchRemoteRepo()
            }
        }
    }

    private fun fetchRemoteRepo() {
        postsAndImages.postValue(Resource.Loading())
        viewModelScope.launch {
            when (val responsePost = getPosts()) {
                is Resource.Success -> {
                    when (val responsePhoto = getPhotos()) {
                        is Resource.Success -> {
                            postsAndImages.postValue(
                                Resource.Success(
                                    PostAndImages(
                                        responsePost.data,
                                        responsePhoto.data
                                    )
                                )
                            )
                            refreshRepoList(PostAndImages(responsePost.data, responsePhoto.data))

                        }
                        is Resource.Error -> postsAndImages.postValue(Resource.Error(responsePhoto.error))

                    }
                }
                is Resource.Error -> postsAndImages.postValue(Resource.Error(responsePost.error))
            }
        }
    }


    private fun refreshRepoList(postAndImages: PostAndImages) {
        viewModelScope.launch {
            insertPosts(postAndImages.posts)
            insertPhotos(postAndImages.photos)
        }
    }

    private suspend fun getPosts(): Resource<List<Post>> {
        return try {
            Resource.Success(apiService.getPosts())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    private suspend fun getPhotos(): Resource<List<Photo>> {
        return try {
            Resource.Success(apiService.getPhotos())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    suspend fun getPostFromLocal(): Resource<List<Post>> =
        withContext(Dispatchers.IO) {
            try {
                val data = databaseDao.getAllPosts()
                if (data.isNullOrEmpty()) {
                    Resource.Error(null)
                } else {
                    Resource.Success(data)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Resource.Error(ex)
            }
        }

    suspend fun getPhotoFromLocal(): Resource<List<Photo>> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(databaseDao.getAllPhotos())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Resource.Error(ex)
            }
        }

    suspend fun insertPhotos(data: List<Photo>) =
        withContext(Dispatchers.IO) {
            try {
                databaseDao.insertAllPhotos(data)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    suspend fun insertPosts(data: List<Post>) =
        withContext(Dispatchers.IO) {
            try {
                databaseDao.insertAllPosts(data)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

}
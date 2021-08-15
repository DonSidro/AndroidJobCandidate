package app.storytel.candidate.com.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.local.LocalDataDao
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.network.ApiService
import app.storytel.candidate.com.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val databaseDao: LocalDataDao
) : ViewModel() {

    var postID: Int = -1

    private val comments = MutableLiveData<Resource<List<Comment>>>().apply {
        this.value = Resource.Loading()
    }

    private val photo = MutableLiveData<Resource<Photo>>()

    fun getPhotoData(): MutableLiveData<Resource<Photo>> {
        loadLocalPhoto()
        return photo
    }

    fun getCommentLiveData(postID: Int): MutableLiveData<Resource<List<Comment>>> {
        this.postID = postID
        loadLocal()
        return comments
    }

    private fun loadLocal() {
        comments.postValue(Resource.Loading())
        viewModelScope.launch {
            when (val responseComments = getCommentsFromLocal()) {
                is Resource.Success -> {
                    comments.postValue(Resource.Success(responseComments.data))
                }
                is Resource.Error -> fetchRemoteRepo()
            }
        }
    }

    private fun fetchRemoteRepo() {
        comments.postValue(Resource.Loading())
        viewModelScope.launch {
            when (val responseComments = getComments()) {
                is Resource.Success -> {
                    refreshRepoList(responseComments.data)
                    comments.postValue(Resource.Success(responseComments.data))
                }
                is Resource.Error -> comments.postValue(Resource.Error(responseComments.error))
            }
        }
    }

    private suspend fun getComments(): Resource<List<Comment>> {
        return try {
            Resource.Success(apiService.getComments(postID))
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

    private fun refreshRepoList(list: List<Comment>) {
        viewModelScope.launch {
            databaseDao.insertAllComments(list)
        }
    }

    private suspend fun getCommentsFromLocal(): Resource<List<Comment>> =
        withContext(Dispatchers.IO) {
            try {
                val data = databaseDao.getCommentsById(postID)
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

    private fun loadLocalPhoto() {
        photo.postValue(Resource.Loading())
        viewModelScope.launch {
            when (val responsePhoto = loadPhotoFromDB()) {
                is Resource.Success -> {
                    photo.postValue(Resource.Success(responsePhoto.data))
                }
                is Resource.Error -> photo.postValue(Resource.Error(responsePhoto.error))
            }
        }
    }

    private suspend fun loadPhotoFromDB(): Resource<Photo> =
        withContext(Dispatchers.IO) {
            try {
                val data = databaseDao.getPhotoById(postID)
                Resource.Success(data)
            } catch (ex: Exception) {
                ex.printStackTrace()
                Resource.Error(ex)
            }
        }


}
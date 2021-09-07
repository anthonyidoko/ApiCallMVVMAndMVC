package com.example.mvvmarchitecture.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.AddPostData
import com.example.mvvmarchitecture.data.CommentsDataClass
import com.example.mvvmarchitecture.data.PostsDataClass
import com.example.mvvmarchitecture.data.PostsDataClassItem
import com.example.mvvmarchitecture.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException

class PostsViewModel :ViewModel() {

    private val mutablePostList = MutableLiveData<PostsDataClass>()
    val immutablePostList : LiveData<PostsDataClass>
        get() = mutablePostList

    lateinit var searchLiveDataList :ArrayList<PostsDataClassItem>

//    val count = 100

    private val mutableArrayListOfPosts = MutableLiveData<ArrayList<PostsDataClassItem>>()
    private val immutablePostsArrayList = mutableArrayListOfPosts
    private val mutableListCommentsLiveData = MutableLiveData<CommentsDataClass>()
    val immutableListCommentsLiveData = mutableListCommentsLiveData

    private val _liveDataAddedPostList = MutableLiveData<Response<AddPostData>>()
    val liveDataAddedPostList :LiveData<Response<AddPostData>> = _liveDataAddedPostList

    fun makeAPICall(){
        viewModelScope.launch (Dispatchers.IO){
            try{

                val response = RetrofitClient.createRetroClient().getPosts()
                mutablePostList.postValue(response)

            }catch (e :UnknownHostException){
                e.printStackTrace()
            }

        }

    }

    fun makeSecondAPICall(id :String){
        viewModelScope.launch (Dispatchers.IO){
            val response = RetrofitClient.createRetroClient().getPostComments(id)
            mutableListCommentsLiveData.postValue(response)
        }

    }

    fun makeNewPost(post : AddPostData) {
        viewModelScope.launch (Dispatchers.IO){
            val response = RetrofitClient.createRetroClient().makePost(post)
            _liveDataAddedPostList.postValue(response)
        }

    }

    fun populatePostsArrayList(){
        if (immutablePostsArrayList.value!!.isNotEmpty()){
            immutablePostList.value?.forEach {post ->
                searchLiveDataList.add(post)
            }
        }
    }


}


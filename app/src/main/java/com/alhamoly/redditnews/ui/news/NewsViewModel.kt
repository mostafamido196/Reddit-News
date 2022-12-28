package com.alhamoly.redditnews.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alhamoly.redditnews.data.remote.NewsServices
import com.alhamoly.redditnews.utils.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.alhamoly.redditnews.ui.base.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) :ViewModel(){

    private val _getNewsStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val getNewsStateFlow: MutableStateFlow<NetworkState> get() = _getNewsStateFlow

    fun getNews() {

        _getNewsStateFlow.value = NetworkState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                Log.d(TAG,"send token: ${sharedHelper.token}")
                repository.getNews(_getNewsStateFlow)
            } catch (e: Exception) {
                Log.d(TAG, "getNews: ${e.message}")
            }
        }

    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}
class NewsRepository @Inject constructor( private val newsServices: NewsServices) : BaseRepository() {

    suspend fun getNews(stateFlow: MutableStateFlow<NetworkState>) {

        Log.d(TAG, "getNews: ")
        runApi(
            stateFlow,
            newsServices.getNews()
        )

    }

    companion object {
        private const val TAG = "NewsRepository"
    }
}
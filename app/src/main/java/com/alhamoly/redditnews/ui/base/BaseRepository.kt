package com.alhamoly.redditnews.ui.base

import android.util.Log
import com.alhamoly.redditnews.utils.Constants
import com.alhamoly.redditnews.utils.Utils
import com.alhamoly.redditnews.utils.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

open class BaseRepository /*@Inject constructor()*/ {

    fun <T> runApi(
        _apiStateFlow: MutableStateFlow<NetworkState>,
        block: Response<T>
    ) {
        Log.e(TAG, "runApi:  start")

        _apiStateFlow.value = NetworkState.Loading
        try {
            if (Utils.isInternetAvailable()) {
                Log.e(TAG, "runApi: isInternetAvailable")
                CoroutineScope(Dispatchers.IO).launch {

                    kotlin.runCatching {
                        block
                    }.onFailure {

                        Log.e(TAG, "runApi: 3")
                        when (it) {
                            is java.net.UnknownHostException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)
                            is java.net.ConnectException ->
                                _apiStateFlow.value =
                                    NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)
                            else -> _apiStateFlow.value =
                                NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }

                    }.onSuccess {

                        Log.e(TAG, "runApi: 4")
                        if (it.body() != null)
                            _apiStateFlow.value = NetworkState.Result(it.body())
                        else {
                            Log.e(TAG, "runApi: ${it.errorBody()}")
                            _apiStateFlow.value = NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        }
                    }

                }
            } else {
                Log.e(TAG, "runApi: no isInternetAvailable")
                _apiStateFlow.value = NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)
            }
        } catch (e: Exception) {
            Log.e(TAG, "runApi: ${e.message}")
        }
        Log.e(TAG, "runApi: end")

    }

    companion object {
        private const val TAG = "BaseRepository"
    }
}
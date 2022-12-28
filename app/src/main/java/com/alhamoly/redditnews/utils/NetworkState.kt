package com.alhamoly.redditnews.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.alhamoly.redditnews.R
import com.alhamoly.redditnews.ui.base.BaseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


sealed class NetworkState {

    //idle
    object Idle : NetworkState()

    //loading
    object Loading : NetworkState()

    //result
    data class Result<T>(var response: T) : NetworkState()

    //error
    data class Error(var errorCode: Int, var msg: String? = null) : NetworkState() {

        fun handleErrors(
            @ApplicationContext
            mContext: Context,
            mDialogsListener: (() -> Unit)? = null
        ) {

/*
            val mFragmentManager: FragmentManager =
                if (mContext is ViewComponentManager.FragmentContextWrapper) {
                    (mContext.baseContext as AppCompatActivity).supportFragmentManager
                } else {
                    try {
                        (mContext as AppCompatActivity).supportFragmentManager
                    } catch (e: Exception) {
                        (mContext as FragmentActivity).supportFragmentManager
                    }
                }
*/

            Log.e(TAG, "handleErrors: msg $msg")
            Log.e(TAG, "handleErrors: error code $errorCode")

            when (errorCode) {

                Constants.Codes.EXCEPTIONS_CODE -> {
                    showHelperDialog(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.internet_connection) else msg!!,
                        mContext = mContext,
                        mDialogsListener = mDialogsListener
                    )
                }
                Constants.Codes.API_KEY_CODE -> {
                    showHelperDialog(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.x_api_key_error) else msg!!,
                        mContext = mContext,
                        mDialogsListener = {
                            mDialogsListener
//                            viewModel.signOut()
//                            viewModel.startAuth()
                        }
                    )
                }
                Constants.Codes.AUTH_CODE -> {
                    showHelperDialog(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.auth_error) else msg!!,
                        mContext = mContext,
                        mDialogsListener = {
                            mDialogsListener
//                            viewModel.signOut()
//                            viewModel.startAuth()
                        }
                    )
                }
                else -> {
                    showHelperDialog(
                        msg = if (msg.isNullOrEmpty()) mContext.getString(R.string.known_error) else msg!!,
                        mContext = mContext,
                        mDialogsListener = mDialogsListener
                    )
                }
            }

        }

        private fun showHelperDialog(
            msg: String,
            mContext: Context,
            mDialogsListener: (() -> Unit)?
        ) {
            Log.d(TAG, "showHelperDialog: ")
            mDialogsListener?.let { code ->
                code()
            }
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }

        companion object {
            private val TAG = this::class.java.name
        }

    }

}

package com.alhamoly.redditnews.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


class SharedHelper @Inject constructor(@ApplicationContext mContext: Context) {

    private val sharedPreferences =
        mContext.getSharedPreferences(Constants.ShardKeys.SHARD_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private val cashingSharedPreferences =
        mContext.getSharedPreferences(Constants.ShardKeys.DB, Context.MODE_PRIVATE)
    private val cashingEditor = cashingSharedPreferences.edit()
//
//    fun cashing(list: List<ContractInfo>) {
//
//        val allData = list + getCashingData()
//
//        val f = HashMap<String, ContractInfo>()
//
//        allData.forEach {
//            f[it.contractAddress] = it
//        }
//
//        val e = f.values.toList()
//
//        val gson = Gson()
//        val json = gson.toJson(e)
//
//        cashingEditor.putString(Constants.DATA, json)
//        cashingEditor.commit()
//
//        Log.e("TAG", "cashing: ${e.size}")
//    }
//
//    fun getCashingData(): List<ContractInfo> {
//
//        var arrayItems: List<ContractInfo> = ArrayList()
//        val serializedObject = cashingSharedPreferences.getString(Constants.DATA, null)
//        if (serializedObject != null) {
//            val gson = Gson()
//            val type: Type = object : TypeToken<List<ContractInfo?>?>() {}.type
//            arrayItems = gson.fromJson(serializedObject, type)
//        }
//
//        Log.e("TAG", "cashing00: ${arrayItems.size}")
//        return arrayItems
//    }

    val lang: String
        get() = sharedPreferences.getString(
            Constants.ShardKeys.LANGUAGE,
            Locale.getDefault().language
        )!!

    private val splash: Boolean
        get() = sharedPreferences.getBoolean(
            Constants.ShardKeys.SPLASH,
            false
        )

//    val token: String
//        get() = "${
//            sharedPreferences.getString(
//                Constants.ShardKeys.TOKEN,
//                ""
//            )
//        }"
////${Constants.ApisKeys.BEARER}
//    fun userData(): User? = Gson().fromJson(
//        sharedPreferences.getString(Constants.ShardKeys.DATA, null),
//        User::class.java
//    )


//    fun setUserData(value: User/*, token: String*/) {
//        val data = Gson().toJson(value, User::class.java)
//        editor.putString(Constants.ShardKeys.DATA, data)
////        editor.putString(Constants.ShardKeys.TOKEN, token)
//        editor.apply()
//    }

    fun setUserToken(token: String) {
        editor.putString(Constants.ShardKeys.TOKEN, token)
        editor.apply()
    }

    fun saveSplash() {
        editor.putBoolean(Constants.ShardKeys.SPLASH, true)
        editor.apply()
    }

    fun isSplashed(): Boolean = splash

    fun signOut() {
        editor.clear()!!.apply()
    }

    fun isDarkModeEnabled(): Boolean =
        sharedPreferences.getBoolean(Constants.ShardKeys.DARK_MODE, false)

    fun setDarkModeEnabled(is_dark: Boolean) {
        editor.putBoolean(Constants.ShardKeys.DARK_MODE, is_dark)
        editor.apply()
    }

    fun saveLang(lang: String) {
        editor.putString(Constants.ShardKeys.LANGUAGE, lang)
        editor.apply()
    }

    companion object {
//        private val TAG = this::class.java.name
    }

    enum class Languages(val lang: String) {
        EN("en"), AR("ar"), RU("ru")
    }
}
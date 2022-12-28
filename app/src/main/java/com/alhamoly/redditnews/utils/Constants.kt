package com.alhamoly.redditnews.utils

object Constants {


    //    const val BASE_URL = "https://api.develocity.finance/api/v1/"
    const val BASE_URL = "https://www.reddit.com/r/"



    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val Shortcut: String = "shortcut"
//    const val SCORE: String = "score"
    const val DATA = "data"

    const val SEARCH_KEYWORD = "search_keyword"
    const val DETAILS: String = "details"
    const val URL: String = "url"
    const val TITLE: String = "title"

    object ApisKeys {
        const val BEARER = "Bearer"
    }

    object ShardKeys {

        const val SHARD_NAME = "Develocity"
        const val DB = "Develocity db"
        const val DATA = "data"
        const val TOKEN = "token"
        const val DARK_MODE = "mode"
        const val LANGUAGE = "lang"
        const val SPLASH = "splash"

    }

    object Codes {
        const val EXCEPTIONS_CODE = 3421
        const val API_KEY_CODE = 5020
        const val AUTH_CODE = 5000
        const val UNKNOWN_CODE = 560
        const val SUCCESSES_CODE = 200
        const val UN_SUCCESSES_CODE = 220
    }
}
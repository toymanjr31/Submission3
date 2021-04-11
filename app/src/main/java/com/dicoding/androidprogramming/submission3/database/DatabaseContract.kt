package com.dicoding.androidprogramming.submission3.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.dicoding.androidprogramming.submission3"
    const val SCHEME = "content"

    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "Favorite"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FAVORITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME).build()
        }
    }
}
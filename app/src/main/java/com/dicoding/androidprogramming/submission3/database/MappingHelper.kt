package com.dicoding.androidprogramming.submission3.database

import android.database.Cursor
import com.dicoding.androidprogramming.submission3.entity.Favorite

object MappingHelper {
    fun mapCursorToArrayList(favsCursor: Cursor?): ArrayList<Favorite>{
        val favsList = ArrayList<Favorite>()
        favsCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAVORITE))
                favsList.add(Favorite(username, name, avatar, company, location, repository, favorite))
            }
        }
        return favsList
    }

}
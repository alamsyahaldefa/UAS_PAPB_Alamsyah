package com.example.uas_papb.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uas_papb.data_class.Favorite
import com.example.uas_papb.data_class.User

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun Insert(favorite: Favorite)

    @Delete
    fun Delete(favorite: Favorite)

    @Query("SELECT * FROM FAVORITE WHERE username = :username ORDER BY id ASC")
    fun getUserFavorite(username : String): LiveData<List<Favorite>>


}
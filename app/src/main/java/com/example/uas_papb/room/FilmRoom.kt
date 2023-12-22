package com.example.uas_papb.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.uas_papb.data_class.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FilmRoom : RoomDatabase(){
    abstract fun FavoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FilmRoom? = null

        fun getDatabase(context: Context): FilmRoom? {
            if (INSTANCE==null) {
                synchronized(FilmRoom::class.java){
                    INSTANCE=databaseBuilder(context.applicationContext, FilmRoom::class.java, "favorite_db").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}

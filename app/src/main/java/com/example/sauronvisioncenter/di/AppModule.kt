package com.example.sauronvisioncenter.di

import android.app.Application
import androidx.room.Room
import com.example.sauronvisioncenter.database.AppDatabase

object AppModule {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun provideDatabase(application: Application): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "sauron_vision_center_database"
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}
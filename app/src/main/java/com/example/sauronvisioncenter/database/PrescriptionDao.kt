package com.example.sauronvisioncenter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sauronvisioncenter.model.PrescriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Insert
    suspend fun insert(prescription: PrescriptionEntity): Long

    @Query("SELECT * FROM prescriptions ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PrescriptionEntity>>

    @Update
    suspend fun update(prescription: PrescriptionEntity)
}
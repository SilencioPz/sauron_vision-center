package com.example.sauronvisioncenter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val patientName: String = "",
    val lastCheckup: String = "",

    val rightEyeSphere: String = "",
    val rightEyeCylinder: String = "",
    val rightEyeAxis: String = "",
    val rightEyeAV: String = "",

    val leftEyeSphere: String = "",
    val leftEyeCylinder: String = "",
    val leftEyeAxis: String = "",
    val leftEyeAV: String = "",

    val addition: String = "",
    val lensType: String = "",
    val lensColor: String = "",

    val observations: String = "",

    val createdAt: Long = System.currentTimeMillis()
)
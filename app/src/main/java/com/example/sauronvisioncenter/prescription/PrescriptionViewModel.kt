package com.example.sauronvisioncenter.prescription

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sauronvisioncenter.database.AppDatabase
import com.example.sauronvisioncenter.model.PrescriptionData
import com.example.sauronvisioncenter.model.PrescriptionEntity
import kotlinx.coroutines.launch
import com.example.sauronvisioncenter.util.PdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.String

class PrescriptionViewModel(
    private val db: AppDatabase
) : ViewModel() {

    private val _formState = MutableStateFlow(PrescriptionData())
    val formState: StateFlow<PrescriptionData> = _formState.asStateFlow()

    fun updateName(name: String) = _formState.update { it.copy(patientName = name) }
    fun updateLastCheckup(date: String) = _formState.update { it.copy(lastCheckup = date) }
    fun updateRightEyeSphere(value: String) = _formState.update { it.copy(rightEyeSphere = value) }
    fun updateRightEyeCylinder(value: String) = _formState.update { it.copy(rightEyeCylinder = value) }
    fun updateRightEyeAxis(value: String) = _formState.update { it.copy(rightEyeAxis = value) }
    fun updateRightEyeAV(value: String) = _formState.update { it.copy(rightEyeAV = value) }
    fun updateLeftEyeSphere(value: String) = _formState.update { it.copy(leftEyeSphere = value) }
    fun updateLeftEyeCylinder(value: String) = _formState.update { it.copy(leftEyeCylinder = value) }
    fun updateLeftEyeAxis(value: String) = _formState.update { it.copy(leftEyeAxis = value) }
    fun updateLeftEyeAV(value: String) = _formState.update { it.copy(leftEyeAV = value) }
    fun updateAddition(value: String) = _formState.update { it.copy(addition = value) }
    fun updateLensType(value: String) = _formState.update { it.copy(lensType = value) }
    fun updateLensColor(value: String) = _formState.update { it.copy(lensColor = value) }
    fun updateObservations(observations: String) = _formState.update { it.copy(observations = observations) }

    fun resetForm() {
        _formState.update {
            PrescriptionData()
        }
    }

    fun savePrescription(context: Context) {
        viewModelScope.launch {
            val current = formState.value
            val prescription = PrescriptionEntity(
                patientName = current.patientName,
                lastCheckup = current.lastCheckup,
                rightEyeSphere = current.rightEyeSphere,
                rightEyeCylinder = current.rightEyeCylinder,
                rightEyeAxis = current.rightEyeAxis,
                rightEyeAV = current.rightEyeAV,
                leftEyeSphere = current.leftEyeSphere,
                leftEyeCylinder = current.leftEyeCylinder,
                leftEyeAxis = current.leftEyeAxis,
                leftEyeAV = current.leftEyeAV,
                addition = current.addition,
                lensType = current.lensType,
                lensColor = current.lensColor,
                observations = current.observations
            )
            db.prescriptionDao().insert(prescription)

            val pdfUri = PdfGenerator(context).generatePrescriptionPdf(prescription)
            pdfUri?.let { uri ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        flags =
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Salvo no banco e PDF gerado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Dados salvos, mas erro ao abrir PDF: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
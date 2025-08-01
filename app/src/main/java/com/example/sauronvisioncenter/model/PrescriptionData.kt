package com.example.sauronvisioncenter.model

data class PrescriptionData(
    // Seção de Identificação
    var patientName: String = "",
    var lastCheckup: String = "",

    // Dados da Prescrição
    var rightEyeSphere: String = "",
    var rightEyeCylinder: String = "",
    var rightEyeAxis: String = "",
    var rightEyeAV: String = "",

    var leftEyeSphere: String = "",
    var leftEyeCylinder: String = "",
    var leftEyeAxis: String = "",
    var leftEyeAV: String = "",

    var addition: String = "",
    var lensType: String = "",
    var lensColor: String = "",

    var observations: String = ""
)
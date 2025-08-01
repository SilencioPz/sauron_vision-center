package com.example.sauronvisioncenter.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PhysicalSignatureRequirement(
    requiresPhysicalSignature: Boolean,
    onRequirementChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = requiresPhysicalSignature,
            onCheckedChange = { checked ->
                if (checked) {
                    showDialog = true
                } else {
                    onRequirementChanged(false)
                }
            }
        )
        Text(
            text = "Requer assinatura física",
            modifier = Modifier.padding(start = 8.dp)
        )
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
                onRequirementChanged(false)
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Assinatura Física Necessária",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Esta prescrição requer uma assinatura física do paciente para ser válida. " +
                                "O documento deverá ser assinado presencialmente antes da finalização do processo.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                showDialog = false
                                onRequirementChanged(false)
                            }
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                showDialog = false
                                onRequirementChanged(true)
                            }
                        ) {
                            Text("Entendi")
                        }
                    }
                }
            }
        }
    }
}
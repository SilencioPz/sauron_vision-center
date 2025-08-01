package com.example.sauronvisioncenter.prescription

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sauronvisioncenter.components.DatePickerField
import com.example.sauronvisioncenter.ui.theme.SauronVisionCenterTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.platform.LocalContext
import com.example.sauronvisioncenter.R
import com.example.sauronvisioncenter.database.AppDatabase

@Composable
fun PrescriptionForm(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: PrescriptionViewModel
) {
    val formState by viewModel.formState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.not_sauron),
                    contentDescription = "Logo",
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "SauronVisionCenter",
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.SansSerif)
                )
            }

            OutlinedTextField(
                value = formState.patientName,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nome completo") },
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                label = "Data",
                date = formState.lastCheckup,
                onDateSelected = { viewModel.updateLastCheckup(it) }
            )

            Text("Prescrição Óptica", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Olho Direito (OD)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = formState.rightEyeSphere,
                        onValueChange = { viewModel.updateRightEyeSphere(it) },
                        label = { Text("ESF") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.rightEyeCylinder,
                        onValueChange = { viewModel.updateRightEyeCylinder(it) },
                        label = { Text("CIL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.rightEyeAxis,
                        onValueChange = { viewModel.updateRightEyeAxis(it) },
                        label = { Text("EIXO") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.rightEyeAV,
                        onValueChange = { viewModel.updateRightEyeAV(it) },
                        label = { Text("AV") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Olho Esquerdo (OE)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = formState.leftEyeSphere,
                        onValueChange = { viewModel.updateLeftEyeSphere(it) },
                        label = { Text("ESF") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.leftEyeCylinder,
                        onValueChange = { viewModel.updateLeftEyeCylinder(it) },
                        label = { Text("CIL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.leftEyeAxis,
                        onValueChange = { viewModel.updateLeftEyeAxis(it) },
                        label = { Text("EIXO") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = formState.leftEyeAV,
                        onValueChange = { viewModel.updateLeftEyeAV(it) },
                        label = { Text("AV") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            OutlinedTextField(
                value = formState.addition,
                onValueChange = { viewModel.updateAddition(it) },
                label = { Text("ADD") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.lensType,
                onValueChange = { viewModel.updateLensType(it) },
                label = { Text("Tipo de lente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.lensColor,
                onValueChange = { viewModel.updateLensColor(it) },
                label = { Text("Cor da lente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.observations,
                onValueChange = { viewModel.updateObservations(it) },
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 6,
                maxLines = 10
            )

            // Botão para gerar PDF
            Button(
                onClick = { viewModel.savePrescription(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar no Banco de dados e Gerar PDF")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Limpar")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Limpar Formulário")
            }

            if (showResetDialog) {
                AlertDialog(
                    onDismissRequest = { showResetDialog = false },
                    title = { Text("Confirmar") },
                    text = { Text("Deseja realmente limpar todos os campos?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.resetForm()
                                showResetDialog = false
                                Toast.makeText(
                                    context,
                                    "Formulário resetado", Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        ) {
                            Text("Limpar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showResetDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun PrescriptionFormPreview() {
    SauronVisionCenterTheme {
        val context = LocalContext.current
        PrescriptionForm(
            viewModel = PrescriptionViewModel(db = AppDatabase.createInMemory(context))
        )
    }
}
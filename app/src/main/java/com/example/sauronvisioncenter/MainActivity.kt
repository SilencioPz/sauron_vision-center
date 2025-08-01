package com.example.sauronvisioncenter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sauronvisioncenter.database.AppDatabase
import com.example.sauronvisioncenter.prescription.PrescriptionForm
import com.example.sauronvisioncenter.prescription.PrescriptionViewModel
import com.example.sauronvisioncenter.di.AppModule
import com.example.sauronvisioncenter.ui.theme.SauronVisionCenterTheme
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private lateinit var prescriptionViewModel: PrescriptionViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                this,
                "Permissão necessária para salvar PDFs",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions()

        val database = AppModule.provideDatabase(application)
        prescriptionViewModel = PrescriptionViewModel(database)

        setContent {
            SauronVisionCenterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "prescription",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("prescription") {
                            PrescriptionForm(
                                viewModel = prescriptionViewModel
                            )
                        }
                    }
                }
            }
        }
    }
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionsToRequest = mutableListOf<String>()

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (permissionsToRequest.isNotEmpty()) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            viewModel = PrescriptionViewModel(
                db = AppDatabase.createInMemory(context)
            )
        )
    }
}
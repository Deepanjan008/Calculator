package com.deepanjan.neocalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelCostScreen(navController: NavController) {
    var distance by remember { mutableStateOf("") }
    var fuelPrice by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var resultCost by remember { mutableStateOf("") }
    var resultFuel by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fuel Cost Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = distance, onValueChange = { distance = it; showError = false },
                label = { Text("Distance (km)") }, isError = showError && distance.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = fuelPrice, onValueChange = { fuelPrice = it; showError = false },
                label = { Text("Fuel Price (per liter)") }, isError = showError && fuelPrice.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = mileage, onValueChange = { mileage = it; showError = false },
                label = { Text("Mileage (km/liter)") }, isError = showError && mileage.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val d = distance.toDoubleOrNull(); val p = fuelPrice.toDoubleOrNull(); val m = mileage.toDoubleOrNull()
                    if (d != null && p != null && m != null && m > 0) {
                        resultFuel = String.format("%.2f Liters", d / m)
                        resultCost = String.format("₹ %.2f", (d / m) * p)
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Calculate Cost", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultCost.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Estimated Cost", style = MaterialTheme.typography.titleMedium)
                        Text(text = resultCost, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "Fuel Required: $resultFuel", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

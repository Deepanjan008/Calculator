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
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentScreen(navController: NavController) {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Annually") }
    var resultAmount by remember { mutableStateOf("") }
    var resultInterest by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val frequencies = listOf("Annually", "Semi-Annually", "Quarterly", "Monthly")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Investment Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = principal, onValueChange = { principal = it; showError = false },
                label = { Text("Principal Amount") }, isError = showError && principal.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = rate, onValueChange = { rate = it; showError = false },
                label = { Text("Annual Interest Rate (%)") }, isError = showError && rate.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = time, onValueChange = { time = it; showError = false },
                label = { Text("Time (years)") }, isError = showError && time.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            var exp by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
                OutlinedTextField(value = frequency, onValueChange = {}, readOnly = true, label = { Text("Compounding Frequency") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(exp) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(16.dp))
                ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                    frequencies.forEach { f -> DropdownMenuItem(text = { Text(f) }, onClick = { frequency = f; exp = false }) }
                }
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    val p = principal.toDoubleOrNull(); val r = rate.toDoubleOrNull(); val t = time.toDoubleOrNull()
                    if (p != null && r != null && t != null) {
                        val n = when (frequency) { "Semi-Annually" -> 2.0; "Quarterly" -> 4.0; "Monthly" -> 12.0; else -> 1.0 }
                        val amount = p * (1 + r / (100 * n)).pow(n * t)
                        resultAmount = String.format("₹ %,.2f", amount)
                        resultInterest = String.format("₹ %,.2f", amount - p)
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Calculate Returns", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultAmount.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Maturity Value", style = MaterialTheme.typography.titleMedium)
                        Text(text = resultAmount, fontSize = 38.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "Interest Earned: $resultInterest", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

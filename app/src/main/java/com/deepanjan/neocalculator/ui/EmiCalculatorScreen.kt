package com.deepanjan.neocalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmiCalculatorScreen(navController: NavController) {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var tenure by remember { mutableStateOf("") }
    var emiResult by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EMI Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = principal,
                onValueChange = { principal = it },
                label = { Text("Loan Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Annual Interest Rate (%)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tenure,
                onValueChange = { tenure = it },
                label = { Text("Tenure (months)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                try {
                    val p = principal.toDouble()
                    val r = rate.toDouble() / 12 / 100
                    val n = tenure.toDouble()
                    val emi = p * r * (1 + r).pow(n) / ((1 + r).pow(n) - 1)
                    emiResult = String.format(Locale.US, "₹%.2f per month", emi)
                } catch (e: Exception) {
                    emiResult = "Please enter valid numbers"
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Calculate EMI")
            }

            if (emiResult.isNotBlank()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = emiResult,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

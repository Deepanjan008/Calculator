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
fun CurrencyScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("INR") }
    var result by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val rates = mapOf("USD" to 1.0, "EUR" to 0.92, "GBP" to 0.79, "INR" to 83.0, "JPY" to 150.0)
    val currencies = rates.keys.toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Currency Converter") },
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
                value = amount,
                onValueChange = { amount = it; showError = false },
                label = { Text("Amount to Convert") },
                isError = showError && amount.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(20.dp))

            // Dropdowns (Simplified for space)
            Row(modifier = Modifier.fillMaxWidth()) {
                var expFrom by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expFrom, onExpandedChange = { expFrom = !expFrom }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = fromCurrency, onValueChange = {}, readOnly = true, label = { Text("From") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expFrom) }, modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expFrom, onDismissRequest = { expFrom = false }) {
                        currencies.forEach { c -> DropdownMenuItem(text = { Text(c) }, onClick = { fromCurrency = c; expFrom = false }) }
                    }
                }
                Spacer(Modifier.width(12.dp))
                var expTo by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expTo, onExpandedChange = { expTo = !expTo }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = toCurrency, onValueChange = {}, readOnly = true, label = { Text("To") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expTo) }, modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expTo, onDismissRequest = { expTo = false }) {
                        currencies.forEach { c -> DropdownMenuItem(text = { Text(c) }, onClick = { toCurrency = c; expTo = false }) }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (amt != null) {
                        val converted = amt * (rates[toCurrency]!! / rates[fromCurrency]!!)
                        result = String.format("%.2f %s", converted, toCurrency)
                        showError = false
                        keyboardController?.hide()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) { Text("Convert Currency", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (result.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Converted Amount", style = MaterialTheme.typography.titleMedium)
                        Text(text = result, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

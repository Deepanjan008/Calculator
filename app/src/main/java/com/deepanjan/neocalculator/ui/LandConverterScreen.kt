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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandConverterScreen(navController: NavController) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Decimal") }
    var toUnit by remember { mutableStateOf("Katha") }
    var resultText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val units = listOf("Decimal", "Katha", "Bigha", "Acre", "Shotok")
    val toDecimal = mapOf("Decimal" to 1.0, "Katha" to 1.653, "Bigha" to 33.057, "Acre" to 100.0, "Shotok" to 1.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Land Converter") },
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
                value = inputValue, onValueChange = { inputValue = it; showError = false },
                label = { Text("Value to Convert") }, isError = showError && inputValue.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                var expFrom by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expFrom, onExpandedChange = { expFrom = !expFrom }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = fromUnit, onValueChange = {}, readOnly = true, label = { Text("From") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expFrom) }, modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expFrom, onDismissRequest = { expFrom = false }) {
                        units.forEach { u -> DropdownMenuItem(text = { Text(u) }, onClick = { fromUnit = u; expFrom = false }) }
                    }
                }
                Spacer(Modifier.width(12.dp))
                var expTo by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expTo, onExpandedChange = { expTo = !expTo }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = toUnit, onValueChange = {}, readOnly = true, label = { Text("To") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expTo) }, modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expTo, onDismissRequest = { expTo = false }) {
                        units.forEach { u -> DropdownMenuItem(text = { Text(u) }, onClick = { toUnit = u; expTo = false }) }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val value = inputValue.toDoubleOrNull()
                    if (value != null) {
                        val decimal = value * (toDecimal[fromUnit] ?: 1.0)
                        val converted = decimal / (toDecimal[toUnit] ?: 1.0)
                        resultText = String.format(Locale.US, "%.4f %s", converted, toUnit)
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Convert Land Area", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultText.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Converted Area", style = MaterialTheme.typography.titleMedium)
                        Text(text = resultText, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        }
    }
}

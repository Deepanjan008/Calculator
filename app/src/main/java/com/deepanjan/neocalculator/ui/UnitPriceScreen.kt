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
fun UnitPriceScreen(navController: NavController) {
    var p1 by remember { mutableStateOf("") }; var w1 by remember { mutableStateOf("") }
    var p2 by remember { mutableStateOf("") }; var w2 by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Price Comparator") },
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
            Text("Product 1", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start).padding(top = 16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = p1, onValueChange = { p1 = it; showError = false }, label = { Text("Price") }, isError = showError && p1.isEmpty(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(value = w1, onValueChange = { w1 = it; showError = false }, label = { Text("Weight") }, isError = showError && w1.isEmpty(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Product 2", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = p2, onValueChange = { p2 = it; showError = false }, label = { Text("Price") }, isError = showError && p2.isEmpty(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(value = w2, onValueChange = { w2 = it; showError = false }, label = { Text("Weight") }, isError = showError && w2.isEmpty(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val pr1 = p1.toDoubleOrNull(); val wt1 = w1.toDoubleOrNull(); val pr2 = p2.toDoubleOrNull(); val wt2 = w2.toDoubleOrNull()
                    if (pr1 != null && wt1 != null && pr2 != null && wt2 != null) {
                        val u1 = pr1 / wt1; val u2 = pr2 / wt2
                        resultText = if (u1 < u2) "Product 1 is a better deal" else if (u2 < u1) "Product 2 is a better deal" else "Both are equal deals"
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Compare Deals", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultText.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text(text = resultText, modifier = Modifier.padding(24.dp).fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

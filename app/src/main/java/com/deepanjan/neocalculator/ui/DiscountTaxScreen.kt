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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountTaxScreen(navController: NavController) {
    var originalPrice by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("") }
    var taxPercent by remember { mutableStateOf("") }
    var resultAmount by remember { mutableStateOf("") }
    var resultSavings by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discount & Tax Calculator") },
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
                value = originalPrice, onValueChange = { originalPrice = it; showError = false },
                label = { Text("Original Price") }, isError = showError && originalPrice.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = discountPercent, onValueChange = { discountPercent = it; showError = false },
                    label = { Text("Discount (%)") }, isError = showError && discountPercent.isEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = taxPercent, onValueChange = { taxPercent = it; showError = false },
                    label = { Text("Tax (%)") }, isError = showError && taxPercent.isEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val price = originalPrice.toDoubleOrNull()
                    val disc = discountPercent.toDoubleOrNull() ?: 0.0
                    val tax = taxPercent.toDoubleOrNull() ?: 0.0
                    if (price != null) {
                        val afterDiscount = price * (1 - disc / 100)
                        val final = afterDiscount * (1 + tax / 100)
                        resultAmount = String.format(Locale.US, "₹%.2f", final)
                        resultSavings = String.format(Locale.US, "₹%.2f", price - afterDiscount)
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Calculate Price", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultAmount.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Final Price", style = MaterialTheme.typography.titleMedium)
                        Text(text = resultAmount, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "Total Discount: $resultSavings", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

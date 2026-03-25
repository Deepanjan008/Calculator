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
fun BmiHealthScreen(navController: NavController) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var resultBmi by remember { mutableStateOf("") }
    var resultBmr by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BMI/Health Tracker") },
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
                value = weight, onValueChange = { weight = it; showError = false },
                label = { Text("Weight (kg)") }, isError = showError && weight.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = height, onValueChange = { height = it; showError = false },
                label = { Text("Height (cm)") }, isError = showError && height.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = age, onValueChange = { age = it; showError = false },
                label = { Text("Age") }, isError = showError && age.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilterChip(selected = isMale, onClick = { isMale = true }, label = { Text("Male") })
                Spacer(Modifier.width(12.dp))
                FilterChip(selected = !isMale, onClick = { isMale = false }, label = { Text("Female") })
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    val w = weight.toDoubleOrNull(); val h = height.toDoubleOrNull(); val a = age.toIntOrNull()
                    if (w != null && h != null && a != null) {
                        val bmi = w / ((h / 100).pow(2))
                        val bmr = if (isMale) 88.362 + (13.397 * w) + (4.799 * h) - (5.677 * a)
                        else 447.593 + (9.247 * w) + (3.098 * h) - (4.330 * a)
                        resultBmi = String.format("%.2f", bmi)
                        resultBmr = String.format("%.0f kcal", bmr)
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Calculate Health Score", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultBmi.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("BMI Score", style = MaterialTheme.typography.titleMedium)
                        Text(text = resultBmi, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(0.1f))
                        Spacer(Modifier.height(12.dp))
                        Text("Daily BMR: $resultBmr", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

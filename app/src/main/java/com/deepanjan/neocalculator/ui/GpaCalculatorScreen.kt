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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaCalculatorScreen(navController: NavController) {
    var subjectsInput by remember { mutableStateOf("4") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val grades = remember { mutableStateListOf<String>().apply { repeat(10) { add("") } } }
    val credits = remember { mutableStateListOf<String>().apply { repeat(10) { add("") } } }
    var result by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPA/CGPA Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = subjectsInput,
                onValueChange = { subjectsInput = it; showError = false },
                label = { Text("Number of Subjects (Max 10)") },
                isError = showError && subjectsInput.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(24.dp))

            val count = subjectsInput.toIntOrNull()?.coerceAtMost(10) ?: 0
            repeat(count) { index ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = grades[index],
                        onValueChange = { grades[index] = it; showError = false },
                        label = { Text("Grade ${index + 1}") },
                        isError = showError && grades[index].isEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.width(12.dp))
                    OutlinedTextField(
                        value = credits[index],
                        onValueChange = { credits[index] = it; showError = false },
                        label = { Text("Credits ${index + 1}") },
                        isError = showError && credits[index].isEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    var totalPoints = 0.0
                    var totalCredits = 0.0
                    var isValid = true

                    for (i in 0 until count) {
                        val g = grades[i].toDoubleOrNull()
                        val c = credits[i].toDoubleOrNull()
                        if (g != null && c != null) {
                            totalPoints += g * c
                            totalCredits += c
                        } else {
                            isValid = false
                        }
                    }

                    if (isValid && totalCredits > 0) {
                        result = String.format("%.2f", totalPoints / totalCredits)
                        showError = false
                        keyboardController?.hide() // কিবোর্ড হাইড হবে
                    } else {
                        showError = true
                        result = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Calculate GPA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            if (result.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Your GPA Result", style = MaterialTheme.typography.titleMedium)
                        Text(text = result, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

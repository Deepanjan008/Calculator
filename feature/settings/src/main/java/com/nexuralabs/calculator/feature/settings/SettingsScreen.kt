package com.nexuralabs.calculator.feature.settings

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()

    val precision by viewModel.precision.collectAsState(initial = 6)
    val hapticEnabled by viewModel.hapticEnabled.collectAsState(initial = true)
    val currentColorHex by viewModel.themeColorHex.collectAsState(initial = "#BB86FC")

    var hexInput by remember(currentColorHex) { mutableStateOf(currentColorHex) }
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- CUSTOMIZE APPEARANCE SECTION ---
            Text("Customize Appearance", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))

            Text("Hue Selection (Slide to rotate color)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            val hue = (change.position.x / size.width).coerceIn(0f, 1f) * 360f
                            val colorInt = android.graphics.Color.HSVToColor(floatArrayOf(hue, 0.6f, 0.9f))
                            val newHex = String.format("#%06X", 0xFFFFFF and colorInt)
                            viewModel.setThemeColorHex(newHex)
                        }
                    }
            ) {
                val colors = List(361) { Color.hsv(it.toFloat(), 1f, 1f) }
                drawRect(brush = Brush.horizontalGradient(colors))
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = hexInput,
                onValueChange = { input ->
                    hexInput = input
                    if (input.length == 7 && input.startsWith("#")) {
                        viewModel.setThemeColorHex(input)
                    }
                },
                label = { Text("Theme Hex Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(try { Color(android.graphics.Color.parseColor(currentColorHex)) } catch (e: Exception) { Color.Gray })
                    )
                }
            )

            Spacer(Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(Modifier.height(24.dp))

            // --- DECIMAL PRECISION SECTION ---
            Text("Decimal Precision", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = precision.toFloat(),
                onValueChange = { newVal -> viewModel.setPrecision(newVal.toInt()) },
                valueRange = 0f..10f,
                steps = 9
            )
            Text("Current: $precision digits")

            Spacer(Modifier.height(24.dp))

            // --- HAPTIC FEEDBACK SECTION ---
            ListItem(
                headlineContent = { Text("Haptic Feedback") },
                trailingContent = {
                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = { isChecked -> viewModel.setHapticEnabled(isChecked) }
                    )
                }
            )

            Spacer(Modifier.height(32.dp))

            // --- DANGER ZONE SECTION ---
            Button(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Clear All History", fontSize = 16.sp)
            }

            if (showClearDialog) {
                AlertDialog(
                    onDismissRequest = { showClearDialog = false },
                    title = { Text("Clear History?") },
                    text = { Text("This will delete all history entries forever.") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.clearAllHistory()
                            showClearDialog = false
                        }) { Text("Confirm", color = MaterialTheme.colorScheme.error) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

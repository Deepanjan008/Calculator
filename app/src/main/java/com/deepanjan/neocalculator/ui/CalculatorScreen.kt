package com.deepanjan.neocalculator.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.deepanjan.neocalculator.viewmodel.CalculatorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalculatorScreen(navController: NavController) {
    val viewModel: CalculatorViewModel = hiltViewModel()
    val context = LocalContext.current
    val expression by viewModel.expression.collectAsState()
    val preview by viewModel.preview.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()

    var showProTools by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val previewFontSize = when {
        preview.length <= 7 -> 72.sp
        preview.length <= 10 -> 52.sp
        preview.length <= 14 -> 38.sp
        else -> 28.sp
    }

    val expressionFontSize = when {
        expression.length <= 12 -> 42.sp
        expression.length <= 20 -> 32.sp
        else -> 24.sp
    }

    if (showProTools) {
        ModalBottomSheet(
            onDismissRequest = { showProTools = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Pro Tools", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { scope.launch { sheetState.hide() }; showProTools = false }) { Icon(Icons.Default.Close, "Close") }
                }
                HorizontalDivider()
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(proTools) { tool -> ProToolCard(tool, navController) { showProTools = false } }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calculator") },
                actions = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(28.dp)
                            .combinedClickable(
                                onClick = { navController.navigate("history") },
                                onLongClick = { Toast.makeText(context, "History", Toast.LENGTH_SHORT).show() }
                            )
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(28.dp)
                            .combinedClickable(
                                onClick = { navController.navigate("settings") },
                                onLongClick = { Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() }
                            )
                    )
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Unit Converter",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(28.dp)
                            .combinedClickable(
                                onClick = { navController.navigate("unit_converter") },
                                onLongClick = { Toast.makeText(context, "Unit Converter", Toast.LENGTH_SHORT).show() }
                            )
                    )
                    Icon(
                        imageVector = Icons.Default.Widgets,
                        contentDescription = "Pro Tools",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(28.dp)
                            .combinedClickable(
                                onClick = { showProTools = true },
                                onLongClick = { Toast.makeText(context, "Pro Tools", Toast.LENGTH_SHORT).show() }
                            )
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Column(modifier = Modifier.weight(2.2f).fillMaxWidth(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = expression.ifEmpty { "0" },
                        fontSize = expressionFontSize,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState())
                    )
                    IconButton(onClick = { viewModel.onButtonClick("⌫") }) {
                        Icon(Icons.AutoMirrored.Filled.Backspace, "Delete", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Text(
                    text = preview.ifEmpty { "0" },
                    fontSize = previewFontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                )
                Spacer(Modifier.height(40.dp))
            }

            LazyVerticalGrid(columns = GridCells.Fixed(4), contentPadding = PaddingValues(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(3.8f)) {
                items(keypadButtons) { btn ->
                    KeypadButton(btn) {
                        if (hapticEnabled) {
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                        }
                        viewModel.onButtonClick(btn.command)
                    }
                }
            }
        }
    }
}

private val keypadButtons = listOf(
    KeypadButton("%", "%"), KeypadButton("^", "^"), KeypadButton("√", "√"), KeypadButton("÷", "÷"),
    KeypadButton("7", "7"), KeypadButton("8", "8"), KeypadButton("9", "9"), KeypadButton("×", "×"),
    KeypadButton("4", "4"), KeypadButton("5", "5"), KeypadButton("6", "6"), KeypadButton("−", "−"),
    KeypadButton("1", "1"), KeypadButton("2", "2"), KeypadButton("3", "3"), KeypadButton("+", "+"),
    KeypadButton("0", "0"), KeypadButton(".", "."), KeypadButton("C", "C"), KeypadButton("=", "=")
)

data class KeypadButton(val display: String, val command: String)

@Composable
private fun KeypadButton(btn: KeypadButton, onClick: () -> Unit) {
    val isOperator = btn.display in listOf("÷", "×", "−", "+", "=", "%", "^", "√")
    Button(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isOperator) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isOperator) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(btn.display, fontSize = 28.sp, fontWeight = FontWeight.Medium)
    }
}

private val proTools = listOf(
    ProTool(Icons.Default.Cake, "Age Calculator", "age"),
    ProTool(Icons.Default.Calculate, "EMI Calculator", "emi"),
    ProTool(Icons.Default.School, "GPA/CGPA", "gpa"),
    ProTool(Icons.Default.CurrencyExchange, "Currency Converter", "currency"),
    ProTool(Icons.Default.LocalGasStation, "Fuel Cost", "fuel"),
    ProTool(Icons.Default.HealthAndSafety, "BMI/Health", "bmi"),
    ProTool(Icons.Default.CompareArrows, "Unit Price", "unit_price"),
    ProTool(Icons.Default.AttachMoney, "Investment", "investment"),
    ProTool(Icons.Default.ShoppingCart, "Discount/Tax", "discount"),
    ProTool(Icons.Default.Landscape, "Land Converter", "land"),
    ProTool(Icons.Default.Functions, "Equation Solver", "solver"),
    ProTool(Icons.Default.PriorityHigh, "Factorial (!)", "factorial")
)

data class ProTool(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProToolCard(tool: ProTool, navController: NavController, onDismiss: () -> Unit) {
    Card(
        onClick = { navController.navigate(tool.route); onDismiss() },
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(tool.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
            Spacer(Modifier.height(8.dp))
            Text(tool.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

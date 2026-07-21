package com.nexuralabs.calculator.feature.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactorialScreen(navController: NavController) {
    var input by remember { mutableStateOf("") }
    var fullResult by remember { mutableStateOf("") }
    var scientificResult by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCalculating by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val displayChunks = remember(fullResult) {
        if (fullResult.isEmpty()) emptyList()
        else if (fullResult.length > 2000) fullResult.chunked(1000)
        else listOf(fullResult)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Factorial Calculator") },
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 7)) {
                        input = newValue
                        errorMessage = null
                    }
                },
                label = { Text("Enter a number") },
                supportingText = {
                    if (errorMessage != null) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Max 100,000")
                    }
                },
                isError = errorMessage != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    val n = input.toIntOrNull()

                    when {
                        input.isEmpty() -> {
                            errorMessage = "Input cannot be empty"
                            return@Button
                        }
                        n == null || n < 0 -> {
                            errorMessage = "Enter a valid positive number"
                            return@Button
                        }
                        n > 100000 -> {
                            errorMessage = "Number too large! Max is 100,000"
                            return@Button
                        }
                    }

                    errorMessage = null
                    fullResult = ""
                    scientificResult = ""
                    isCalculating = true

                    scope.launch {
                        try {
                            val res = calculateFactorialFast(n)
                            fullResult = res
                            scientificResult = formatScientific(res)
                        } catch (e: Exception) {
                            errorMessage = e.localizedMessage ?: "Calculation failed"
                        } finally {
                            isCalculating = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isCalculating
            ) {
                if (isCalculating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Calculate (!)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(24.dp))

            if (fullResult.isNotEmpty() && !isCalculating) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Result",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Factorial Result", fullResult)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(0.1f)
                        )

                        if (scientificResult.isNotEmpty()) {
                            Text(
                                text = "Approx: $scientificResult",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        val fontSize = when {
                            fullResult.length <= 10 -> 36.sp
                            fullResult.length <= 16 -> 26.sp
                            fullResult.length <= 22 -> 20.sp
                            fullResult.length <= 100 -> 16.sp
                            else -> 12.sp
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(displayChunks) { chunk ->
                                Text(
                                    text = chunk,
                                    fontSize = fontSize,
                                    lineHeight = (fontSize.value * 1.4).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatScientific(result: String): String {
    return if (result.length > 20) {
        val mantissaPart = result.substring(1, minOf(4, result.length))
        "≈ ${result[0]}.$mantissaPart e+${result.length - 1}"
    } else {
        ""
    }
}

suspend fun calculateFactorialFast(n: Int): String = withContext(Dispatchers.Default) {
    if (n < 0) return@withContext "0"
    if (n == 0 || n == 1) return@withContext "1"

    fun treeProduct(left: Int, right: Int): BigInteger {
        return when {
            left > right -> BigInteger.ONE
            left == right -> BigInteger.valueOf(left.toLong())
            right - left == 1 -> BigInteger.valueOf(left.toLong() * right.toLong())
            else -> {
                val mid = (left + right) / 2
                treeProduct(left, mid) * treeProduct(mid + 1, right)
            }
        }
    }

    treeProduct(2, n).toString()
}

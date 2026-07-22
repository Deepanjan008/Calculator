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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactorialScreen(navController: NavController) {
    var input by remember { mutableStateOf("") }
    var fullResult by remember { mutableStateOf("") }
    var scientificResult by remember { mutableStateOf("") }
    var isCalculating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Factorial Calculator") },
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 7)) {
                        input = newValue
                        errorMessage = ""
                    }
                },
                label = { Text("Enter a number (Max 100,000)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = errorMessage.isNotEmpty(),
                supportingText = if (errorMessage.isNotEmpty()) {
                    { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
                } else null
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        input.isEmpty() -> {
                            errorMessage = "Enter a positive number"
                            fullResult = ""
                            scientificResult = ""
                        }
                        input.toIntOrNull() == null -> {
                            errorMessage = "Invalid input"
                            fullResult = ""
                            scientificResult = ""
                        }
                        input.toInt() < 0 -> {
                            errorMessage = "Number must be non-negative"
                            fullResult = ""
                            scientificResult = ""
                        }
                        input.toInt() > 100000 -> {
                            errorMessage = "Number exceeds limit (max 100,000)"
                            fullResult = ""
                            scientificResult = ""
                        }
                        else -> {
                            val n = input.toInt()
                            keyboardController?.hide()
                            isCalculating = true
                            errorMessage = ""
                            fullResult = ""
                            scientificResult = ""

                            scope.launch {
                                try {
                                    val result = calculateFactorialFast(n)
                                    fullResult = result
                                    scientificResult = formatScientific(result)
                                } catch (e: CancellationException) {
                                    throw e
                                } catch (e: Exception) {
                                    errorMessage = "Calculation error: ${e.message}"
                                    fullResult = ""
                                    scientificResult = ""
                                } finally {
                                    isCalculating = false
                                }
                            }
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
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(12.dp))
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
                                    "Copy",
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
                                text = scientificResult,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        val fontSize = when {
                            fullResult.length <= 10 -> 32.sp
                            fullResult.length <= 16 -> 24.sp
                            fullResult.length <= 50 -> 18.sp
                            fullResult.length <= 500 -> 13.sp
                            else -> 11.sp
                        }

                        // Small results (< 5000 digits) use a single Text; large
                        // results use a virtualized LazyColumn — rendering ~450K
                        // digits in a single non-virtualized Text risks UI jank/ANR
                        val singleTextThreshold = 5000

                        if (fullResult.length <= singleTextThreshold) {
                            SelectionContainer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text(
                                    text = fullResult,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    textAlign = TextAlign.Center,
                                    lineHeight = (fontSize.value * 1.25).sp,
                                    fontFamily = FontFamily.Monospace,
                                    softWrap = true
                                )
                            }
                        } else {
                            val chunkSize = 2000
                            val displayChunks = remember(fullResult) {
                                fullResult.chunked(chunkSize)
                            }

                            SelectionContainer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {
                                    items(displayChunks) { chunk ->
                                        Text(
                                            text = chunk,
                                            fontSize = fontSize,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 4.dp),
                                            textAlign = TextAlign.Center,
                                            lineHeight = (fontSize.value * 1.25).sp,
                                            fontFamily = FontFamily.Monospace,
                                            softWrap = true
                                        )
                                    }
                                }
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
        val exponent = result.length - 1
        val mantissa = "${result[0]}.${result.substring(1, minOf(4, result.length))}"
        "≈ $mantissa × 10^$exponent"
    } else ""
}

suspend fun calculateFactorialFast(n: Int): String = withContext(Dispatchers.Default) {
    if (n < 0) return@withContext "0"
    if (n == 0 || n == 1) return@withContext "1"

    // currentCoroutineContext() is a suspend call, so it must be captured
    // here in the suspend scope — treeProduct itself is a plain (non-suspend)
    // recursive function and cannot call it directly
    val ctx = currentCoroutineContext()

    fun treeProduct(left: Int, right: Int): BigInteger {
        // Cooperative cancellation check on every split, so a long-running
        // recursion (e.g. n=100000) stops promptly if the composable is
        // disposed, instead of wasting CPU/battery in the background
        ctx.ensureActive()
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

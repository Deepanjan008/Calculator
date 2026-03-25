package com.deepanjan.neocalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorScreen(navController: NavController) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var isValid by remember { mutableStateOf(true) }
    var birthDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    
    // Stats states
    var years by remember { mutableIntStateOf(0) }
    var months by remember { mutableIntStateOf(0) }
    var days by remember { mutableIntStateOf(0) }
    var hours by remember { mutableIntStateOf(0) }
    var minutes by remember { mutableIntStateOf(0) }
    var seconds by remember { mutableIntStateOf(0) }
    var totalDaysLived by remember { mutableLongStateOf(0L) }
    var totalWeeksLived by remember { mutableLongStateOf(0L) }

    // Live ticking effect
    LaunchedEffect(birthDateTime) {
        if (birthDateTime == null) return@LaunchedEffect
        while (isActive) {
            val now = LocalDateTime.now()
            val birthDateOnly = birthDateTime!!.toLocalDate()
            val nowDateOnly = now.toLocalDate()
            
            val period = Period.between(birthDateOnly, nowDateOnly)
            val duration = Duration.between(birthDateTime, now)

            years = period.years
            months = period.months
            days = period.days
            
            hours = (duration.toHours() % 24).toInt()
            minutes = (duration.toMinutes() % 60).toInt()
            seconds = (duration.seconds % 60).toInt()
            
            totalDaysLived = ChronoUnit.DAYS.between(birthDateOnly, nowDateOnly)
            totalWeeksLived = totalDaysLived / 7
            
            delay(1000L)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Age Calculator", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { new ->
                    val digits = new.text.replace("[^0-9]".toRegex(), "")
                    if (digits.length <= 8) {
                        val formatted = buildString {
                            for (i in digits.indices) {
                                append(digits[i])
                                if ((i == 1 || i == 3) && i != digits.lastIndex) append("/")
                            }
                        }
                        // This forces the cursor to ALWAYS stay at the end
                        textFieldValue = TextFieldValue(
                            text = formatted,
                            selection = androidx.compose.ui.text.TextRange(formatted.length)
                        )
                    }
                    
                    // Validation
                    isValid = if (digits.length >= 4) {
                        val d = digits.take(2).toIntOrNull() ?: 0
                        val m = digits.drop(2).take(2).toIntOrNull() ?: 0
                        val y = if (digits.length == 8) digits.drop(4).take(4).toIntOrNull() ?: 0 else 0
                        var valid = d in 1..31 && m in 1..12
                        if (y > 0) valid = valid && y in 1900..LocalDate.now().year
                        valid
                    } else true
                },
                label = { Text("Date of Birth (DD/MM/YYYY)") },
                placeholder = { Text("25/12/1995") },
                isError = !isValid,
                supportingText = { if (!isValid) Text("Invalid date format", color = MaterialTheme.colorScheme.error) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val rawDigits = textFieldValue.text.replace("/", "")
                    if (isValid && rawDigits.length == 8) {
                        try {
                            val date = LocalDate.parse(textFieldValue.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            if (!date.isAfter(LocalDate.now())) {
                                birthDateTime = date.atStartOfDay()
                            } else {
                                isValid = false // Cannot be in the future
                            }
                        } catch (_: Exception) {
                            isValid = false
                        }
                    } else {
                        isValid = false
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Calculate & Start Live Counter", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(40.dp))

            if (birthDateTime != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "$years Years",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "$months Months, $days Days",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        Text(
                            "Live Ticking",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            "${String.format("%02d", hours)}h : ${String.format("%02d", minutes)}m : ${String.format("%02d", seconds)}s",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Weeks", fontSize = 14.sp)
                            Text("$totalWeeksLived", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Days", fontSize = 14.sp)
                            Text("$totalDaysLived", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.deepanjan.neocalculator.viewmodel.UnitConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericConverterDetailScreen(navController: NavController, category: String) {
    val viewModel: UnitConverterViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    var value by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("") }
    var toUnit by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val units = getUnitsForCategory(category)
    if (fromUnit.isEmpty() && units.isNotEmpty()) fromUnit = units[0]
    if (toUnit.isEmpty() && units.size > 1) toUnit = units[1]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category) },
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
                value = value, onValueChange = { value = it; showError = false },
                label = { Text("Enter Value") }, isError = showError && value.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                UnitDropdown(units, fromUnit, "From", Modifier.weight(1f)) { fromUnit = it }
                Spacer(Modifier.width(12.dp))
                UnitDropdown(units, toUnit, "To", Modifier.weight(1f)) { toUnit = it }
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val v = value.toDoubleOrNull()
                    if (v != null) {
                        val res = viewModel.convert(category, v, fromUnit, toUnit)
                        // Formatting result properly
                        val formattedRes = if (res % 1.0 == 0.0) String.format("%.0f", res) else String.format("%.5f", res).trimEnd('0').trimEnd('.')
                        resultText = "$formattedRes $toUnit"
                        showError = false; keyboardController?.hide()
                    } else { showError = true }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("Convert Unit", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

            if (resultText.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Result", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(text = resultText, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(units: List<String>, selected: String, label: String, modifier: Modifier, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(value = selected, onValueChange = {}, readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(12.dp))
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { u -> DropdownMenuItem(text = { Text(u) }, onClick = { onSelect(u); expanded = false }) }
        }
    }
}

// এই লিস্টটাই আমি একদম প্রো লেভেলের করে দিলাম!
private fun getUnitsForCategory(category: String): List<String> = when (category) {
    "Length" -> listOf("Kilometre (km)", "Metre (m)", "Centimetre (cm)", "Millimetre (mm)", "Micrometre (μm)", "Nanometre (nm)", "Angstrom (Å)", "Mile (mi)", "Yard (yd)", "Foot (ft)", "Inch (in)", "Nautical Mile (NM)", "Astronomical Unit (au)", "Light Year (ly)", "Parsec (pc)")
    "Area" -> listOf("Square Kilometre (km²)", "Square Metre (m²)", "Square Centimetre (cm²)", "Square Millimetre (mm²)", "Hectare (ha)", "Acre (ac)", "Square Mile (mi²)", "Square Yard (yd²)", "Square Foot (ft²)", "Square Inch (in²)")
    "Volume" -> listOf("Cubic Metre (m³)", "Litre (L)", "Millilitre (mL)", "Cubic Centimetre (cm³)", "US Gallon (gal)", "US Quart (qt)", "US Pint (pt)", "US Cup", "US Fluid Ounce (fl oz)", "Imperial Gallon", "Cubic Foot (ft³)", "Cubic Inch (in³)")
    "Mass" -> listOf("Metric Tonne (t)", "Kilogram (kg)", "Gram (g)", "Milligram (mg)", "Microgram (μg)", "Imperial Ton", "US Ton", "Stone (st)", "Pound (lb)", "Ounce (oz)", "Carat (ct)")
    "Temperature" -> listOf("Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)", "Rankine (°R)")
    "Time" -> listOf("Nanosecond (ns)", "Microsecond (μs)", "Millisecond (ms)", "Second (s)", "Minute (min)", "Hour (h)", "Day (d)", "Week (wk)", "Month (mo)", "Year (yr)", "Decade", "Century")
    "Speed" -> listOf("Metre per second (m/s)", "Kilometre per hour (km/h)", "Mile per hour (mph)", "Foot per second (ft/s)", "Knot (kn)", "Mach (Ma)")
    "Pressure" -> listOf("Pascal (Pa)", "Kilopascal (kPa)", "Megapascal (MPa)", "Bar (bar)", "Millibar (mbar)", "Standard Atmosphere (atm)", "Pound per square inch (psi)", "Torr (Torr)", "Millimetre of mercury (mmHg)")
    "Energy" -> listOf("Joule (J)", "Kilojoule (kJ)", "Gram calorie (cal)", "Kilocalorie (kcal)", "Watt-hour (Wh)", "Kilowatt-hour (kWh)", "Electronvolt (eV)", "British Thermal Unit (BTU)", "US Therm")
    else -> emptyList()
}

package com.deepanjan.neocalculator.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UnitConverterViewModel @Inject constructor() : ViewModel() {

    fun convert(category: String, value: Double, fromUnit: String, toUnit: String): Double {
        if (fromUnit == toUnit) return value
        return when (category) {
            "Length" -> lengthConvert(value, fromUnit, toUnit)
            "Area" -> areaConvert(value, fromUnit, toUnit)
            "Volume" -> volumeConvert(value, fromUnit, toUnit)
            "Mass" -> massConvert(value, fromUnit, toUnit)
            "Temperature" -> temperatureConvert(value, fromUnit, toUnit)
            "Time" -> timeConvert(value, fromUnit, toUnit)
            "Speed" -> speedConvert(value, fromUnit, toUnit)
            "Pressure" -> pressureConvert(value, fromUnit, toUnit)
            "Energy" -> energyConvert(value, fromUnit, toUnit)
            else -> value
        }
    }

    private fun lengthConvert(value: Double, from: String, to: String): Double {
        val toMeter = when (from) {
            "Kilometre (km)" -> value * 1000.0
            "Metre (m)" -> value
            "Centimetre (cm)" -> value / 100.0
            "Millimetre (mm)" -> value / 1000.0
            "Micrometre (μm)" -> value / 1e6
            "Nanometre (nm)" -> value / 1e9
            "Angstrom (Å)" -> value / 1e10
            "Mile (mi)" -> value * 1609.344
            "Yard (yd)" -> value * 0.9144
            "Foot (ft)" -> value * 0.3048
            "Inch (in)" -> value * 0.0254
            "Nautical Mile (NM)" -> value * 1852.0
            "Astronomical Unit (au)" -> value * 1.496e11
            "Light Year (ly)" -> value * 9.461e15
            "Parsec (pc)" -> value * 3.086e16
            else -> value
        }
        return when (to) {
            "Kilometre (km)" -> toMeter / 1000.0
            "Metre (m)" -> toMeter
            "Centimetre (cm)" -> toMeter * 100.0
            "Millimetre (mm)" -> toMeter * 1000.0
            "Micrometre (μm)" -> toMeter * 1e6
            "Nanometre (nm)" -> toMeter * 1e9
            "Angstrom (Å)" -> toMeter * 1e10
            "Mile (mi)" -> toMeter / 1609.344
            "Yard (yd)" -> toMeter / 0.9144
            "Foot (ft)" -> toMeter / 0.3048
            "Inch (in)" -> toMeter / 0.0254
            "Nautical Mile (NM)" -> toMeter / 1852.0
            "Astronomical Unit (au)" -> toMeter / 1.496e11
            "Light Year (ly)" -> toMeter / 9.461e15
            "Parsec (pc)" -> toMeter / 3.086e16
            else -> toMeter
        }
    }

    private fun areaConvert(value: Double, from: String, to: String): Double {
        val toSqm = when (from) {
            "Square Kilometre (km²)" -> value * 1e6
            "Square Metre (m²)" -> value
            "Square Centimetre (cm²)" -> value / 10000.0
            "Square Millimetre (mm²)" -> value / 1e6
            "Hectare (ha)" -> value * 10000.0
            "Acre (ac)" -> value * 4046.856
            "Square Mile (mi²)" -> value * 2.59e6
            "Square Yard (yd²)" -> value * 0.836127
            "Square Foot (ft²)" -> value * 0.092903
            "Square Inch (in²)" -> value * 0.00064516
            else -> value
        }
        return when (to) {
            "Square Kilometre (km²)" -> toSqm / 1e6
            "Square Metre (m²)" -> toSqm
            "Square Centimetre (cm²)" -> toSqm * 10000.0
            "Square Millimetre (mm²)" -> toSqm * 1e6
            "Hectare (ha)" -> toSqm / 10000.0
            "Acre (ac)" -> toSqm / 4046.856
            "Square Mile (mi²)" -> toSqm / 2.59e6
            "Square Yard (yd²)" -> toSqm / 0.836127
            "Square Foot (ft²)" -> toSqm / 0.092903
            "Square Inch (in²)" -> toSqm / 0.00064516
            else -> toSqm
        }
    }

    private fun volumeConvert(value: Double, from: String, to: String): Double {
        val toLiter = when (from) {
            "Cubic Metre (m³)" -> value * 1000.0
            "Litre (L)" -> value
            "Millilitre (mL)" -> value / 1000.0
            "Cubic Centimetre (cm³)" -> value / 1000.0
            "US Gallon (gal)" -> value * 3.78541
            "US Quart (qt)" -> value * 0.946353
            "US Pint (pt)" -> value * 0.473176
            "US Cup" -> value * 0.236588
            "US Fluid Ounce (fl oz)" -> value * 0.0295735
            "Imperial Gallon" -> value * 4.54609
            "Cubic Foot (ft³)" -> value * 28.3168
            "Cubic Inch (in³)" -> value * 0.0163871
            else -> value
        }
        return when (to) {
            "Cubic Metre (m³)" -> toLiter / 1000.0
            "Litre (L)" -> toLiter
            "Millilitre (mL)" -> toLiter * 1000.0
            "Cubic Centimetre (cm³)" -> toLiter * 1000.0
            "US Gallon (gal)" -> toLiter / 3.78541
            "US Quart (qt)" -> toLiter / 0.946353
            "US Pint (pt)" -> toLiter / 0.473176
            "US Cup" -> toLiter / 0.236588
            "US Fluid Ounce (fl oz)" -> toLiter / 0.0295735
            "Imperial Gallon" -> toLiter / 4.54609
            "Cubic Foot (ft³)" -> toLiter / 28.3168
            "Cubic Inch (in³)" -> toLiter / 0.0163871
            else -> toLiter
        }
    }

    private fun massConvert(value: Double, from: String, to: String): Double {
        val toKg = when (from) {
            "Metric Tonne (t)" -> value * 1000.0
            "Kilogram (kg)" -> value
            "Gram (g)" -> value / 1000.0
            "Milligram (mg)" -> value / 1e6
            "Microgram (μg)" -> value / 1e9
            "Imperial Ton" -> value * 1016.05
            "US Ton" -> value * 907.185
            "Stone (st)" -> value * 6.35029
            "Pound (lb)" -> value * 0.453592
            "Ounce (oz)" -> value * 0.0283495
            "Carat (ct)" -> value * 0.0002
            else -> value
        }
        return when (to) {
            "Metric Tonne (t)" -> toKg / 1000.0
            "Kilogram (kg)" -> toKg
            "Gram (g)" -> toKg * 1000.0
            "Milligram (mg)" -> toKg * 1e6
            "Microgram (μg)" -> toKg * 1e9
            "Imperial Ton" -> toKg / 1016.05
            "US Ton" -> toKg / 907.185
            "Stone (st)" -> toKg / 6.35029
            "Pound (lb)" -> toKg / 0.453592
            "Ounce (oz)" -> toKg / 0.0283495
            "Carat (ct)" -> toKg / 0.0002
            else -> toKg
        }
    }

    private fun temperatureConvert(value: Double, from: String, to: String): Double {
        val celsius = when (from) {
            "Celsius (°C)" -> value
            "Fahrenheit (°F)" -> (value - 32.0) * 5.0 / 9.0
            "Kelvin (K)" -> value - 273.15
            "Rankine (°R)" -> (value - 491.67) * 5.0 / 9.0
            else -> value
        }
        return when (to) {
            "Celsius (°C)" -> celsius
            "Fahrenheit (°F)" -> celsius * 9.0 / 5.0 + 32.0
            "Kelvin (K)" -> celsius + 273.15
            "Rankine (°R)" -> (celsius + 273.15) * 9.0 / 5.0
            else -> celsius
        }
    }

    private fun timeConvert(value: Double, from: String, to: String): Double {
        val toSec = when (from) {
            "Nanosecond (ns)" -> value / 1e9
            "Microsecond (μs)" -> value / 1e6
            "Millisecond (ms)" -> value / 1000.0
            "Second (s)" -> value
            "Minute (min)" -> value * 60.0
            "Hour (h)" -> value * 3600.0
            "Day (d)" -> value * 86400.0
            "Week (wk)" -> value * 604800.0
            "Month (mo)" -> value * 2.628e6
            "Year (yr)" -> value * 3.154e7
            "Decade" -> value * 3.154e8
            "Century" -> value * 3.154e9
            else -> value
        }
        return when (to) {
            "Nanosecond (ns)" -> toSec * 1e9
            "Microsecond (μs)" -> toSec * 1e6
            "Millisecond (ms)" -> toSec * 1000.0
            "Second (s)" -> toSec
            "Minute (min)" -> toSec / 60.0
            "Hour (h)" -> toSec / 3600.0
            "Day (d)" -> toSec / 86400.0
            "Week (wk)" -> toSec / 604800.0
            "Month (mo)" -> toSec / 2.628e6
            "Year (yr)" -> toSec / 3.154e7
            "Decade" -> toSec / 3.154e8
            "Century" -> toSec / 3.154e9
            else -> toSec
        }
    }

    private fun speedConvert(value: Double, from: String, to: String): Double {
        val toMps = when (from) {
            "Metre per second (m/s)" -> value
            "Kilometre per hour (km/h)" -> value / 3.6
            "Mile per hour (mph)" -> value * 0.44704
            "Foot per second (ft/s)" -> value * 0.3048
            "Knot (kn)" -> value * 0.514444
            "Mach (Ma)" -> value * 340.3
            else -> value
        }
        return when (to) {
            "Metre per second (m/s)" -> toMps
            "Kilometre per hour (km/h)" -> toMps * 3.6
            "Mile per hour (mph)" -> toMps / 0.44704
            "Foot per second (ft/s)" -> toMps / 0.3048
            "Knot (kn)" -> toMps / 0.514444
            "Mach (Ma)" -> toMps / 340.3
            else -> toMps
        }
    }

    private fun pressureConvert(value: Double, from: String, to: String): Double {
        val toPa = when (from) {
            "Pascal (Pa)" -> value
            "Kilopascal (kPa)" -> value * 1000.0
            "Megapascal (MPa)" -> value * 1e6
            "Bar (bar)" -> value * 100000.0
            "Millibar (mbar)" -> value * 100.0
            "Standard Atmosphere (atm)" -> value * 101325.0
            "Pound per square inch (psi)" -> value * 6894.76
            "Torr (Torr)" -> value * 133.322
            "Millimetre of mercury (mmHg)" -> value * 133.322
            else -> value
        }
        return when (to) {
            "Pascal (Pa)" -> toPa
            "Kilopascal (kPa)" -> toPa / 1000.0
            "Megapascal (MPa)" -> toPa / 1e6
            "Bar (bar)" -> toPa / 100000.0
            "Millibar (mbar)" -> toPa / 100.0
            "Standard Atmosphere (atm)" -> toPa / 101325.0
            "Pound per square inch (psi)" -> toPa / 6894.76
            "Torr (Torr)" -> toPa / 133.322
            "Millimetre of mercury (mmHg)" -> toPa / 133.322
            else -> toPa
        }
    }

    private fun energyConvert(value: Double, from: String, to: String): Double {
        val toJoule = when (from) {
            "Joule (J)" -> value
            "Kilojoule (kJ)" -> value * 1000.0
            "Gram calorie (cal)" -> value * 4.184
            "Kilocalorie (kcal)" -> value * 4184.0
            "Watt-hour (Wh)" -> value * 3600.0
            "Kilowatt-hour (kWh)" -> value * 3.6e6
            "Electronvolt (eV)" -> value * 1.6022e-19
            "British Thermal Unit (BTU)" -> value * 1055.06
            "US Therm" -> value * 1.055e8
            else -> value
        }
        return when (to) {
            "Joule (J)" -> toJoule
            "Kilojoule (kJ)" -> toJoule / 1000.0
            "Gram calorie (cal)" -> toJoule / 4.184
            "Kilocalorie (kcal)" -> toJoule / 4184.0
            "Watt-hour (Wh)" -> toJoule / 3600.0
            "Kilowatt-hour (kWh)" -> toJoule / 3.6e6
            "Electronvolt (eV)" -> toJoule / 1.6022e-19
            "British Thermal Unit (BTU)" -> toJoule / 1055.06
            "US Therm" -> toJoule / 1.055e8
            else -> toJoule
        }
    }
}

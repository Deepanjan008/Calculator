package com.nexuralabs.calculator.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class EquationSolverViewModel @Inject constructor() : ViewModel() {

    private val _result = mutableStateOf("")
    val result: State<String> = _result

    fun solve(input: String) {
        try {
            // Normalize: remove spaces, remove "=0", replace Unicode superscript
            var eq = input.replace(" ", "").replace("=0", "")
            eq = eq.replace("x²", "x^2")

            if (eq.contains("x^2")) {
                // Quadratic
                val (a, b, c) = parseQuadratic(eq)
                val disc = b * b - 4 * a * c
                if (disc < 0) {
                    _result.value = "No real roots"
                } else if (disc == 0.0) {
                    val x = -b / (2 * a)
                    _result.value = "x = $x (double root)"
                } else {
                    val x1 = (-b + sqrt(disc)) / (2 * a)
                    val x2 = (-b - sqrt(disc)) / (2 * a)
                    _result.value = "x₁ = $x1\nx₂ = $x2"
                }
            } else {
                // Linear
                val (a, b) = parseLinear(eq)
                if (a == 0.0) throw Exception("Not a valid equation")
                val x = -b / a
                _result.value = "x = $x"
            }
        } catch (e: Exception) {
            _result.value = "Invalid equation format"
        }
    }

    // ---------- Parser helpers ----------

    /**
     * Splits an expression like "2x^2+3x-5" into a list of terms,
     * each preserving its leading sign.
     */
    private fun splitTerms(expr: String): List<String> {
        val terms = mutableListOf<String>()
        var current = ""
        var i = 0
        while (i < expr.length) {
            val ch = expr[i]
            if (ch == '+' || ch == '-') {
                if (current.isNotEmpty()) {
                    terms.add(current)
                    current = ch.toString()
                } else {
                    // Expression starts with a sign
                    current = ch.toString()
                }
            } else {
                current += ch
            }
            i++
        }
        if (current.isNotEmpty()) terms.add(current)
        return terms
    }

    /**
     * Parses a single term like "+2x^2", "-3x", "+5", "-x".
     * Returns a pair (type, coefficient) where type is "x2", "x", or "const".
     */
    private fun parseTerm(term: String): Pair<String, Double> {
        val varPart = when {
            term.contains("x^2") -> "x^2"
            term.contains("x") -> "x"
            else -> ""
        }
        val coeffStr = if (varPart.isNotEmpty()) {
            term.substring(0, term.length - varPart.length)
        } else {
            term
        }
        val coeff = when {
            coeffStr.isEmpty() -> 1.0          // e.g. "x^2" or "x"
            coeffStr == "+" -> 1.0
            coeffStr == "-" -> -1.0
            else -> coeffStr.toDoubleOrNull() ?: throw Exception("Invalid coefficient")
        }
        val type = when (varPart) {
            "x^2" -> "x2"
            "x" -> "x"
            else -> "const"
        }
        return Pair(type, coeff)
    }

    private fun parseQuadratic(eq: String): Triple<Double, Double, Double> {
        val terms = splitTerms(eq)
        var a = 0.0
        var b = 0.0
        var c = 0.0
        for (term in terms) {
            val (type, coeff) = parseTerm(term)
            when (type) {
                "x2" -> a += coeff
                "x" -> b += coeff
                "const" -> c += coeff
            }
        }
        if (a == 0.0) throw Exception("Not a quadratic equation")
        return Triple(a, b, c)
    }

    private fun parseLinear(eq: String): Pair<Double, Double> {
        val terms = splitTerms(eq)
        var a = 0.0
        var b = 0.0
        for (term in terms) {
            val (type, coeff) = parseTerm(term)
            when (type) {
                "x" -> a += coeff
                "const" -> b += coeff
                "x2" -> throw Exception("Not a linear equation")
            }
        }
        if (a == 0.0) throw Exception("Not a linear equation")
        return Pair(a, b)
    }
}

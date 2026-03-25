package com.deepanjan.neocalculator.viewmodel

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
            val eq = input.replace(" ", "").replace("=0", "")
            if (eq.contains("x²") || eq.contains("x^2")) {
                // Quadratic
                val coeffs = parseQuadratic(eq)
                val (a, b, c) = coeffs
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

    private fun parseQuadratic(eq: String): Triple<Double, Double, Double> {
        // Warning: This is a placeholder!
        return Triple(1.0, 0.0, 0.0) 
    }

    private fun parseLinear(eq: String): Pair<Double, Double> {
        // Warning: This is a placeholder!
        return Pair(1.0, 0.0)
    }
}

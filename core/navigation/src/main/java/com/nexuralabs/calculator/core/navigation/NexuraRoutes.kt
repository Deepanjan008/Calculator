package com.nexuralabs.calculator.core.navigation

/**
 * Every navigation route string used anywhere in the app lives here, once.
 * Feature modules only depend on this tiny module (no dependency on each other)
 * to register and navigate to destinations owned by other features.
 */
object NexuraRoutes {
    const val CALCULATOR = "calculator"
    const val HISTORY = "history"
    const val SETTINGS = "settings"

    const val BMI = "bmi"
    const val INVESTMENT = "investment"
    const val FUEL = "fuel"
    const val UNIT_PRICE = "unit_price"
    const val GPA = "gpa"
    const val CURRENCY = "currency"
    const val AGE = "age"
    const val EMI = "emi"
    const val SOLVER = "solver"
    const val FACTORIAL = "factorial"
    const val DISCOUNT = "discount"
    const val LAND = "land"

    const val UNIT_CONVERTER = "unit_converter"
    const val CONVERTER_DETAIL_ARG = "category"
    const val CONVERTER_DETAIL = "converter_detail/{$CONVERTER_DETAIL_ARG}"

    fun converterDetail(category: String) = "converter_detail/$category"
}

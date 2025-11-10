package com.example.appmilsabores.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PriceUtils {

    fun formatPriceCLP(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat("#,###", symbols)
        return "$" + formatter.format(price.toInt())
    }
}
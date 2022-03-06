package ru.ytken.sravni.internship.presentation

object Utils {
    fun convertStringToPrice(price: Float): String {
        var string = price.toInt().toString()
        var result = ""
        while (string.length > 3) {
            result = string.substring(string.length - 3) + " " + result
            string = string.substring(0, string.length - 3)
        }
        return "$string $result"
    }
}
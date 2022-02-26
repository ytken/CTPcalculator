package ru.ytken.sravni.internship.data.storage.models.insurersactivity

data class Insurer(
    var name: String,
    var rating: Float,
    var price: Float,
    var branding: Branding
)
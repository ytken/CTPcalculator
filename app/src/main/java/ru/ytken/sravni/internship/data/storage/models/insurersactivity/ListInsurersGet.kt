package ru.ytken.sravni.internship.data.storage.models.insurersactivity

data class ListInsurersGet(
    var offers: Array<Insurer>,
    var actionText: String
)
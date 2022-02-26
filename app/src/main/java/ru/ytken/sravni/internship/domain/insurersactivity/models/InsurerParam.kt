package ru.ytken.sravni.internship.domain.insurersactivity.models

import ru.ytken.sravni.internship.data.storage.models.insurersactivity.Branding

data class InsurerParam(
    var name: String,
    var rating: Float,
    var price: Float,
    var fontColor: String,
    var backgroundColor: String,
    var iconTitle: String,
    var bankLogoUrlPDF: String?,
    var bankLogoUrlSVG: String?
)
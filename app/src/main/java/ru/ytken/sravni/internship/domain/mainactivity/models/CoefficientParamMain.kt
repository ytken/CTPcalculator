package ru.ytken.sravni.internship.domain.mainactivity.models

import java.io.Serializable

data class CoefficientParamMain (
    val title: String,
    val headerValue: String,
    val name: String,
    val detailText: String,
    val value: String
    ) : Serializable
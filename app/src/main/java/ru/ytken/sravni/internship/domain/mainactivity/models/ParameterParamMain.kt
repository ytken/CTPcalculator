package ru.ytken.sravni.internship.domain.mainactivity.models

import java.io.Serializable

data class ParameterParamMain (
    val title: String,
    var value: String,
    val hint: String,
    val type: String,
    val dimension: String
    ) : Serializable
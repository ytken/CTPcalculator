package ru.ytken.sravni.internship.data.storage

import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.Parameter

interface ParameterStorage {

    fun send(parameter: Parameter): Boolean

    fun get(): Coefficient

}
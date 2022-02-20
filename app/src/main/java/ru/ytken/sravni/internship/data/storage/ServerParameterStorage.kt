package ru.ytken.sravni.internship.data.storage

import android.content.Context
import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.Parameter

class ServerParameterStorage(context: Context) : ParameterStorage {

    override fun send(parameterList: ListParametersPost): Boolean {
        return false
        TODO("Implement in part 2")
    }

    override fun get(): ListCoefficientsGet {
        TODO("Implement in part 2")
    }


}
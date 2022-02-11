package ru.ytken.sravni.internship.data.storage

import android.content.Context
import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.Parameter

class ServerParameterStorage(context: Context) : ParameterStorage {

    override fun send(parameter: Parameter): Boolean {
        return false
        TODO("Implement in part 2")
    }

    override fun get(): Coefficient {
        return Coefficient("Whaat BT", "Whaat KM", "Whaat KT", "Whaat KBM", "Whaat KVS", "Whaat KO")
        TODO("Implement in part 2")
    }


}
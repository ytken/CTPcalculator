package ru.ytken.sravni.internship.domain.mainactivity.models

import android.content.Context
import ru.ytken.sravni.internship.R

class ListCoefficientsParam(
    val list: ArrayList<CoefficientParamMain> = ArrayList()
) {
    fun initList(context: Context) {
        val arrays = arrayOf(
            context.resources.getStringArray(R.array.BT),
            context.resources.getStringArray(R.array.KM),
            context.resources.getStringArray(R.array.KT),
            context.resources.getStringArray(R.array.KBM),
            context.resources.getStringArray(R.array.KVS),
            context.resources.getStringArray(R.array.KO)
        )
        for (array in arrays) {
            list.add(
                CoefficientParamMain(
                    array[0],
                    array[1],
                    array[2],
                    array[3],
                    array[4]
                )
            )
        }
    }
}
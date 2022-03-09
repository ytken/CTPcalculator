package ru.ytken.sravni.internship.domain.mainactivity.models

import android.content.Context
import ru.ytken.sravni.internship.R

class ListParametersParam (
    var list: ArrayList<ParameterParamMain> = ArrayList()
) {
    fun initList(context: Context) {
        val arrays = arrayOf(
            context.resources.getStringArray(R.array.userCity),
            context.resources.getStringArray(R.array.userCapacity),
            context.resources.getStringArray(R.array.userNumber),
            context.resources.getStringArray(R.array.userYoungest),
            context.resources.getStringArray(R.array.userMinExp),
            context.resources.getStringArray(R.array.userNoCrush)
        )
        for (array in arrays) {
            list.add(
                ParameterParamMain(
                    array[0],
                    "",
                    array[1],
                    array[2],
                    array[3]
                )
            )
        }
    }
}

package ru.ytken.sravni.internship.domain.models

class ListParametersParam(
    var userCity: ParameterParam = ParameterParam("userCity", "",
        "Город регистрации собственника", "text", ""),
    var userCapacity: ParameterParam = ParameterParam("userCapacity", "",
        "Мощность автомобиля", "number", "л.с."),
    var userNumber: ParameterParam = ParameterParam("userNumber", "",
        "Сколько водителей", "number", "водителя"),
    var userYoungest: ParameterParam = ParameterParam("userYoungest", "",
        "Возраст младшего из водителей", "text", "лет"),
    var userMinExp: ParameterParam = ParameterParam("userMinExp", "",
        "Минимальный стаж водителей", "text", "года"),
    var userNoCrush: ParameterParam = ParameterParam("userNoCrush", "",
        "Сколько лет не было аварий", "number", "года")
)
{
    fun getSize(): Int {
        return 6
    }

    fun toArray() : Array<ParameterParam> {
        return arrayOf(userCity, userCapacity,
            userNumber, userYoungest,
            userMinExp, userNoCrush
        )
    }

    fun getElementById(id: Int) : ParameterParam {
        val parameterShow = when (id) {
            0 -> userCity
            1 -> userCapacity
            2 -> userNumber
            3 -> userYoungest
            4 -> userMinExp
            5 -> userNoCrush
            else -> ParameterParam("title", "value", "hint", "type", "dimension")
        }
        return parameterShow
    }

    fun setValueForElement(id: Int, value: String) {
        val parameterChange = getElementById(id)
        parameterChange.value = value
    }

}
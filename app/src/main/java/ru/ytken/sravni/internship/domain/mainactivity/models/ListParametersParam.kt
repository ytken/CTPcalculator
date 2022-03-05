package ru.ytken.sravni.internship.domain.mainactivity.models

class ListParametersParam (
    var list: ArrayList<ParameterParamMain> = arrayListOf(
        ParameterParamMain("userCity", "",
        "Город регистрации собственника", "text", ""),
        ParameterParamMain("userCapacity", "",
        "Мощность автомобиля", "number", "л.с."),
        ParameterParamMain("userNumber", "",
        "Сколько водителей", "number", "водителя"),
        ParameterParamMain("userYoungest", "",
        "Возраст младшего из водителей", "text", "лет"),
        ParameterParamMain("userMinExp", "",
        "Минимальный стаж водителей", "text", "года"),
        ParameterParamMain("userNoCrush", "",
        "Сколько лет не было аварий", "number", "года")
    )
)

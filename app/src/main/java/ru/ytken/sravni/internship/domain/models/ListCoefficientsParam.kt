package ru.ytken.sravni.internship.domain.models

class ListCoefficientsParam (
    var BT: CoefficientParam = CoefficientParam("БТ", "БТ","базовый тариф",
        "Устанавливает страховая компания", "2 754 - 4 432 ₽"),
    var KM: CoefficientParam = CoefficientParam("КМ", "КМ","коэфф. мощности",
        "Чем мощнее автомобиль, \nтем дороже страховой полис", "0,6 - 1,6"),
    var KT: CoefficientParam = CoefficientParam("КТ", "КТ","территориальный коэфф.",
        "Определяется по прописке собственника автомобиля", "0,64 - 1,99"),
    var KBM: CoefficientParam = CoefficientParam("КБМ", "КБМ","коэфф. безаварийности",
        "Учитывается только самый высокий коэффициент из всех водителей", "0,5 - 2,45"),
    var KVS: CoefficientParam = CoefficientParam("КВС", "КВС","коэфф. возраст/стаж",
        "Чем больше возраст и стаж у вписанного в полис водителя, \nтем дешевле будет полис", "0,90 - 1,93"),
    var KO: CoefficientParam = CoefficientParam("КО", "КО","коэфф. ограничений",
        "Полис с ограниченным списком водителей будет стоить дешевле", "1 или 1,99")
)
{
    fun getSize(): Int {
        return 6
    }

    fun getElementById(id: Int) : CoefficientParam {
        val coefficientShow = when (id) {
            0 -> BT
            1 -> KM
            2 -> KT
            3 -> KBM
            4 -> KVS
            5 -> KO
            else -> CoefficientParam("title", "header", "name", "detail", "value")
        }
        return coefficientShow
    }
}
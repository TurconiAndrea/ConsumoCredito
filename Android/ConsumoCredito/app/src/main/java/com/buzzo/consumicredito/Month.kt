package com.buzzo.consumicredito

enum class Month (val month: String) {

    Gen ("01"),
    Feb ("02"),
    Mar ("03"),
    Apr ("04"),
    Mag ("05"),
    Giu ("06"),
    Lug ("07"),
    Ago ("08"),
    Set ("09"),
    Ott ("10"),
    Nov ("11"),
    Dic ("12");

    companion object {
        fun getMonthFromValue(value: String): String {
            var monthName = ""
            for (month in Month.values()) {
                if (month.month == value) {
                    monthName = month.name
                    break
                }
            }
            return monthName
        }
    }

}
package com.buzzo.consumicredito

enum class IliadRoute(val route: String) {

    ROUTE_HOME ("https://iliad-api.herokuapp.com/"),
    ROUTE_GB("GBcomsumption"),
    ROUTE_RENEWAL("RenewalInformation"),
    ROUTE_ALL_INFORMATION ("AllComsumption"),
    ROUTE_FAQ("FAQ")

}
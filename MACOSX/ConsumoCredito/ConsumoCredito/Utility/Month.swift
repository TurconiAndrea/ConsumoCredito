//
//  Month.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import Foundation

class Month {
    
    enum MonthType: String, CaseIterable {
        case Gen = "01"
        case Feb = "02"
        case Mar = "03"
        case Apr = "04"
        case Mag = "05"
        case Giu = "06"
        case Lug = "07"
        case Ago = "08"
        case Set = "09"
        case Ott = "10"
        case Nov = "11"
        case Dic = "12"
    }
    
    public func getMonthFromValue(value: String) -> String {
        var monthName = ""
        for month in MonthType.allCases {
            if month.rawValue == value {
                monthName = String(describing: month)
            }
        }
        return monthName
    }
    
}

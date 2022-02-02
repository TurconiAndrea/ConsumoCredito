//
//  Utility.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import Foundation
import UIKit

class Utility {
    
    public func createCardView(view: UIView) {
            view.layer.cornerRadius = 10.0
            view.layer.shadowRadius = 2
            view.layer.shadowOpacity = 0.5
            view.layer.shadowOffset = .zero
            view.layer.shadowColor = UIColor.black.cgColor
    }
    
}

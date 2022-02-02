//
//  SavedInformation.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import Foundation

class SavedInformation {
    
    private let FIRST_RUN_KEY = "FIRST_RUN"
    private let AUTH_KEY = "AUTH_KEY"
    private let userDefaults = UserDefaults.standard
    
    public func isFirstRun() -> Bool {
        return !userDefaults.bool(forKey: FIRST_RUN_KEY)
    }
    
    public func setFirstRunDone() {
        userDefaults.set(true, forKey: FIRST_RUN_KEY)
    }
    
    public func getAuthKey() -> String {
        return userDefaults.string(forKey: AUTH_KEY) ?? ""
    }
    
    public func setAuthKey(authKey: String) {
        userDefaults.set(authKey, forKey: AUTH_KEY)
    }
    
    public func removeAuthKey() {
        userDefaults.removeObject(forKey: AUTH_KEY)
    }
    
}

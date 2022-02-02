//
//  User.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import Foundation

class User {
    var username: String
    var password: String
    
    init(username: String, password: String) {
        self.username = username
        self.password = password
    }
    
    let encryptionTool = EncryptionTool()
    
    public func getEncryptedAuth() -> String {
        let key = encryptionTool.randomString(length: 10)
        let authKey = "\(username):\(password)"
        var encryptedAuth = "\(key)%"
        
        do {
            let encryptionKey = encryptionTool.generateEncryptionKey(withPassword: key)
            encryptedAuth += try encryptionTool.enctryptMessage(message: authKey, encryptionKey: encryptionKey)
        } catch {
            debugPrint(error)
        }
        
        return encryptedAuth
    }    
}

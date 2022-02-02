//
//  UserTool.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 30/09/2020.
//

import Foundation

class UserTool {
    
    let encryptionTool = EncryptionTool()
    
    public func getDecryptedAuth(authKey: String) -> String {
        let splitted = authKey.split(separator: "%", maxSplits: 1)
        let key = String(splitted[0])
        let encryptedMessage = String(splitted[1])
        var decrypted = ""
        
        do {
            let encryptionKey = encryptionTool.generateEncryptionKey(withPassword: key)
            decrypted = try encryptionTool.decryptMessage(encryptedMessage: encryptedMessage, encryptionKey: encryptionKey)
        } catch {
            debugPrint(error)
        }
        
        return decrypted
    }
    
    public func getLoginData(authKey: String) -> [String] {
        let splitted = authKey.split(separator: ":")
        let username = String(splitted[0])
        let password = String(splitted[1])
        return [username, password]
    }
    
}

//
//  EncryptionTool.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 30/09/2020.
//

import Foundation
import RNCryptor

class EncryptionTool {
    
    func randomString(length: Int) -> String {
      let letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      return String((0..<length).map{ _ in letters.randomElement()! })
    }
    
    func enctryptMessage(message: String, encryptionKey:String) throws -> String {
        let messageData = message.data(using: .utf8)!
        let chiperData = RNCryptor.encrypt(data: messageData, withPassword: encryptionKey)
        return chiperData.base64EncodedString()
    }
    
    func decryptMessage(encryptedMessage: String, encryptionKey: String) throws -> String {
        let encryptedData = Data.init(base64Encoded: encryptedMessage)!
        let decryptedData = try RNCryptor.decrypt(data: encryptedData, withPassword: encryptionKey)
        let decryptedString = String(data: decryptedData, encoding: .utf8)!
        return decryptedString
    }
    
    func generateEncryptionKey(withPassword password: String) -> String {
        return password.data(using: .utf8)!.base64EncodedString()
    }
    
}

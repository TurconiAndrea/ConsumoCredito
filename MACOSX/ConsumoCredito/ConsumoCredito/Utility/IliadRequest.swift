//
//  IliadRequest.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import Foundation
import Alamofire

class IliadRequest {
    
    var username: String = ""
    var password: String = ""
    
    let savedInformation = SavedInformation()
    let userTool = UserTool()
    
    init (active: Bool) {
        if active {
            self.initializeData()
        }
    }
    
    private func initializeData() {
        let userData = getUserLoginData()
        self.username = userData[0]
        self.password = userData[1]
    }
    
    private func getUserLoginData() -> [String] {
        let authKeyEncrypted = savedInformation.getAuthKey()
        let authKey = userTool.getDecryptedAuth(authKey: authKeyEncrypted)
        let userData = userTool.getLoginData(authKey: authKey)
        return userData
    }
    
    public func getRoute(route: IliadRoute.Route) -> String {
        let url = IliadRoute.Route.ROUTE_HOME.rawValue + route.rawValue
        return url
    }
    
    public func getHeader(username: String, password: String) -> HTTPHeaders {
        let headers: HTTPHeaders = [
            .authorization(username: username, password: password)]
        return headers
    }
    
    public func getHeader() -> HTTPHeaders {
        let headers: HTTPHeaders = [
            .authorization(username: self.username, password: self.password)]
        return headers
    }
    
}

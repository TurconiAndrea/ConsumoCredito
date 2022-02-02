//
//  ViewController.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 28/09/2020.
//

import UIKit
import Alamofire
import NVActivityIndicatorView

class ViewController: UIViewController {
    
    let savedInformation = SavedInformation()
    let userTool = UserTool()
    
    @IBOutlet weak var loadingDots: NVActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        animateLoadingDots()
    }

    override func viewDidAppear(_ animated: Bool) {
        if savedInformation.isFirstRun() || savedInformation.getAuthKey() == "" {
            savedInformation.setFirstRunDone()
            openLoginPage()
        } else {
            sendGBdataToHomePage()
        }
    }
    
    private func openLoginPage() {
        let storyboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let loginPageController = storyboard.instantiateViewController(withIdentifier: "LoginPage") as! LoginViewController
        loginPageController.modalPresentationStyle = .fullScreen
        self.present(loginPageController, animated: false, completion: nil)
    }

    private func openHomePage(gbConsumption: String) {
        let storyboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let homePageController = storyboard.instantiateViewController(withIdentifier: "HomePage") as! HomePageViewController
        homePageController.modalPresentationStyle = .fullScreen
        homePageController.gbConsumption = gbConsumption
        self.present(homePageController, animated: false, completion: nil)
    }
        
    private func animateLoadingDots() {
        loadingDots.startAnimating()
    }
    
    private func getUserLoginData() -> [String] {
        let authKeyEncrypted = savedInformation.getAuthKey()
        let authKey = userTool.getDecryptedAuth(authKey: authKeyEncrypted)
        let userData = userTool.getLoginData(authKey: authKey)
        return userData
    }
    
    private func sendGBdataToHomePage() {
        let iliadRequest = IliadRequest(active: true)
        let url = iliadRequest.getRoute(route: IliadRoute.Route.ROUTE_GB)
        let headers = iliadRequest.getHeader()
        
        AF.request(url, headers: headers).responseJSON { response in
            if let result = response.value {
                let gbConsumptionArray = result as! NSArray
                let gbConsumption = gbConsumptionArray[0] as! String
                self.openHomePage(gbConsumption: gbConsumption)
                
            } else {
                debugPrint("LOGIN ERROR")
            }
        }
    }    
}

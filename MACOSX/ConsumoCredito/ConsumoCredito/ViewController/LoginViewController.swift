//
//  LoginViewController.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 29/09/2020.
//

import UIKit
import Alamofire
import DTTextField

class LoginViewController: UIViewController {
    
    let utility = Utility()
    let savedInformation = SavedInformation()

    @IBOutlet weak var usernameCardView: UIView!
    @IBOutlet weak var passwordCardView: UIView!
    @IBOutlet weak var usernameTextField: DTTextField!
    @IBOutlet weak var passwordTextField: DTTextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var errorTextView: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setUpView()
    }
    
    @IBAction func onLoginButtonClick(_ sender: Any) {
        login()
    }
    
    private func setUpView() {
        utility.createCardView(view: usernameCardView)
        utility.createCardView(view: passwordCardView)
        setUpButton()
        setUpTextField(input: usernameTextField, message: "L'username non può essere vuoto")
        setUpTextField(input: passwordTextField, message: "La password non può essere vuota")
    }
    
    private func setUpButton() {
        self.loginButton.layer.cornerRadius = 10
    }
    
    private func setUpTextField(input: DTTextField, message: String) {
        input.dtborderStyle = .none
        input.floatPlaceholderActiveColor = UIColor(named: "IliadColor")!
        input.errorMessage = message
    }
    
    private func login() {
        let username = usernameTextField.text!
        let password = passwordTextField.text!
        
        if (username.isEmpty) {
            usernameTextField.showError()
            return
        }
        
        if (password.isEmpty) {
            passwordTextField.showError()
            return
        }
        
        sendGBdataToHomePage(username: username, password: password)
    }
    
    private func showLoginError() {
        errorTextView.text = "Username o password errati. Riprova"
        errorTextView.textColor = UIColor.red
        loginButton.shake()
    }
    
    private func saveLoginData(username: String, password: String) {
        let user = User(username: username, password: password)
        let authKey = user.getEncryptedAuth()
        savedInformation.setAuthKey(authKey: authKey)
    }
    
    private func sendGBdataToHomePage(username: String, password: String) {
        let iliadRequest = IliadRequest(active: false)
        let url = iliadRequest.getRoute(route: IliadRoute.Route.ROUTE_GB)
        let headers = iliadRequest.getHeader(username: username, password: password)
        
        AF.request(url, headers: headers).responseJSON { response in
            if let result = response.value {
                let gbConsumptionArray = result as! NSArray
                let gbConsumption = gbConsumptionArray[0] as! String
                self.saveLoginData(username: username, password: password)                
                self.openHomePage(gbConsumption: gbConsumption)
                
            } else {
                debugPrint("LOGIN ERROR")
                self.showLoginError()
            }
        }
    }
    
    private func openHomePage(gbConsumption: String) {
        let storyboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let homePageController = storyboard.instantiateViewController(withIdentifier: "HomePage") as! HomePageViewController
        homePageController.modalPresentationStyle = .fullScreen
        homePageController.gbConsumption = gbConsumption
        self.present(homePageController, animated: false, completion: nil)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }
    
}

//
//  SettingsViewController.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 03/10/2020.
//

import UIKit

class SettingsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    private func openLoginPage() {
        let storyboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let loginPageController = storyboard.instantiateViewController(withIdentifier: "LoginPage") as! LoginViewController
        loginPageController.modalPresentationStyle = .fullScreen
        self.present(loginPageController, animated: false, completion: nil)
    }
    
    private func removeUserAuthKey() {
        let savedInformation = SavedInformation()
        savedInformation.removeAuthKey()
    }
    
    private func openDeleteConfirmDialog() {
        let alert = UIAlertController(title: "Confermi?", message: "Sei sicuro di voler rimuovere i dati di accesso? Sarai costretto a inserire username e password nuovamente.", preferredStyle: .actionSheet)

        alert.addAction(UIAlertAction(title: "Si", style: .default, handler: {action in
            self.removeUserAuthKey()
            self.openLoginPage()
        }))
        alert.addAction(UIAlertAction(title: "No", style: .cancel, handler: nil))

        self.present(alert, animated: true)
    }
    
    @IBAction func onRemoveInfoClick(_ sender: Any) {
        openDeleteConfirmDialog()
    }
    
    static func instantiate() -> SettingsViewController {
            return UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SettingsPage") as! SettingsViewController
    }

}

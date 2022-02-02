//
//  HomePageViewController.swift
//  ConsumoCredito
//
//  Created by Andrea Turconi on 28/09/2020.
//

import UIKit
import Alamofire
import FittedSheets
import UICircularProgressRing

class HomePageViewController: UIViewController {

    var gbConsumption: String = ""
    let iliadRequest = IliadRequest(active: true)
    let utility = Utility()
    
    @IBOutlet weak var renewalCardView: UIView!
    @IBOutlet weak var consumptionCardView: UIView!
    @IBOutlet weak var gbConsumptionProgressBar: UICircularProgressRing!
    @IBOutlet weak var gbRemainingTextView: UILabel!
    @IBOutlet weak var creditResidualTextView: UILabel!
    @IBOutlet weak var dateRenewalTextView: UILabel!
    @IBOutlet weak var callConsumptionTextView: UILabel!
    @IBOutlet weak var smsConsumptionTextView: UILabel!
    @IBOutlet weak var mmsConsumptionTextView: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        utility.createCardView(view: renewalCardView)
        utility.createCardView(view: consumptionCardView)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        setUpProgressBar()
        setRenewalInformation()
        setAllConsumptionInformation()
    }
    
    private func buildBottomSheet() -> SheetViewController {
        let controller = SettingsViewController.instantiate()
        let sheet = SheetViewController(controller: controller, sizes: [.fixed(350)])
        sheet.topCornersRadius = 20
        return sheet
    }
    
    private func openSettingsSheet() {
        let sheet = buildBottomSheet()
        self.present(sheet, animated: true, completion: nil)
    }
    
    @IBAction func onSettingsClick(_ sender: Any) {
        openSettingsSheet()
    }
    
    private func setUpProgressBar() {
        let gbInformation = getInfoFromGbConsumption()
        let gbRemaining = gbInformation[1] - gbInformation[0]
        let gbStrRemaining = String(format: "%.2f", gbRemaining)
        gbRemainingTextView.text = "\(gbStrRemaining) GB" 
        gbConsumptionProgressBar.maxValue = CGFloat(gbInformation[1])
        gbConsumptionProgressBar.minValue = CGFloat(0)
        gbConsumptionProgressBar.startProgress(to: CGFloat(gbRemaining), duration: 3)
    }
    
    private func getInfoFromGbConsumption() -> [Double] {
        let gbUsedStr = gbConsumption.split(separator: "/")[0].trimmingCharacters(in: .whitespacesAndNewlines)
        let gbTotalStr = gbConsumption.split(separator: "/")[1].trimmingCharacters(in: .whitespacesAndNewlines)
        
        var gbUsed = 0.0
        if gbUsedStr.contains("mb") {
            let strFormatted = gbUsedStr.components(separatedBy: "mb")[0].replacingOccurrences(of: ",", with: ".")
            gbUsed = (Double(strFormatted) ?? 0) / 1000.0
        } else {
            let strFormatted = gbUsedStr.components(separatedBy: "GB")[0].replacingOccurrences(of: ",", with: ".")
            gbUsed = Double(strFormatted) ?? 0
        }
        
        let gbTotal = Double(getOnlyDigitsFromString(value: gbTotalStr))!
        
        return [gbUsed, gbTotal]
    }
    
    private func getDateOfRenewal(dateStr: String) -> String {
        let day = dateStr.split(separator: "/")[0]
        let month = dateStr.split(separator: "/")[1]
        let monthStr = Month().getMonthFromValue(value: String(month))
        return "\(day) \(monthStr)"
    }
    
    private func setRenewalInformation() {
        let url = iliadRequest.getRoute(route: IliadRoute.Route.ROUTE_RENEWAL)
        let headers = iliadRequest.getHeader()
        
        AF.request(url, headers: headers).responseJSON { response in
            if let result = response.value {
                let renewalInfoArray = result as! NSArray
                self.creditResidualTextView.text = (renewalInfoArray[0] as! String)
                self.dateRenewalTextView.text = self.getDateOfRenewal(dateStr: renewalInfoArray[1] as! String)
            } else {
                debugPrint("LOGIN ERROR")
            }
        }
    }
    
    private func getOnlyDigitsFromString(value: String) -> String {
        return value.components(separatedBy: CharacterSet.decimalDigits.inverted).joined()
    }
    
    private func setCallConsumption(array: NSArray) {
        var callConsumption = array[0] as! String
        callConsumption = callConsumption.split(separator: ":")[1].trimmingCharacters(in: .whitespacesAndNewlines)
        self.callConsumptionTextView.text = "Chiamate: \(callConsumption)"
    }
    
    private func setSMSConsumption(array: NSArray) {
        var smsConsumption = array[0] as! String
        smsConsumption = getOnlyDigitsFromString(value: smsConsumption)
        self.smsConsumptionTextView.text = "Messaggi inviati: \(smsConsumption)"
    }
    
    private func setMMSConsumption(array: NSArray) {
        var mmsConsumption = array[0] as! String
        mmsConsumption = getOnlyDigitsFromString(value: mmsConsumption)
        self.mmsConsumptionTextView.text = "MMS inviati: \(mmsConsumption)"
    }
    
    private func setAllConsumptionInformation() {
        let url = iliadRequest.getRoute(route: IliadRoute.Route.ROUTE_ALL_INFORMATION)
        let headers = iliadRequest.getHeader()
        
        AF.request(url, headers: headers).responseJSON { response in
            if let result = response.value {
                let allConsumptionArray = result as! NSArray
                self.setCallConsumption(array: allConsumptionArray[0] as! NSArray)
                self.setSMSConsumption(array: allConsumptionArray[1] as! NSArray)
                self.setMMSConsumption(array: allConsumptionArray[3] as! NSArray)
            }
        }
    }
    
}

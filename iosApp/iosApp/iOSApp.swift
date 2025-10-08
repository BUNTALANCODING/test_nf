import SwiftUI
import shared
import GoogleMaps
import UIKit
import SwiftUI
import FirebaseCore

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        GMSServices.provideAPIKey("YOUR_API_KEY")
    }
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

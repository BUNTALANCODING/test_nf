import SwiftUI
import UIKit
import FirebaseCore
import GoogleMaps
import shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        GMSServices.provideAPIKey("YOUR_API_KEY")

    }

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea()
        }
    }
}

import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        DiHelperKt.doInitKoinForIos()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

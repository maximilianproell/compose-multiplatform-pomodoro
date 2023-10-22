import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        DiHelperKt.doInitKoinForIos()
        
        let lifecycleHelper = LifecycleHelper()
        
        NotificationCenter.default.addObserver(
            forName: UIApplication.didBecomeActiveNotification,
            object: nil,
            queue: .main
        ) { (notification: Notification) in
            lifecycleHelper.onAppMovedToForeground()
        }
        
        NotificationCenter.default.addObserver(
            forName: UIApplication.willResignActiveNotification,
            object: nil,
            queue: .main
        ) { (notification: Notification) in
            lifecycleHelper.onAppMovedToBackGround()
        }
    }
    
    
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

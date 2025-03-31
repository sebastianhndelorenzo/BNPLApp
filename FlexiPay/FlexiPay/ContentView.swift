//
//  ContentView.swift
//  BNPL
//
//  Created by Sebastian DeLorenzo on 3/30/25.
//

import SwiftUI

// Define theme colors (if not using Asset Catalog)
// let themeRed = Color.red
// let themeBlue = Color.blue
// If using Asset Catalog, these lines aren't needed.

// Main View hosting the TabView, now with Splash Screen logic
struct ContentView: View {
    // Use AppStorage to persist first launch flag across app restarts
    @AppStorage("hasLaunchedBefore") private var hasLaunchedBefore = false
    // State to control splash screen visibility within the current session
    @State private var showingSplash = true

    var body: some View {
        // Conditionally show Splash Screen or Main TabView
        if showingSplash && !hasLaunchedBefore {
            // --- Splash Screen View ---
            ZStack {
                Color.white.edgesIgnoringSafeArea(.all) // White background
                
                // Make sure you have an image named "AppLogo" in your Assets.xcassets
                Image("AppLogo") 
                    .resizable() // Allows the image to be resized
                    .scaledToFit() // Scales the image to fit the frame while maintaining aspect ratio
                    .frame(width: 150, height: 150) // Adjust size as needed
            }
            .onAppear { 
                // Start a timer when the splash screen appears
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) { // 2-second delay
                    // Use animation for a smoother transition out
                    withAnimation {
                        self.showingSplash = false // Hide splash for this session
                    }
                    // Mark that the app has launched before
                    self.hasLaunchedBefore = true 
                }
            }
        } else {
            // --- Main TabView (Existing Code) ---
            TabView {
                // Tab 1: Subscriptions
                SubscriptionsView()
                    .tabItem {
                        Label("Subscriptions", systemImage: "list.bullet.rectangle.portrait")
                    }

                // Tab 2: Profile
                ProfileView()
                    .tabItem {
                        Label("Profile", systemImage: "person.crop.circle")
                    }

                // Tab 3: Settings
                SettingsView()
                    .tabItem {
                        Label("Settings", systemImage: "gearshape.fill")
                    }
            }
            // Optional: Adjust TabView appearance
            // .tint(.red) // Example: Set the tint color for selected tab items
        }
    }
}

// Define an identifiable item for the sheet
struct SheetItem: Identifiable {
    let id = UUID() // Conformance to Identifiable
    let serviceName: String
}

// Renamed original view to SubscriptionsView
struct SubscriptionsView: View {
    @State private var selectedItem: SheetItem? = nil

    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [.blue.opacity(0.8), .red.opacity(0.6)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .edgesIgnoringSafeArea(.all)
            
            VStack(spacing: 15) {
                Text("Your Subscriptions")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white) // Use white text on the gradient
                    .padding(.top, 40)

                // Subscription options - update the closure action
                SubscriptionOptionCard(
                    serviceName: "JioHotstar",
                    iconName: "film.fill",
                    duration: "1 Week Access",
                    themeColor: .blue,
                    buyAction: { serviceName in
                        selectedItem = SheetItem(serviceName: serviceName) // Set the item
                    }
                )

                SubscriptionOptionCard(
                    serviceName: "SonyLIV",
                    iconName: "play.tv.fill",
                    duration: "1 Week Access",
                    themeColor: .blue,
                    buyAction: { serviceName in
                        selectedItem = SheetItem(serviceName: serviceName) // Set the item
                    }
                )

                SubscriptionOptionCard(
                    serviceName: "Amazon Prime Video",
                    iconName: "play.rectangle.fill",
                    duration: "1 Week Access",
                    themeColor: .blue,
                    buyAction: { serviceName in
                        selectedItem = SheetItem(serviceName: serviceName) // Set the item
                    }
                )

                Spacer() // Push content upwards
            }
            .padding(.horizontal)
        }
        .sheet(item: $selectedItem) { item in
            SubscriptionOptionsSheet(serviceName: item.serviceName)
        }
    }
}

// Placeholder View for Profile
struct ProfileView: View {
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [.blue.opacity(0.8), .red.opacity(0.6)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .edgesIgnoringSafeArea(.all)
            
            Text("Profile Page")
                .font(.largeTitle)
                .foregroundColor(.white)
        }
    }
}

// Placeholder View for Settings
struct SettingsView: View {
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [.blue.opacity(0.8), .red.opacity(0.6)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .edgesIgnoringSafeArea(.all)

            Text("Settings Page")
                .font(.largeTitle)
                .foregroundColor(.white)
        }
    }
}

// Updated Card View with new layout structure and tap gesture
struct SubscriptionOptionCard: View {
    let serviceName: String
    let iconName: String
    let duration: String
    let themeColor: Color
    let buyAction: (String) -> Void

    var body: some View {
        // Apply tap gesture and content shape to the OUTER VStack
        VStack {
            // Main content row
            HStack(alignment: .center, spacing: 15) { // Use standard center alignment
                Image(systemName: iconName)
                    .font(.title)
                    .foregroundColor(themeColor)
                
                // VStack for Text content
                VStack(alignment: .leading) {
                    Text(serviceName)
                        .font(.title2)
                        .fontWeight(.semibold)
                        .foregroundColor(.primary)
                    
                    Text(duration)
                        .font(.headline)
                        .foregroundColor(.secondary)
                }
                
                Spacer() // Push button to the right
                
                Button("Buy Now") { 
                    // Action is now handled by the tap gesture on the VStack
                    // No action here anymore
                }
                .buttonStyle(.bordered)
                .tint(themeColor)
                // Ensure button doesn't interfere with tap gesture (usually fine)
                .allowsHitTesting(false) // Make the button non-interactive
            }
            // Remove any prior alignment guides or specific padding on elements inside HStack
        }
        .padding(20)
        .background(.ultraThinMaterial)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .contentShape(Rectangle()) // Define the tappable area as the whole rectangle
        .onTapGesture { // Add the tap gesture here
            buyAction(serviceName) // Call the action when the card is tapped
        }
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(themeColor.opacity(0.5), lineWidth: 1)
        )
        .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 5)
    }
}

// Updated View for the Subscription Options Sheet using ZStack and Offset
struct SubscriptionOptionsSheet: View {
    let serviceName: String
    @Environment(\.dismiss) var dismiss // Environment value to dismiss the sheet

    // Prices
    let priceDay = "$0.99"
    let priceWeek = "$2.99"
    let priceMonth = "$7.99"
    
    // Email to display
    let emailToCopy = "BNPLTestEmail1@gmail.com"

    // State
    @State private var optionSelected = false
    @State private var showCopiedFeedback = false

    var body: some View {
        NavigationView { 
            VStack(alignment: .leading, spacing: 0) { // Remove spacing here, handle below
                Text("Choose duration for \(serviceName)")
                    .font(.headline)
                    .padding(.bottom, 10)
                    .padding(.horizontal, 20) // Add horizontal padding for title
                
                // Use GeometryReader to get width for offset
                GeometryReader { geometry in
                    ZStack(alignment: .topLeading) { // ZStack holds both views
                        
                        // --- Options View --- 
                        VStack(alignment: .leading, spacing: 6) { 
                           // ... (All 3 Button blocks: 1 Day, 1 Week, 1 Month)
                           // Make sure the button actions still use withAnimation:
                           // withAnimation { optionSelected = true }
                            
                            // Option: 1 Day Button...
                            Button { 
                                print("Selected 1 Day for \(serviceName) at \(priceDay)")
                                withAnimation { optionSelected = true }
                            } label: {
                                HStack {
                                    Text("1 Day Access")
                                        .foregroundColor(.primary)
                                    Spacer()
                                    Text(priceDay)
                                        .fontWeight(.semibold)
                                        .foregroundColor(.primary)
                                }
                            }
                            .buttonStyle(.bordered)
                            
                            // Option: 1 Week Block...
                            VStack(alignment: .leading, spacing: 4) {
                                Button { 
                                    print("Selected 1 Week for \(serviceName) at \(priceWeek)")
                                    withAnimation { optionSelected = true }
                                } label: {
                                    HStack {
                                        Text("1 Week Access")
                                            .foregroundColor(.primary)
                                        Spacer()
                                        Text(priceWeek)
                                            .fontWeight(.semibold)
                                            .foregroundColor(.primary)
                                    }
                                }
                                .buttonStyle(.bordered)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 8)
                                        .stroke(Color.blue, lineWidth: 1.5)
                                )
                                
                                Text("Most Popular")
                                    .font(.caption)
                                    .foregroundColor(.blue)
                                    .padding(.leading, 5)
                            }
                            
                            // Option: 1 Month Button...
                            Button { 
                                print("Selected 1 Month for \(serviceName) at \(priceMonth)")
                                withAnimation { optionSelected = true }
                            } label: {
                                HStack {
                                    Text("1 Month Access")
                                        .foregroundColor(.primary)
                                    Spacer()
                                    Text(priceMonth)
                                        .fontWeight(.semibold)
                                        .foregroundColor(.primary)
                                }
                            }
                            .buttonStyle(.bordered)
                            
                            Spacer() // Takes up remaining space if needed
                        }
                        .padding(.horizontal, 20) // Add padding to match outer VStack
                        .frame(width: geometry.size.width) // Ensure it takes full width
                        .opacity(optionSelected ? 0 : 1) // Fade out when selected
                        .offset(x: optionSelected ? -geometry.size.width : 0) // Slide left when selected
                        // Remove .transition from here

                        // --- Email Display View (Corrected Structure) ---
                        // VStack container for the Email View content
                        VStack(alignment: .leading) {
                            // HStack containing the text and button
                            HStack { 
                                // VStack for the text content
                                VStack(alignment: .leading) {
                                    Text("Use this email to sign in:")
                                        .font(.subheadline)
                                        .foregroundColor(.secondary)
                                    
                                    Text(emailToCopy)
                                        .font(.system(.body, design: .monospaced))
                                        .foregroundColor(.primary)
                                        .lineLimit(1)
                                        .truncationMode(.middle)
                                }
                                    
                                Spacer()
                                
                                // Use if/else to swap Button and Label instantly
                                if showCopiedFeedback {
                                    Label("Copied", systemImage: "checkmark.circle.fill")
                                        .frame(height: 24) // Match Label height
                                        .foregroundColor(.green)
                                        .frame(width: 90, alignment: .trailing) // Match Button frame
                                        .padding(.vertical, 5) // Approximate button internal padding
                                        // Asymmetric transition: Fade IN, instant OUT
                                        .transition(.asymmetric(insertion: .opacity.animation(.easeIn(duration: 0.2)), removal: .identity))
                                } else {
                                    // Original Copy Button - remove action, make non-interactive
                                    Button { 
                                        // Action moved to onTapGesture on HStack
                                    } label: {
                                        Label("Copy", systemImage: "doc.on.doc")
                                            .frame(height: 24) 
                                    }
                                    .frame(width: 90, alignment: .trailing) 
                                    .buttonStyle(.borderless)
                                    .foregroundColor(.blue)
                                    .allowsHitTesting(false) // Make button non-interactive
                                    .transition(.asymmetric(insertion: .opacity.animation(.easeIn(duration: 0.2)), removal: .identity))
                                }
                                
                            }
                            // Apply modifiers and tap gesture to the HStack
                            .padding(.vertical, 8)
                            .padding(.horizontal, 12)
                            .background(.thinMaterial) 
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                            .frame(height: 44) 
                            .contentShape(Rectangle()) // Define tappable area for HStack
                            .onTapGesture { // Add tap gesture to HStack
                                // Only copy if not already showing feedback
                                if !showCopiedFeedback {
                                    UIPasteboard.general.string = emailToCopy
                                    showCopiedFeedback = true
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                        showCopiedFeedback = false
                                    }
                                }
                            }

                            // Spacer to push content up, *inside* the VStack
                            Spacer() 
                        } 
                        // Add .onAppear here to trigger the network call
                        .onAppear {
                            // Call the function to notify the server
                            // IMPORTANT: Replace YOUR_MAC_IP_ADDRESS with your Mac's actual local IP
                            notifyServerUserSawEmail(serverIp: "YOUR_MAC_IP_ADDRESS")
                        }
                        // Apply animation modifiers to the containing VStack
                        .padding(.horizontal, 20) // Keep padding for the overall email view area
                        .frame(width: geometry.size.width) 
                        .opacity(optionSelected ? 1 : 0) 
                        .offset(x: optionSelected ? 0 : geometry.size.width) 
                        // Remove .transition from here

                    }
                    // Apply animation to the ZStack based on the state change
                    .animation(.default, value: optionSelected)
                }
                // Remove Spacer from the outer VStack, handled inside GeometryReader now
            }
            // Remove padding from outer VStack, handled inside GeometryReader now
            .navigationTitle(optionSelected ? "Email for Login" : "Purchase Options") 
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    // Only show Cancel if options are visible, maybe change to Done later?
                    if !optionSelected {
                        Button("Cancel") { dismiss() }
                    }
                }
                // Optional: Add a Done button when email is shown?
                /*
                ToolbarItem(placement: .navigationBarTrailing) {
                    if optionSelected {
                        Button("Done") { dismiss() }
                    }
                }
                */
            }
        }
    }
}

// --- Helper Function to Notify Server ---
// IMPORTANT: You need to replace "YOUR_MAC_IP_ADDRESS" in the onAppear call above
func notifyServerUserSawEmail(serverIp: String) {
    // Construct the URL for your local server
    // Make sure your Mac's firewall allows incoming connections on port 8080
    guard let url = URL(string: "http://\\(serverIp):8080/userSawEmail") else {
        print("Error: Invalid server URL")
        return
    }

    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    // You can add a body if needed, but for a simple ping, it's often not necessary
    // request.httpBody = try? JSONEncoder().encode(["message": "user saw email"])
    // request.setValue("application/json", forHTTPHeaderField: "Content-Type")

    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        if let error = error {
            print("Error sending request to server: \\(error)")
            return
        }
        if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 {
            print("Successfully notified server.")
        } else {
            print("Server returned non-200 status or error.")
        }
    }
    task.resume()
}

#Preview {
    ContentView()
}

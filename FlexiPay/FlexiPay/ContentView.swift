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

// Main View hosting the TabView or Onboarding
struct ContentView: View {
    // Use AppStorage to persist onboarding completion across app restarts
    @AppStorage("hasCompletedOnboarding") private var hasCompletedOnboarding = false
    // State for phone number input
    @State private var phoneNumber: String = ""

    var body: some View {
        // Conditionally show Onboarding or Main TabView
        if !hasCompletedOnboarding {
            OnboardingView(phoneNumber: $phoneNumber, hasCompletedOnboarding: $hasCompletedOnboarding)
                .transition(.move(edge: .leading)) // Add transition
        } else {
            MainTabView()
                .transition(.move(edge: .trailing)) // Add transition
        }
    }
}

// --- Country Data Structure ---
struct Country: Identifiable, Hashable {
    let id = UUID()
    let name: String
    let flag: String
    let code: String // Phone country code (e.g., "+91")
}

// Function to get the sorted country list
func getSortedCountries() -> [Country] {
    // More comprehensive list (expand as needed)
    let allCountries = [
        Country(name: "Afghanistan", flag: "🇦🇫", code: "+93"),
        Country(name: "Albania", flag: "🇦🇱", code: "+355"),
        Country(name: "Algeria", flag: "🇩🇿", code: "+213"),
        Country(name: "Andorra", flag: "🇦🇩", code: "+376"),
        Country(name: "Angola", flag: "🇦🇴", code: "+244"),
        Country(name: "Argentina", flag: "🇦🇷", code: "+54"),
        Country(name: "Armenia", flag: "🇦🇲", code: "+374"),
        Country(name: "Australia", flag: "🇦🇺", code: "+61"),
        Country(name: "Austria", flag: "🇦🇹", code: "+43"),
        Country(name: "Bahamas", flag: "🇧🇸", code: "+1-242"),
        Country(name: "Bahrain", flag: "🇧🇭", code: "+973"),
        Country(name: "Bangladesh", flag: "🇧🇩", code: "+880"),
        Country(name: "Belgium", flag: "🇧🇪", code: "+32"),
        Country(name: "Brazil", flag: "🇧🇷", code: "+55"),
        Country(name: "Canada", flag: "🇨🇦", code: "+1"),
        Country(name: "Chile", flag: "🇨🇱", code: "+56"),
        Country(name: "China", flag: "🇨🇳", code: "+86"),
        Country(name: "Colombia", flag: "🇨🇴", code: "+57"),
        Country(name: "Denmark", flag: "🇩🇰", code: "+45"),
        Country(name: "Egypt", flag: "🇪🇬", code = "+20"),
        Country(name: "Finland", flag: "🇫🇮", code = "+358"),
        Country(name: "France", flag: "🇫🇷", code = "+33"),
        Country(name: "Germany", flag: "🇩🇪", code = "+49"),
        Country(name: "Greece", flag: "🇬🇷", code = "+30"),
        Country(name: "Hong Kong", flag: "🇭🇰", code = "+852"),
        Country(name: "Hungary", flag: "🇭🇺", code = "+36"),
        Country(name: "Iceland", flag: "🇮🇸", code = "+354"),
        Country(name: "India", flag: "🇮🇳", code = "+91"),
        Country(name: "Indonesia", flag: "🇮🇩", code = "+62"),
        Country(name: "Iran", flag: "🇮🇷", code = "+98"),
        Country(name: "Iraq", flag: "🇮🇶", code = "+964"),
        Country(name: "Ireland", flag: "🇮🇪", code = "+353"),
        Country(name: "Israel", flag: "🇮🇱", code = "+972"),
        Country(name: "Italy", flag: "🇮🇹", code = "+39"),
        Country(name: "Jamaica", flag: "🇯🇲", code = "+1-876"),
        Country(name: "Japan", flag: "🇯🇵", code = "+81"),
        Country(name: "Kenya", flag: "🇰🇪", code = "+254"),
        Country(name: "Kuwait", flag: "🇰🇼", code = "+965"),
        Country(name: "Malaysia", flag: "🇲🇾", code = "+60"),
        Country(name: "Mexico", flag: "🇲🇽", code = "+52"),
        Country(name: "Morocco", flag: "🇲🇦", code = "+212"),
        Country(name: "Nepal", flag: "🇳🇵", code = "+977"),
        Country(name: "Netherlands", flag: "🇳🇱", code = "+31"),
        Country(name: "New Zealand", flag: "🇳🇿", code = "+64"),
        Country(name: "Nigeria", flag: "🇳🇬", code = "+234"),
        Country(name: "Norway", flag: "🇳🇴", code = "+47"),
        Country(name: "Oman", flag = "🇴🇲", code = "+968"),
        Country(name: "Pakistan", flag: "🇵🇰", code = "+92"),
        Country(name: "Peru", flag: "🇵🇪", code = "+51"),
        Country(name: "Philippines", flag: "🇵🇭", code = "+63"),
        Country(name: "Poland", flag: "🇵🇱", code = "+48"),
        Country(name: "Portugal", flag: "🇵🇹", code = "+351"),
        Country(name: "Qatar", flag = "🇶🇦", code = "+974"),
        Country(name: "Romania", flag: "🇷🇴", code = "+40"),
        Country(name: "Russia", flag = "🇷🇺", code = "+7"),
        Country(name: "Saudi Arabia", flag = "🇸🇦", code = "+966"),
        Country(name: "Singapore", flag = "🇸🇬", code = "+65"),
        Country(name: "South Africa", flag = "🇿🇦", code = "+27"),
        Country(name: "South Korea", flag = "🇰🇷", code = "+82"),
        Country(name: "Spain", flag: "🇪🇸", code = "+34"),
        Country(name = "Sri Lanka", flag = "🇱🇰", code = "+94"),
        Country(name: "Sweden", flag = "🇸🇪", code = "+46"),
        Country(name: "Switzerland", flag = "🇨🇭", code = "+41"),
        Country(name = "Taiwan", flag = "🇹🇼", code = "+886"),
        Country(name: "Thailand", flag = "🇹🇭", code = "+66"),
        Country(name = "Turkey", flag = "🇹🇷", code = "+90"),
        Country(name: "Uganda", flag = "🇺🇬", code = "+256"),
        Country(name: "Ukraine", flag = "🇺🇦", code = "+380"),
        Country(name = "United Arab Emirates", flag = "🇦🇪", code = "+971"),
        Country(name: "United Kingdom", flag = "🇬🇧", code = "+44"),
        Country(name: "United States", flag = "🇺🇸", code = "+1"),
        Country(name: "Uruguay", flag = "🇺🇾", code = "+598"),
        Country(name = "Vietnam", flag = "🇻🇳", code = "+84"),
        Country(name: "Yemen", flag = "🇾🇪", code = "+967"),
        Country(name: "Zambia", flag = "🇿🇲", code = "+260"),
        Country(name = "Zimbabwe", flag = "🇿🇼", code = "+263")
    ]
    
    // --- Sorting Logic ---
    let india = allCountries.first { $0.code == "+91" }
    let usa = allCountries.first { $0.name == "United States" && $0.code == "+1" }
    
    let sortedRest = allCountries
        .filter { $0 != india && $0 != usa }
        .sorted { $0.name < $1.name }
        
    var finalList = [Country]()
    if let india = india { finalList.append(india) }
    if let usa = usa { finalList.append(usa) }
    finalList.append(contentsOf: sortedRest)
    
    return finalList
}

// --- Helper formatting functions ---
private func formatPhoneNumber(_ digits: String, countryCode: String) -> String {
    switch countryCode {
    case "+91": return formatIndianNumber(digits)
    case "+1": return formatNorthAmericanNumber(digits)
    case "+44": return formatUKNumber(digits)
    // Add other country formats here
    default: return digits // Default: no formatting
    }
}

private func formatIndianNumber(_ digits: String) -> String {
    // Format: XXX XXX XXXX (up to 10 digits)
    let trimmed = String(digits.prefix(10))
    var result = ""
    let count = trimmed.count

    if count > 0 { result += "\(trimmed.prefix(min(count, 3)))" }
    if count > 3 { result += " \(trimmed.dropFirst(3).prefix(min(count-3, 3)))" }
    if count > 6 { result += " \(trimmed.dropFirst(6).prefix(min(count-6, 4)))" }
    
    return result
}

private func formatNorthAmericanNumber(_ digits: String) -> String {
    // Format: (XXX) XXX-XXXX (up to 10 digits)
    let trimmed = String(digits.prefix(10))
    var result = ""
    let count = trimmed.count

    if count > 0 { result += "(\(trimmed.prefix(min(count, 3)))" }
    if count > 3 { result += ") \(trimmed.dropFirst(3).prefix(min(count-3, 3)))" }
    if count > 6 { result += "-\(trimmed.dropFirst(6).prefix(min(count-6, 4)))" }
    
    return result
}

private func formatUKNumber(_ digits: String) -> String {
    // Format: XXXXX XXXXXX (up to 11 digits)
    // Simple format for now: XXXXX XXXXX (10 digits after code)
    let trimmed = String(digits.prefix(10))
    return switch trimmed.count {
    case 1...5: trimmed
    case 6...10: "\(trimmed.prefix(5)) \(trimmed.suffix(trimmed.count - 5))"
    default: trimmed
    }
}

// --- Onboarding View ---
struct OnboardingView: View {
    @Binding var phoneNumber: String // Stores raw digits
    @Binding var hasCompletedOnboarding: Bool
    
    // Get the sorted country list
    let countries = getSortedCountries()
    // State for country selection - default to first item (India)
    @State private var selectedCountry: Country
    // State for the text displayed in the TextField
    @State private var displayedPhoneNumber: String = ""
    
    // Initializer to set default selected country
    init(phoneNumber: Binding<String>, hasCompletedOnboarding: Binding<Bool>) {
        self._phoneNumber = phoneNumber
        self._hasCompletedOnboarding = hasCompletedOnboarding
        // Set the initial state for selectedCountry using the sorted list
        self._selectedCountry = State(initialValue: getSortedCountries().first!)
    }
    
    var isPhoneNumberValid: Bool {
        // Validation should be based on raw digits
        !phoneNumber.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    var body: some View {
        VStack(spacing: 20) {
            Spacer()

            Image("AppLogo") // Reuse the logo
                .resizable()
                .scaledToFit()
                .frame(width: 100, height: 100)
                .padding(.bottom, 30)

            Text("Welcome to FlexiPay")
                .font(.title)
                .fontWeight(.bold)

            Text("Please enter your phone number to get started.")
                .font(.headline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 40)

            // --- Phone Input Row ---
            HStack(spacing: 0) {
                // Country Selection Menu
                Menu {
                    // Use the instance variable 'countries' here
                    ForEach(countries) { country in
                        Button {
                            selectedCountry = country
                        } label: {
                            Text("\(country.flag) \(country.name) (\(country.code))")
                        }
                    }
                } label: {
                    // Label showing selected country flag and code
                    HStack {
                        Text(selectedCountry.flag)
                        Text(selectedCountry.code)
                            .font(.body)
                            .foregroundColor(.primary)
                    }
                    .padding(.horizontal, 12)
                    .frame(height: 50) // Match approximate TextField height
                    .background(Color(.systemGray6))
                }
                .cornerRadius(10, corners: [.topLeft, .bottomLeft]) // Round left corners
                
                // Phone Number TextField - Bound to displayedPhoneNumber
                TextField("Phone Number", text: $displayedPhoneNumber)
                    .keyboardType(.phonePad)
                    .padding(.leading, 10) // Add padding between country code and number
                    .frame(height: 50)
                    .background(Color(.systemGray6))
                    .cornerRadius(10, corners: [.topRight, .bottomRight]) // Round right corners
                    .textContentType(.telephoneNumber) // Helps with autofill
                    .onChange(of: displayedPhoneNumber) { newValue in
                        // 1. Filter input to get raw digits
                        let rawDigits = newValue.filter { $0.isNumber }
                        // 2. Update the raw digit state
                        phoneNumber = rawDigits
                        // 3. Format the raw digits according to the selected country
                        let formatted = formatPhoneNumber(rawDigits, countryCode: selectedCountry.code)
                        // 4. Update the displayed text ONLY if it's different
                        //    (prevents potential infinite loops and helps cursor positioning)
                        if newValue != formatted {
                            displayedPhoneNumber = formatted
                        }
                    }
                    .onChange(of: selectedCountry) { newCountry in
                        // Re-format the existing raw digits when country changes
                        let formatted = formatPhoneNumber(phoneNumber, countryCode: newCountry.code)
                        displayedPhoneNumber = formatted
                    }
            }
            .padding(.horizontal, 40)

            Spacer()

            Button {
                // Use the raw phoneNumber state for logic
                print("Phone number entered: \(selectedCountry.code) \(phoneNumber)")
                withAnimation {
                    hasCompletedOnboarding = true
                }
            } label: {
                Text("Continue")
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .foregroundColor(.white)
                    .background(isPhoneNumberValid ? Color.blue : Color.gray) // Use theme colors if available
                    .cornerRadius(10)
            }
            .disabled(!isPhoneNumberValid)
            .padding(.horizontal, 40)
            .padding(.bottom, 50) // Add padding at the bottom
        }
        .background(Color.white.edgesIgnoringSafeArea(.all)) // Simple white background
    }
}

// Helper extension for corner radius
extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape( RoundedCorner(radius: radius, corners: corners) )
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

// --- Main TabView Structure ---
struct MainTabView: View {
    var body: some View {
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

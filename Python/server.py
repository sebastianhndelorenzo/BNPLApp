#!/usr/bin/env python3
import os
import requests
import random
from flask import Flask, request, jsonify
from flask_cors import CORS  # For handling CORS issues

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# MSG91 Configuration
MSG91_API_KEY = os.environ.get('MSG91_API_KEY')
MSG91_TEMPLATE_ID = os.environ.get('MSG91_TEMPLATE_ID')
MSG91_SENDER_ID = "FLEXIP"  # 6 character sender ID

if not all([MSG91_API_KEY, MSG91_TEMPLATE_ID]):
    print("ERROR: MSG91 credentials not configured. Please set MSG91_API_KEY and MSG91_TEMPLATE_ID environment variables.")

@app.route('/send-verification', methods=['POST'])
def handle_verification():
    if not request.is_json:
        return jsonify({"error": "Request must be JSON"}), 400

    data = request.get_json()
    phone_number = data.get('phoneNumber')  # Get the phone number from request

    if not phone_number:
        return jsonify({"error": "Missing 'phoneNumber' field"}), 400

    # Remove any spaces or special characters from phone number
    phone_number = ''.join(filter(str.isdigit, phone_number))
    
    # Remove +91 or 91 prefix if present
    if phone_number.startswith('91'):
        phone_number = phone_number[2:]
    
    print(f"Attempting to send verification to: {phone_number}")

    # Check for required credentials
    if not all([MSG91_API_KEY, MSG91_TEMPLATE_ID]):
        print("ERROR: Cannot send SMS, MSG91 credentials missing.")
        return jsonify({"error": "Server configuration error (MSG91 credentials)"}), 500

    try:
        # Generate a 6-digit OTP
        otp = ''.join([str(random.randint(0, 9)) for _ in range(6)])
        
        # MSG91 API endpoint
        url = "https://control.msg91.com/api/v5/flow/"
        
        # Request headers
        headers = {
            "authkey": MSG91_API_KEY,
            "Content-Type": "application/json",
        }
        
        # Request payload
        payload = {
            "template_id": MSG91_TEMPLATE_ID,
            "sender": MSG91_SENDER_ID,
            "short_url": "0",  # Disable short URL
            "mobiles": phone_number,
            "VAR1": otp  # Template variable for OTP
        }

        # Make the API request
        response = requests.post(url, json=payload, headers=headers)
        response_data = response.json()

        if response.status_code == 200 and response_data.get('type') == 'success':
            print(f"SMS sent successfully to {phone_number}")
            return jsonify({
                "status": "success",
                "message": "Verification code sent successfully"
            }), 200
        else:
            print(f"Error from MSG91: {response_data}")
            return jsonify({
                "error": f"Failed to send SMS: {response_data.get('message', 'Unknown error')}"
            }), 500

    except Exception as e:
        print(f"Error sending SMS via MSG91: {e}")
        return jsonify({"error": f"Failed to send SMS: {str(e)}"}), 500

if __name__ == '__main__':
    # Run on all network interfaces (0.0.0.0) so your phone can reach it
    # Use a port like 5001. Set debug=False for production.
    print("Starting Flask server on port 5001...")
    app.run(host='0.0.0.0', port=5001, debug=True) 
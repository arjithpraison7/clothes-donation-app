from flask import Flask, request, jsonify

app = Flask(__name__)

# Initialize a dictionary with placeholder data
data_store_donate = {
    'placeholder_1': {
        'title': 'Green Shirt',
        'description': 'New Green Shirt for donation',
        'size': 'M',
        'condition': 'new',
        'type': 'shirt',
        'category': 'men',
        'latitude': 46.7749,
        'longitude': 12.4194
    }
}

data_store_lend = {
    'placeholder_1': {
        'title': 'Coat',
        'description': 'Coat required for program',
        'size': 'M',
        'condition': 'new',
        'type': 'dress',
        'category': 'men',
        'latitude': 47.7749,
        'longitude': 13.4194
    }
}

@app.route('/receive_data_donate', methods=['POST', 'GET'])
def receive_data_donate():
    try:
        if request.method == 'POST':
            # Expect JSON data for POST requests
            data = request.get_json()
            if data is None:
                return jsonify({"error": "Invalid JSON"}), 400
            # Store the data in the dictionary with a unique key
            key = f'item_{len(data_store_donate) + 1}'
            data_store_donate[key] = data
            return jsonify({"message": "Data received successfully!"}), 200
        elif request.method == 'GET':
            # Return the stored data, including placeholders
            return jsonify(data_store_donate), 200
    except Exception as e:
        error_message = f"Error in /receive_data route: {e}"
        print(error_message)
        return jsonify({"error": error_message}), 500


@app.route('/receive_data_lend', methods=['POST', 'GET'])
def receive_data_lend():
    try:
        if request.method == 'POST':
            # Expect JSON data for POST requests
            data = request.get_json()
            if data is None:
                return jsonify({"error": "Invalid JSON"}), 400
            # Store the data in the dictionary with a unique key
            key = f'item_{len(data_store_lend) + 1}'
            data_store_lend[key] = data
            return jsonify({"message": "Data received successfully!"}), 200
        elif request.method == 'GET':
            # Return the stored data, including placeholders
            return jsonify(data_store_lend), 200
    except Exception as e:
        error_message = f"Error in /receive_data route: {e}"
        print(error_message)
        return jsonify({"error": error_message}), 500



if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)

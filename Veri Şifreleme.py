from flask import Flask, request, jsonify

app = Flask(__name__)

# XOR şifreleme fonksiyonu
def xor_encrypt_decrypt(data, key):
    return ''.join(chr(ord(c) ^ key) for c in data)

# Örnek veri
example_data = {
    "message": "denemeeeeeeeeeee",
    "data": [1, 2, 3, 4, 5]
}


xor_key = 123  # Örnek anahtar

# GET isteği ile şifrelenmiş veri döndüren endpoint
@app.route('/api/data', methods=['GET'])
def get_data():
    # Veriyi JSON formatında şifrele
    json_data = jsonify(example_data).get_data(as_text=True)
    encrypted_data = xor_encrypt_decrypt(json_data, xor_key)
    
    return encrypted_data.encode('utf-8')  # byte dizisi olarak döndür

# POST isteği ile veri alan endpoint
@app.route('/api/data', methods=['POST'])
def post_data():
    received_data = request.json
    print("Alınan Veri:", received_data)  # Alınan veriyi terminalde yazdır
    return jsonify({"status": "Başarıyla alındı!", "received": received_data}), 201

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

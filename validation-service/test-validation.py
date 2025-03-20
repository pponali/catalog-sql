import requests
import json
import sys

BASE_URL = "http://localhost:8085/api/validation"

def print_json(data):
    print(json.dumps(data, indent=2))

def test_get_enhanced_rules():
    print("Getting all enhanced validation rules...")
    response = requests.get(f"{BASE_URL}/rules/enhanced")
    if response.status_code == 200:
        print_json(response.json())
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

def test_valid_product():
    print("\nTesting product validation with valid product...")
    valid_product = {
        "name": "Test Product",
        "price": 100,
        "description": "A test product",
        "sku": "ABC-1234"
    }
    response = requests.post(f"{BASE_URL}/product/enhanced", json=valid_product)
    if response.status_code == 200:
        print_json(response.json())
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

def test_invalid_name():
    print("\nTesting product validation with invalid product (name too short)...")
    invalid_product = {
        "name": "Te",
        "price": 100,
        "description": "A test product with invalid name",
        "sku": "ABC-1234"
    }
    response = requests.post(f"{BASE_URL}/product/enhanced", json=invalid_product)
    if response.status_code == 200:
        print_json(response.json())
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

def test_invalid_discount():
    print("\nTesting cross-field validation with invalid discount...")
    invalid_discount = {
        "name": "Test Product",
        "price": 100,
        "discountPrice": 110,
        "description": "A product with invalid discount"
    }
    response = requests.post(f"{BASE_URL}/product/enhanced", json=invalid_discount)
    if response.status_code == 200:
        print_json(response.json())
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

def test_high_discount():
    print("\nTesting with calculated field validation...")
    high_discount = {
        "name": "Test Product",
        "price": 100,
        "discountPrice": 5,
        "description": "A product with 95% discount"
    }
    response = requests.post(f"{BASE_URL}/product/enhanced", json=high_discount)
    if response.status_code == 200:
        print_json(response.json())
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

if __name__ == "__main__":
    print("Running validation tests...")
    try:
        test_get_enhanced_rules()
        test_valid_product()
        test_invalid_name()
        test_invalid_discount()
        test_high_discount()
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)
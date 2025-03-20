#!/bin/bash

echo "Getting all enhanced validation rules..."
http_response=$(wget -qO- http://localhost:8085/api/validation/rules/enhanced)
echo $http_response | python -m json.tool

echo -e "\nTesting product validation with valid product..."
valid_product='{"name":"Test Product","price":100,"description":"A test product","sku":"ABC-1234"}'
http_response=$(wget -qO- --header="Content-Type: application/json" --post-data="$valid_product" http://localhost:8085/api/validation/product/enhanced)
echo $http_response | python -m json.tool

echo -e "\nTesting product validation with invalid product (name too short)..."
invalid_product='{"name":"Te","price":100,"description":"A test product with invalid name","sku":"ABC-1234"}'
http_response=$(wget -qO- --header="Content-Type: application/json" --post-data="$invalid_product" http://localhost:8085/api/validation/product/enhanced)
echo $http_response | python -m json.tool

echo -e "\nTesting cross-field validation with invalid discount..."
invalid_discount='{"name":"Test Product","price":100,"discountPrice":110,"description":"A product with invalid discount"}'
http_response=$(wget -qO- --header="Content-Type: application/json" --post-data="$invalid_discount" http://localhost:8085/api/validation/product/enhanced)
echo $http_response | python -m json.tool

echo -e "\nTesting with calculated field validation..."
high_discount='{"name":"Test Product","price":100,"discountPrice":5,"description":"A product with 95% discount"}'
http_response=$(wget -qO- --header="Content-Type: application/json" --post-data="$high_discount" http://localhost:8085/api/validation/product/enhanced)
echo $http_response | python -m json.tool
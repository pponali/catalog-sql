#!/bin/bash

# Script to update validation rules based on classification attributes
# This script runs the UpdateValidationRulesScript class

# Default paths
CLASSIFICATION_ATTRIBUTES_PATH="DevMDD_Classification_Attributes.csv"
VALIDATION_RULES_PATH="src/main/resources/csv/validation_rules.csv"

# Parse command-line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --classificationAttributesPath=*)
      CLASSIFICATION_ATTRIBUTES_PATH="${1#*=}"
      shift
      ;;
    --validationRulesPath=*)
      VALIDATION_RULES_PATH="${1#*=}"
      shift
      ;;
    *)
      echo "Unknown option: $1"
      echo "Usage: $0 [--classificationAttributesPath=<path>] [--validationRulesPath=<path>]"
      exit 1
      ;;
  esac
done

echo "Starting validation rule update script..."
echo "Using classification attributes path: $CLASSIFICATION_ATTRIBUTES_PATH"
echo "Using validation rules path: $VALIDATION_RULES_PATH"

# Check if the classification attributes file exists
if [ ! -f "$CLASSIFICATION_ATTRIBUTES_PATH" ]; then
  echo "Error: Classification attributes file not found: $CLASSIFICATION_ATTRIBUTES_PATH"
  exit 1
fi

# Run the Java class
java -cp target/catalog-sql.jar com.scaler.cli.UpdateValidationRulesScript "$CLASSIFICATION_ATTRIBUTES_PATH" "$VALIDATION_RULES_PATH"

# Check if the command was successful
if [ $? -eq 0 ]; then
  echo "Validation rule update completed successfully"
else
  echo "Error: Validation rule update failed"
  exit 1
fi

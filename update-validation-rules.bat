@echo off
REM Script to update validation rules based on classification attributes
REM This script runs the UpdateValidationRulesScript class

REM Default paths
SET CLASSIFICATION_ATTRIBUTES_PATH=DevMDD_Classification_Attributes.csv
SET VALIDATION_RULES_PATH=src\main\resources\csv\validation_rules.csv

REM Parse command-line arguments
:parse_args
IF "%~1"=="" GOTO :end_parse_args
SET ARG=%~1
IF "%ARG:~0,29%"=="--classificationAttributesPath=" (
    SET CLASSIFICATION_ATTRIBUTES_PATH=%ARG:~29%
) ELSE IF "%ARG:~0,21%"=="--validationRulesPath=" (
    SET VALIDATION_RULES_PATH=%ARG:~21%
) ELSE (
    ECHO Unknown option: %ARG%
    ECHO Usage: %0 [--classificationAttributesPath=^<path^>] [--validationRulesPath=^<path^>]
    EXIT /B 1
)
SHIFT
GOTO :parse_args
:end_parse_args

ECHO Starting validation rule update script...
ECHO Using classification attributes path: %CLASSIFICATION_ATTRIBUTES_PATH%
ECHO Using validation rules path: %VALIDATION_RULES_PATH%

REM Check if the classification attributes file exists
IF NOT EXIST "%CLASSIFICATION_ATTRIBUTES_PATH%" (
    ECHO Error: Classification attributes file not found: %CLASSIFICATION_ATTRIBUTES_PATH%
    EXIT /B 1
)

REM Run the Java class
java -cp target\catalog-sql.jar com.scaler.cli.UpdateValidationRulesScript "%CLASSIFICATION_ATTRIBUTES_PATH%" "%VALIDATION_RULES_PATH%"

REM Check if the command was successful
IF %ERRORLEVEL% EQU 0 (
    ECHO Validation rule update completed successfully
) ELSE (
    ECHO Error: Validation rule update failed
    EXIT /B 1
)

package com.scaler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToCSVConverter {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ExcelToCSVConverter <excel_file_path>");
            return;
        }

        String excelFilePath = args[0];
        convertExcelToCSV(excelFilePath);
    }

    public static void convertExcelToCSV(String excelFilePath) {
        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the directory of the Excel file
            File excelFile = new File(excelFilePath);
            String outputDir = excelFile.getParent();
            if (outputDir == null) {
                outputDir = ".";
            }

            // Get base filename without extension
            String baseFileName = excelFile.getName();
            int dotIndex = baseFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                baseFileName = baseFileName.substring(0, dotIndex);
            }

            // Process each sheet
            int numberOfSheets = workbook.getNumberOfSheets();
            System.out.println("Processing " + numberOfSheets + " sheets");

            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                String csvFileName = baseFileName + "_" + sheetName + ".csv";
                String csvFilePath = outputDir + File.separator + csvFileName;

                System.out.println("Converting sheet: " + sheetName + " to " + csvFilePath);

                try (FileOutputStream fos = new FileOutputStream(csvFilePath);
                     OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

                    // Iterate through rows
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        StringBuilder csvLine = new StringBuilder();

                        // Iterate through cells
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                            // Add comma if not the first cell
                            if (j > 0) {
                                csvLine.append(",");
                            }

                            // Process cell content
                            String cellValue = getCellValueAsString(cell);

                            // Quote fields with commas, quotes, or newlines
                            if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
                                // Escape quotes by doubling them
                                cellValue = cellValue.replace("\"", "\"\"");
                                csvLine.append("\"").append(cellValue).append("\"");
                            } else {
                                csvLine.append(cellValue);
                            }
                        }

                        // Write line to CSV
                        writer.write(csvLine.toString());
                        writer.write("\n");
                    }

                    System.out.println("Sheet " + sheetName + " converted successfully.");
                }
            }

            System.out.println("All sheets converted successfully.");

        } catch (IOException e) {
            System.err.println("Error converting Excel to CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    // Use toString to avoid scientific notation
                    double numericValue = cell.getNumericCellValue();
                    // Check if it's an integer
                    if (numericValue == Math.floor(numericValue)) {
                        return String.format("%.0f", numericValue);
                    }
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    try {
                        return cell.getStringCellValue();
                    } catch (IllegalStateException e2) {
                        return cell.getCellFormula();
                    }
                }
            default:
                return "";
        }
    }
}

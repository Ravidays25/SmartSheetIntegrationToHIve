package com.smartsheet.impl;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetBuilder;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.RecipientEmail;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ImportDataFromTheFromSmartSheetWeb {
	static Properties prop = new Properties();

	public static void main(final String[] args) {
		try {

			prop.load(ImportDataFromTheFromSmartSheetWeb.class.getClassLoader()
					.getResourceAsStream("smartsheet.properties"));

			Files.deleteIfExists(Paths.get(prop.getProperty("outoutfilepath")));
			String accessToken = prop.getProperty("accessToken");
			String sheetname = prop.getProperty("sheetname");
			if (accessToken == null || accessToken.isEmpty())
				throw new Exception("Please add the correct Accees token in the smartsheet.properties file");

			Smartsheet smartsheet = SmartsheetFactory.createDefaultClient(accessToken);

			PagedResult<Sheet> sheets = smartsheet.sheetResources().listSheets(null, null, null);

			Long sheetId = 0L;
			for (int i = 0; i < sheets.getTotalCount(); i++) {
				if (sheets.getData().get(i).getName().contains(sheetname)) {
					sheetId = sheets.getData().get(i).getId();
				}
			}

			Sheet sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);
			List<Row> rows = sheet.getRows();

			for (int rowNumber = 0; rowNumber < rows.size(); rowNumber++)
				DumpRow(rows.get(rowNumber), sheet.getColumns());

		} catch (Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void DumpRow(Row row, List<Column> columns) throws IOException {
		String delimited = prop.getProperty("delimited");
		String out = null;
		StringBuffer sb = new StringBuffer();
		for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
			out = row.getCells().get(columnNumber).getValue() + "" + delimited;
			sb.append(out);

		}

		Files.write(Paths.get(prop.getProperty("outoutfilepath")), sb.toString().concat("\n").getBytes(),
				StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

}

package com.smartsheet.imp;

import java.io.IOException;
import java.util.Properties;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Sheet;

public class ExportTheSmartSheetToSmartSheetWeb {
	static Properties prop = new Properties();
	public static void main(String[] args) {

		try {
			
			prop.load(ImportDataFromTheFromSmartSheetWeb.class.getClassLoader()
					.getResourceAsStream("smartsheet.properties"));
			String accessToken = prop.getProperty("accessToken");
			String exportfilepath = prop.getProperty("exportfilepath");
			if (accessToken == null || accessToken.isEmpty())
				throw new Exception("Must set API access token in rwsheet.properties file");
			Smartsheet smartsheet = SmartsheetFactory.createDefaultClient(accessToken);
			Sheet sheet = smartsheet.sheetResources().importCsv(exportfilepath,"Quarterly_Commit_vs_Fcst_stage", 0, 0);
			
			/*Sheet sheet = smartsheet.sheetResources().importXlsx(
					"C:/Users/ranaraha/Desktop/GSM/MOS/release-2/Quarterly_Commit_vs_Fcst_stage.xlsx",
					"Quarterly_Commit_vs_Fcst_stage", 0, 0);
			 */
			
			// Load the entire sheet
			sheet = smartsheet.sheetResources().getSheet(sheet.getId(), null, null, null, null, null, null, null);
			System.out.println("Loaded " + sheet.getRows().size() + " rows from sheet: " + sheet.getName());

		} catch (Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}

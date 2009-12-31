package edu.wustl.catissuecore.api.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; //import edu.wustl.catissuecore.domain.*;
import edu.wustl.catissuecore.domain.CollectionProtocol;

/**
 * Class to read data from Microsoft Excel Sheet
 * 
 * 
 */
public class ExcelFileReader {
	private String fileName;
	private HSSFSheet sheet;
	private String[][] data;
	// 0=String, 1= Numeric, 2=Date
	public static Integer[] columnDataType = new Integer[] {};
	public static final String DATE_PATTERN_MM_DD_YYYY = "MM-dd-yyyy";

	/**
	 * Constructor for class which takes Excel file name as input parameter
	 * 
	 * @param fileName
	 *            Excel file to be read
	 * @throws Exception
	 *             Generic Exception
	 */
	public ExcelFileReader(String fileName) throws Exception {
		try {
			this.fileName = fileName;
			this.init();
			this.readData();

		} catch (Exception ex) {
			System.out.println("Exception:" + ex);
		}
	}

	public ExcelFileReader() {
		System.out.println("In Excel file ReaderConstructor");
	}

	public String[][] setInfo(String fileName) throws Exception {

		String allCP[][] = null;
		try {
			this.fileName = fileName;

			init();
			allCP = this.readData();
			System.out.println("In ExcelFileReader excel lenght "
					+ allCP.length);

			// new AddAnticipatedSCGInParticipant().addSCGs(allCP);

		} catch (IOException ex) {
			System.out.println("Exception in ExcelFileReader.setInfo: " + ex);
			throw ex;
		}

		return allCP;

	}

	/**
	 * Method to perform initialization tasks for the class
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		File excelSheet = new File(this.fileName);
		InputStream s = new FileInputStream(excelSheet);
		HSSFWorkbook workbook = new HSSFWorkbook(s);
		sheet = workbook.getSheetAt(0);
		int noOfSheets = workbook.getNumberOfSheets();

	}

	/**
	 * Method to read whole Excel sheet into data structure to avoid repetitive
	 * calls to file IO
	 */
	public String[][] readData() {
		HSSFRow row = null;
		int noOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println(" noOfRows ---" + noOfRows);
		data = new String[noOfRows + 1][];

		int noOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
		System.out.println("No Of columns----" + noOfColumn);
		for (int h = 0; h <= noOfRows; h++) {
			row = sheet.getRow(h);

			if (row != null)
				data[h] = getRowContent(row, noOfColumn);
			else {
				data[h] = new String[noOfColumn];
				Arrays.fill(data[h], "-");
			}
		}
		System.out.println("In Excel file after Read" + data.length);
		return data;
	}

	/**
	 * Method to read content of one row of Excel sheet
	 * 
	 * @param row
	 *            HSSFRow row whose content to be read
	 * @return
	 */
	public String[] getRowContent(HSSFRow row, int noOfColumn) {
		HSSFCell cell = null;
		// int noOfColumn= row.getPhysicalNumberOfCells();
		// System.out.println("No Of columns----"+noOfColumn);
		String[] rowContent = new String[noOfColumn];
		for (short i = 0; i < noOfColumn; i++) {
			try {
				cell = row.getCell(i);
				if (cell == null)
					rowContent[i] = "";
				else {
					//System.out.println("columnDataType.length"+columnDataType.
					// length);
					int cellType = cell.getCellType();
					// System.out.println("cellType"+cellType);
					if (columnDataType.length > i && columnDataType[i] != null) {
						System.out.println("columnDataType[i]"
								+ columnDataType[i]);
						switch (columnDataType[i]) {
						case 0: {

							HSSFRichTextString strCell = cell
									.getRichStringCellValue();
							rowContent[i] = strCell.toString();
							break;
						}
						case 1: {
							rowContent[i] = String.valueOf(cell
									.getNumericCellValue());
							break;
						}
						case 2: {
							Date date = cell.getDateCellValue();
							rowContent[i] = parseDateToString(date,
									DATE_PATTERN_MM_DD_YYYY);
							break;
						}
						}
					} else {
						switch (cellType) {
						case 0: { // System.out.println(i);
							if (i == 12) {
								//System.out.println("there in ExcelFileReader")
								// ;
								Date date = cell.getDateCellValue();
								rowContent[i] = parseDateToString(date,
										DATE_PATTERN_MM_DD_YYYY);
							} else {
								rowContent[i] = String.valueOf(cell
										.getNumericCellValue());
							}
							break;

						}
						case 1: {
							HSSFRichTextString strCell = cell
									.getRichStringCellValue();
							rowContent[i] = strCell.toString();
							break;
						}
						case 2: {
							Date date = cell.getDateCellValue();
							rowContent[i] = parseDateToString(date,
									DATE_PATTERN_MM_DD_YYYY);
							break;
						}
						}
					}
				}

			} catch (Exception e) {
				System.out.println("columnDataType[" + i + "]"
						+ columnDataType[i]);
			}
		}
		return rowContent;
	}

	/**
	 * Method to get number of rows in Excel sheet
	 * 
	 * @return rowCount of Excel sheet
	 */
	public int getRowCount() {
		return sheet.getPhysicalNumberOfRows();
	}

	/**
	 * Method to return a row depending
	 * 
	 * @param index
	 * @return
	 */
	public String[] getRowAt(int index) {
		return data[index];
	}

	public String[][] getAllRows() {
		return data;
	}

	/**
	 * Parses the Date in given format and returns the string representation.
	 * 
	 * @param date
	 *            the Date to be parsed.
	 * @param pattern
	 *            the pattern of the date.
	 * @return
	 */
	public String parseDateToString(Date date, String pattern) {
		String d = "";
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			d = dateFormat.format(date);
		}
		return d;
	}

	/**
	 * Method to set datatype of each column of the excel sheet
	 * 
	 * @param columnDataType
	 */
	public void setColumnDataType(Integer[] columnDataType) {
		ExcelFileReader.columnDataType = columnDataType;
	}
}

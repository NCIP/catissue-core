package com.krishagni.catissueplus.core.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvFileWriter implements CsvWriter {
	private CSVWriter csvWriter;

	public CsvFileWriter(CSVWriter csvWriter) {
		this.csvWriter = csvWriter;
	}
	
	public static CsvFileWriter createCsvFileWriter(OutputStream outputStream) {
		return createCsvFileWriter(new OutputStreamWriter(outputStream));
	}
	
	public static CsvFileWriter createCsvFileWriter(File csvFile) {
		try {
			return createCsvFileWriter(new FileWriter(csvFile));
		} catch (IOException e) {
			throw new CsvException("Error creating CSV file writer", e);
		}
	}
	
	public static CsvFileWriter createCsvFileWriter(Writer writer){
		return createCsvFileWriter(writer, Utility.getFieldSeparator(), CSVWriter.DEFAULT_QUOTE_CHARACTER);
	}
	
	public static CsvFileWriter createCsvFileWriter(Writer writer, char separator, char quotechar){
		CSVWriter csvWriter = new CSVWriter(writer, separator, quotechar);
		return new CsvFileWriter(csvWriter);
	}
	
	@Override
	public void writeAll(List<String[]> allLines) {
		csvWriter.writeAll(allLines);
	}
	
	@Override
	public void writeNext(String[] nextLine) {
		csvWriter.writeNext(nextLine);
	}
	
	@Override
	public void flush() throws IOException {
		csvWriter.flush();
	}
	
	@Override
	public void close() throws IOException {
		try {
			csvWriter.close();
		} catch (IOException e) {
			throw new CsvException("Error closing CSVWriter", e);
		}
	}
}

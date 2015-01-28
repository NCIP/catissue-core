package com.krishagni.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import au.com.bytecode.opencsv.CSVWriter;

public class TableViewer {
	
	private static final String SHOW_COLS_SQL = "show columns from ";
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	private static final String ESCAPE_SEQ = "\t\t\t\t" + NEW_LINE;
	
	private static final String SELECT_SQL = "select * from ";
	
	private static List<String> getColumns(String table, Connection conn) throws Exception {
		String sql = SHOW_COLS_SQL + table;
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		List<String> cols = new ArrayList<String>();
		
		while (rs.next()) {
			cols.add(rs.getString(1));
		}
		
		statement.close();
		return cols;
	}
	
	private static List<String> fetchRow(ResultSet rs, List<String> cols) {
		List<String> row = new ArrayList<String>();
		
		for (String col : cols) {
			try {
				row.add(rs.getString(col)); 
			} catch (Exception e) {
				e.printStackTrace();
				row.add("{ERR_FETCH_COL}");
			}
		}
		return row;
	}
	
//	private static void displayTable(String tableName, Connection conn) throws Exception {
//		List<String> cols = getColumns(tableName, conn);
//
//		Statement statement = conn.createStatement();
//		String sql = SELECT_SQL + tableName;
//		ResultSet rs = statement.executeQuery(sql);
//		int rows = 0;
//		
//		System.out.println("");
//		System.out.println("----------------------------" + tableName + "---------------------------------");
//		
//		while (rs.next()) {
//			rows++;
//			System.out.println(fetchRow(rs, cols));
//		}		
//		
//		if (rows == 0) {
//			System.out.println("No rows found!");
//		}
//		
//		statement.close();
//	}
	

	private static String[] toArray(List<String> rows) {
		String[] array = new String[rows.size()];
		return rows.toArray(array);
	}
	
	public static void logTable(String tableName, Connection connection, String dropFolder) {
		try {
			CSVWriter output = new CSVWriter(new FileWriter(dropFolder + File.separator + tableName + ".csv",true), ',');
			
			//Write Columns
			List<String> columns = getColumns(tableName, connection);
			output.writeNext(toArray(columns));
			
			//Write Rows
			Statement statement = connection.createStatement();
			String sql = SELECT_SQL + tableName;
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				output.writeNext(toArray(fetchRow(rs, columns)));
			}		
			
			statement.close();
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void logTables(ApplicationContext context, String... tables) {
		DataSource ds = context.getBean(DataSource.class);
		try {
			
			String applicationDropFolder = "./application-test-files"; 
			Connection connection = ds.getConnection();
			
			for (String table : tables) {
				try {
					logTable(table, connection, applicationDropFolder);
				} catch (Exception e) {
					System.err.println("Could not load table: " + table );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}

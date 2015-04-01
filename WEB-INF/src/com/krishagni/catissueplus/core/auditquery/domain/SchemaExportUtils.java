package com.krishagni.catissueplus.core.auditquery.domain;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.EnversSchemaGenerator;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaExportUtils {
	public static void main(String args[]) {
		System.out.println("Starting Schema Export...");
		Configuration config = new Configuration().configure();
		config.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect"); 

		SchemaExport export = new EnversSchemaGenerator(config).export().setOutputFile("envers-schema.sql");
		export.execute(true, false, false, true);
		System.out.println("Schema Export Completed...");
	}
}
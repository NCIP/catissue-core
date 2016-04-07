package com.krishagni.catissueplus.core.upgrade;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class CpPpidFormatUpdater implements CustomTaskChange {

	@Override
	public String getConfirmationMessage() {
		return "Collection Protocol PPID format updated successfully";
	}

	@Override
	public void setUp() throws SetupException {
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
	}

	@Override
	public ValidationErrors validate(Database database) {
		return new ValidationErrors();
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		JdbcConnection dbConnection = (JdbcConnection) database.getConnection();
		Statement statement = null;
		Statement updateStmt = null;
		ResultSet rs = null;
		try {
			statement = dbConnection.createStatement();
			updateStmt = dbConnection.createStatement();
			
			rs = statement.executeQuery(GET_ALL_PPID_FORMAT_SQL);
			while (rs.next()) {
				Long cpId = rs.getLong("identifier");
				String ppidFormat = rs.getString("ppid_format");
				
				if (StringUtils.isBlank(ppidFormat)) {
					continue;
				}
				
				String newFormat = getNewFormat(ppidFormat);
				updateStmt.executeUpdate(String.format(UPDATE_PPID_FORMAT_SQL, newFormat, cpId));
			}
		} catch (Exception e) {
			throw new CustomChangeException("Error when updating ppid format: ", e);
		} finally {
			try { rs.close(); } catch (Exception e) {}

			try { statement.close(); } catch (Exception e) {}
			
			try { updateStmt.close(); } catch (Exception e) {}
		}
	}
	
	private String getNewFormat(String existingFormat) {
		Matcher matcher = pattern.matcher(existingFormat);
		if (matcher.find()) {
			existingFormat = existingFormat.replace(matcher.group(), String.format(PPID_FORMAT, matcher.group(1)));
		}
		
		return existingFormat;
	}
	
	private Pattern pattern = Pattern.compile("%0(.+?)d");
	
	private static final String PPID_FORMAT = "%%CP_UID(%s)%%";
	
	private static final String GET_ALL_PPID_FORMAT_SQL = "select identifier, ppid_format from catissue_collection_protocol";
	
	private static final String UPDATE_PPID_FORMAT_SQL = "update catissue_collection_protocol set ppid_format = '%s' where identifier = %d";
}

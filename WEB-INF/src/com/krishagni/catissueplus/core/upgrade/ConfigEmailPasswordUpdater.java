package com.krishagni.catissueplus.core.upgrade;

import java.sql.ResultSet;
import java.sql.Statement;

import com.krishagni.catissueplus.core.common.util.Utility;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class ConfigEmailPasswordUpdater implements CustomTaskChange {
	@Override
	public String getConfirmationMessage() {
		return "Email account password updated successfully.";
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
		ResultSet rs = null;
		try {
			statement = dbConnection.createStatement();
			rs = statement.executeQuery(GET_EMAIL_PASSWORD_SQL);
			
			String password = null;
			Long id = null;
			if (rs.next()) {
				id = rs.getLong("identifier");
				password = rs.getString("value");
			}
			
			if (password != null) {
				password = Utility.encrypt(password);
				statement.executeUpdate(String.format(UPDATE_PASSWORD_SQL, password, id));
			}
		} catch (Exception e) {
			throw new CustomChangeException("Error when encrypting email account password: ", e);
		} finally {
			try { rs.close(); } catch (Exception e) {}

			try { statement.close(); } catch (Exception e) {
			}
		}
	}
	
	public static final String GET_EMAIL_PASSWORD_SQL = 
			"select " + 
			"  s.identifier, s.value " +
			"from "	+
			"  os_modules m" +
			"  inner join os_cfg_props p on p.module_id = m.identifier" +
			"  inner join os_cfg_settings s on s.property_id = p.identifier " +
			"where " +
			"  m.name = 'email' and " +
			"  p.name = 'account_password' and " +
			"  s.activity_status = 'Active'";

	public static final String UPDATE_PASSWORD_SQL = "update os_cfg_settings set value = '%s' where identifier = %d";
}
package com.krishagni.catissueplus.core.upgrade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.util.CsvMapReader;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class MoveAbbreviationsToPvProps implements CustomTaskChange {
	@Override
	public String getConfirmationMessage() {
		return "Specimen type and pathology status abbreviations successfully moved to PV properties table";
	}

	@Override
	public void setUp() throws SetupException {

	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {

	}

	@Override
	public ValidationErrors validate(Database database) {
		return null;
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		JdbcConnection conn = (JdbcConnection) database.getConnection();
		try {
			Map<String, String> typeAbbrs = getAbbreviations(conn, SPMN_TYPE_ABBRS, DEF_SPMN_TYPE_ABBR_FILE);
			moveAbbreviations(conn, SPMN_TYPE, typeAbbrs);

			Map<String, String> pathStatusAbbrs = getAbbreviations(conn, PATH_STATUS_ABBRS, DEF_PATH_STATUS_ABBR_FILE);
			moveAbbreviations(conn, PATH_STATUS, pathStatusAbbrs);
		} catch (Exception e) {
			throw new CustomChangeException("Error while migrating abbreviations: ", e);
		}
	}

	private Map<String, String> getAbbreviations(JdbcConnection conn, String propertyName, String defaultFilePath)
	throws Exception {
		return CsvMapReader.getMap(getAbbrFilePath(conn, propertyName, defaultFilePath));
	}

	private String getAbbrFilePath(JdbcConnection conn, String propertyName, String defaultFilePath)
	throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(GET_ABBR_FILE_PATH_SQL);
			stmt.setString(1, propertyName);
			rs = stmt.executeQuery();

			String filePath = defaultFilePath;
			if (rs.next()) {
				String result = rs.getString("value");
				if (StringUtils.isNotBlank(result)) {
					filePath = result;
				}
			}

			return filePath;
		} finally {
			close(rs);
			close(stmt);
		}
	}

	private void moveAbbreviations(JdbcConnection conn, String pvAttr, Map<String, String> abbrMap)
	throws Exception {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(INSERT_PV_PROP_SQL);
			for (Map.Entry<String, Long> pvId : getPvIds(conn, pvAttr, abbrMap.keySet()).entrySet()) {
				String abbr = abbrMap.get(pvId.getKey());
				if (StringUtils.isBlank(abbr)) {
					continue;
				}

				int idx = 0;
				stmt.setLong(++idx, pvId.getValue());
				stmt.setString(++idx, "abbreviation");
				stmt.setString(++idx, abbr);
				stmt.addBatch();
			}

			stmt.executeBatch();
		} catch (Exception e) {
			throw new CustomChangeException("Error while migrating abbreviations for: " + pvAttr, e);
		} finally {
			close(stmt);
		}
	}

	private Map<String, Long> getPvIds(JdbcConnection conn, String pvAttr, Set<String> pvs)
	throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			int idx = 0;
			String placeholders = pvs.stream().map(pv -> "?").collect(Collectors.joining(", "));

			stmt = conn.prepareStatement(String.format(GET_PV_ID_SQL, placeholders));
			stmt.setString(++idx, pvAttr);
			for (String pv : pvs) {
				stmt.setString(++idx, pv);
			}

			rs = stmt.executeQuery();

			Map<String, Long> pvIds = new HashMap<>();
			while (rs.next()) {
				pvIds.put(rs.getString("value"), rs.getLong("id"));
			}

			return pvIds;
		} finally {
			close(rs);
			close(stmt);
		}
	}

	private void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {

		}
	}

	private void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {

		}
	}

	private static final String GET_ABBR_FILE_PATH_SQL =
		"select " +
		"  cs.value as value " +
		"from " +
		"  os_cfg_settings cs " +
		"  inner join os_cfg_props cp on cs.property_id = cp.identifier " +
		"where " +
		"  cp.name = ? and " +
		"  activity_status='Active'";

	private static final String GET_PV_ID_SQL =
		"select " +
		"  identifier as id, value as value " +
		"from " +
		"  catissue_permissible_value " +
		"where " +
		"  public_id = ? and " +
		"  value in (%s)";

	private static final String INSERT_PV_PROP_SQL =
		"insert into os_pv_props (pv_id, name, value) values(?, ?, ?)";

	private static final String SPMN_TYPE = "specimen_type";

	private static final String SPMN_TYPE_ABBRS = "specimen_type_abbr_map";

	private static final String PATH_STATUS = "pathology_status";

	private static final String PATH_STATUS_ABBRS = "spmn_path_status_abbr_map";

	private static final String DEF_SPMN_TYPE_ABBR_FILE =
		"classpath:/com/krishagni/catissueplus/core/biospecimen/specimen-type-abbr.csv";

	private static final String DEF_PATH_STATUS_ABBR_FILE =
		"classpath:/com/krishagni/catissueplus/core/biospecimen/specimen-path-status-abbr.csv";
}
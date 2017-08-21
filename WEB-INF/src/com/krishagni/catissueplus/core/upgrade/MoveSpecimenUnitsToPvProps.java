package com.krishagni.catissueplus.core.upgrade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class MoveSpecimenUnitsToPvProps implements CustomTaskChange{
	@Override
	public String getConfirmationMessage() {
		return "Specimen units successfully moved to PV properties table";
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

		PreparedStatement insertStmt = null;
		try {
			insertStmt = conn.prepareStatement(INSERT_PV_PROP_SQL);

			for (Map<String, Object> unitProp : getSpecimenUnitProps(conn)) {
				Long pvId = (Long)unitProp.get("pvId");
				addUnitProp(insertStmt, pvId, unitProp, "quantity_unit");
				addUnitProp(insertStmt, pvId, unitProp, "quantity_display_unit");
				addUnitProp(insertStmt, pvId, unitProp, "concentration_unit");
				addUnitProp(insertStmt, pvId, unitProp, "concentration_display_unit");
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			throw new CustomChangeException("Error while migrating specimen units: ", e);
		} finally {
			close(insertStmt);
		}
	}

	private List<Map<String, Object>> getSpecimenUnitProps(JdbcConnection conn)
	throws Exception {
		PreparedStatement unitsStmt = null;
		ResultSet unitsRs = null;

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			unitsStmt = conn.prepareStatement(GET_SPECIMEN_UNITS_SQL);
			unitsRs = unitsStmt.executeQuery();

			while (unitsRs.next()) {
				Map<String, Object> typeUnits = new HashMap<>();
				typeUnits.put("pvId", unitsRs.getLong("pv_id"));
				typeUnits.put("specimen_class", unitsRs.getString("specimen_class"));
				typeUnits.put("specimen_type", unitsRs.getString("specimen_type"));
				typeUnits.put("quantity_unit", unitsRs.getString("qty_unit"));
				typeUnits.put("quantity_display_unit", unitsRs.getString("qty_html_display_code"));
				typeUnits.put("concentration_unit", unitsRs.getString("conc_unit"));
				typeUnits.put("concentration_display_unit", unitsRs.getString("conc_html_display_code"));

				result.add(typeUnits);
			}

			return result;
		} finally {
			close(unitsRs);
			close(unitsStmt);
		}
	}

	private void addUnitProp(PreparedStatement stmt, Long pvId, Map<String, Object> unitProps, String prop)
	throws Exception {
		String value = (String)unitProps.get(prop);
		if (StringUtils.isBlank(value)) {
			return;
		}

		stmt.setLong(1, pvId);
		stmt.setString(2, prop);
		stmt.setString(3, value);
		stmt.addBatch();
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

	private static final String GET_SPECIMEN_UNITS_SQL =
			"select " +
			"  pv.identifier as pv_id, u.specimen_class, u.specimen_type, " +
			"  u.qty_unit, u.qty_html_display_code, u.conc_unit, u.conc_html_display_code " +
			"from " +
			"  os_specimen_units u " +
			"  inner join catissue_permissible_value pv on (" +
			"    (u.specimen_type is null and u.specimen_class = pv.value and pv.parent_identifier is null) or " +
			"    (u.specimen_type is not null and u.specimen_type = pv.value and pv.parent_identifier is not null)" +
			"  ) " +
			"where " +
			"  u.activity_status = 'Active'";

	private static final String INSERT_PV_PROP_SQL =
			"insert into os_pv_props (pv_id, name, value) values(?, ?, ?)";
}
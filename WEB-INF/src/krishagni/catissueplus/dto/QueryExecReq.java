package krishagni.catissueplus.dto;

public class QueryExecReq {
	private String drivingForm;
	
	private Long cpId;
	
	private String aql;
	
	private boolean wideRows = false;

	public String getDrivingForm() {
		return drivingForm;
	}

	public void setDrivingForm(String drivingForm) {
		this.drivingForm = drivingForm;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}
	
	public boolean isWideRows() {
		return wideRows;
	}

	public void setWideRows(boolean wideRows) {
		this.wideRows = wideRows;
	}	
}

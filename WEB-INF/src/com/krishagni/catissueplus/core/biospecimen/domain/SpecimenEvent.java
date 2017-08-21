package com.krishagni.catissueplus.core.biospecimen.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.de.domain.DeObject;

import edu.common.dynamicextensions.domain.nui.DatePicker;

@Configurable
public abstract class SpecimenEvent extends DeObject {
	private User user;
	
	private Date time;
	
	private String comments;
	
	private Specimen specimen;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ConfigurationService cfgSvc;
	
	public SpecimenEvent(Specimen specimen) {
		super(true);
		this.specimen = specimen;
	}

	public User getUser() {
		loadRecordIfNotLoaded();
		return user;
	}

	public void setUser(User user) {
		loadRecordIfNotLoaded();
		this.user = user;
	}

	public Date getTime() {
		loadRecordIfNotLoaded();
		return time;
	}

	public void setTime(Date time) {
		loadRecordIfNotLoaded();
		this.time = time;
	}

	public String getComments() {
		loadRecordIfNotLoaded();
		return comments;
	}

	public void setComments(String comments) {
		loadRecordIfNotLoaded();
		this.comments = comments;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}
	
	public void update(SpecimenEvent other) {
		setUser(other.getUser());
		setTime(other.getTime());
		setComments(other.getComments());
	}
	
	public Map<String, Object> getAttrValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("user", getUser().getId());
		vals.put("time", new SimpleDateFormat(cfgSvc.getDeDateTimeFormat()).format(time)); 
		vals.put("comments", getComments());
		
		vals.putAll(getEventAttrs());
		return vals;
	}
	
	public void setAttrValues(Map<String, Object> values) {
		String userIdStr = (String)values.get("user");
		if (StringUtils.isNotBlank(userIdStr)) {
			Long userId = Long.parseLong(userIdStr);
			this.user = userDao.getById(userId);
		}
		
		String timeStr = (String)values.get("time");
		if (StringUtils.isNotBlank(timeStr)) {
			this.time = new DatePicker().fromString(timeStr);
		}
				
		this.comments = (String)values.get("comments");		
		setEventAttrs(values);
	}

	@Override
	public Long getObjectId() {
		return specimen.getId();
	}

	
	@Override
	public String getEntityType() {		
		return "SpecimenEvent";
	}

	@Override
	public Long getCpId() {
		return specimen.getVisit().getCollectionProtocol().getId();
	}
		
	protected abstract Map<String, Object> getEventAttrs();
	
	protected abstract void setEventAttrs(Map<String, Object> attrValues);
}

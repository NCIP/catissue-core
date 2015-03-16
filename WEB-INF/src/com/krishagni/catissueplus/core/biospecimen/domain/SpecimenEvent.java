package com.krishagni.catissueplus.core.biospecimen.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.de.domain.DeObject;

@Configurable
public abstract class SpecimenEvent extends DeObject {
	private User user;
	
	private Date time;
	
	private String comments;
	
	private Specimen specimen;
	
	@Autowired
	private UserDao userDao;
	
	public SpecimenEvent(Specimen specimen) {
		this.specimen = specimen;
	}

	public User getUser() {
		loadRecordIfNotLoaded();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		loadRecordIfNotLoaded();
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getComments() {
		loadRecordIfNotLoaded();
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}	
	
	public Map<String, Object> getAttrValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("user", user.getId());
		vals.put("time", new SimpleDateFormat("MM-dd-yyyy HH:mm").format(time)); // TODO: take date format from locale 
		vals.put("comments", comments);
		
		vals.putAll(getEventAttrs());
		return vals;
	}
	
	public void setAttrValues(Map<String, Object> values) {
		Number userId = (Number)values.get("user");
		if (userId != null) {
			this.user = userDao.getById(userId.longValue());
		}
		this.time = (Date)values.get("time");
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

package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;

@JsonFilter("withoutId")
public class ConsentTierDetail {
	private Long cpId;
	
	private Long id;
	
	private String statement;
	
	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	
	public static ConsentTierDetail from(ConsentTier ct) {
		if (ct == null) {
			return null;
		}
		
		ConsentTierDetail detail = new ConsentTierDetail();
		detail.setId(ct.getId());
		detail.setStatement(ct.getStatement());
		return detail;
	}
	
	public static List<ConsentTierDetail> from(Collection<ConsentTier> cts) {
		List<ConsentTierDetail> tiers = new ArrayList<ConsentTierDetail>();
		
		for (ConsentTier ct : cts) {
			tiers.add(ConsentTierDetail.from(ct));
		}
		
		return tiers;
	}	
	
	public ConsentTier toConsentTier() {
		ConsentTier ct = new ConsentTier();
		ct.setId(id);
		ct.setStatement(statement);
		return ct;
	}
}

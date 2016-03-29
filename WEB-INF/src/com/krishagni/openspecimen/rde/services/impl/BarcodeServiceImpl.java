package com.krishagni.openspecimen.rde.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.VisitBarcodeDetail;
import com.krishagni.openspecimen.rde.services.BarcodeError;
import com.krishagni.openspecimen.rde.services.BarcodeService;

public class BarcodeServiceImpl implements BarcodeService {

	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitBarcodeDetail>> validate(RequestEvent<List<String>> req) {
		try {
			return ResponseEvent.response(validate(req.getPayload()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public List<VisitBarcodeDetail> validate(List<String> barcodes) {
		List<VisitBarcodeDetail> result = new ArrayList<VisitBarcodeDetail>();
		
		Map<String, Object> objCache = new HashMap<String, Object>();
		for (String barcode : barcodes) {
			result.add(getBarcodeDetail(barcode, objCache));				
		}
		
		return result;
	}
		
	private VisitBarcodeDetail getBarcodeDetail(String barcode, Map<String, Object> objCache) {
		BarcodeParser parser = new BarcodeParser(barcode, objCache);
		return parser.getVisitBarcodeDetail();
	}
	
	private class BarcodeParser {
		private CollectionProtocol cp;
		
		private VisitBarcodeDetail result = new VisitBarcodeDetail();
		
		private Map<String, BarcodeError> errorMap = new HashMap<String, BarcodeError>();
		
		private String barcode;
		
		private Map<String, Object> objCache;
		
		public BarcodeParser(String barcode, Map<String, Object> objCache) {
			this.barcode = barcode;
			this.objCache = objCache;
			this.result.setErrorMap(errorMap);
		}
				
		public VisitBarcodeDetail getVisitBarcodeDetail() {			
			if (StringUtils.isEmpty(barcode)) {
				errorMap.put("general", BarcodeError.EMPTY);
				return result;				
			}
			
			String[] parts = barcode.split("-");
			int idx = -1;
			
			handleCpCode(parts, ++idx);
			handlePpid(parts, ++idx);
			handleCpeCode(parts, ++idx);
			handleSiteCode(parts, ++idx);
			handleCohortCode(parts, ++idx);
			
			if ((parts.length - 1) > idx) {
				errorMap.put("general", BarcodeError.INVALID);
			}
			
			result.setBarcode(barcode);
			return result;			
		}
		
		private void handleCpCode(String[] parts, int idx) {
			if (idx >= parts.length) {
				errorMap.put("general", BarcodeError.INVALID);
				errorMap.put("cp", BarcodeError.NO_CP_CODE);
				return;
			}
			
			String cpCode = parts[idx];
			result.setCpCode(cpCode);
			
			if (objCache.containsKey("$cp." + cpCode)) {
				cp = (CollectionProtocol)objCache.get("$cp." + cpCode); 
			} else {
				cp = daoFactory.getCollectionProtocolDao().getCpByCode(cpCode);
				objCache.put("$cp." + cpCode, cp);
			}

			if (cp == null) {
				errorMap.put("cp", BarcodeError.INVALID_CP_CODE);				
			} else {
				result.setCpId(cp.getId());
				result.setCpShortTitle(cp.getShortTitle());
			}			
		}
		
		private void handlePpid(String[] parts, int idx) {
			if (idx >= parts.length) {
				errorMap.put("general", BarcodeError.INVALID);
				errorMap.put("ppid", BarcodeError.NO_PPID_CODE);
				return;
			}
			
			String ppid = result.getCpCode() + "-" + parts[idx];
			result.setPpid(ppid);
			result.setPpidCode(parts[idx]);
			
			if (cp == null) {
				return;
			}
						
			if (!cp.isValidPpid(ppid)) {
				errorMap.put("ppid", BarcodeError.INVALID_PPID_CODE);
			}			
		}
		
		private void handleCpeCode(String[] parts, int idx) {
			if (idx >= parts.length) {
				errorMap.put("general", BarcodeError.INVALID);
				errorMap.put("cpe", BarcodeError.NO_CPE_CODE);
				return;
			}
			
			String cpeCode = parts[idx];
			result.setCpeCode(cpeCode);
			
			if (cp == null) {
				return;
			}

			CollectionProtocolEvent cpe = null;
			String key = "$cpe." + cp.getShortTitle() + "." + cpeCode; 
			if (objCache.containsKey(key)) {
				cpe = (CollectionProtocolEvent)objCache.get(key); 
			} else {
				cpe = daoFactory.getCollectionProtocolDao().getCpeByCode(cp.getShortTitle(), cpeCode);
				objCache.put(key, cpe);
			}
			
			if (cpe == null) {
				errorMap.put("cpe", BarcodeError.INVALID_CPE_CODE);
				return;
			}
			
			result.setCpeId(cpe.getId());
			result.setCpeLabel("T" + cpe.getEventPoint().intValue() + ": " + cpe.getEventLabel());
		}
		
		private void handleSiteCode(String[] parts, int idx) {
			if (idx >= parts.length) {
				errorMap.put("general", BarcodeError.INVALID);
				errorMap.put("site", BarcodeError.NO_SITE_CODE);
				return;
			}
			
			String siteCode = parts[idx];
			result.setSiteCode(siteCode);

			Site site = null;
			if (objCache.containsKey("$site." + siteCode)) {
				site = (Site)objCache.get("$site." + siteCode); 
			} else {
				site = daoFactory.getSiteDao().getSiteByCode(siteCode);
				objCache.put("$site." + siteCode, site);
			}
			
			if (site == null) {
				errorMap.put("site", BarcodeError.INVALID_SITE_CODE);
				return;
			}
			
			if (cp == null) {
				return;
			}
			
			if (!cp.getRepositories().contains(site)) {
				errorMap.put("site", BarcodeError.INVALID_CP_SITE);
				return;
			}
			
			result.setSiteId(site.getId());
			result.setSiteName(site.getName());
		}
		
		private void handleCohortCode(String[] parts, int  idx) {
			if (idx >= parts.length) {
				errorMap.put("general", BarcodeError.INVALID);
				errorMap.put("cohort", BarcodeError.NO_COHORT_CODE);
				return;
			}
			
			String cohortCode = parts[idx];
			result.setCohortCode(cohortCode);

			PermissibleValue pv = null;
			if (objCache.containsKey("$cohort." + cohortCode)) {
				pv = (PermissibleValue)objCache.get("$cohort." + cohortCode); 
			} else {
				pv = daoFactory.getPermissibleValueDao().getByConceptCode("cohort", cohortCode);
				objCache.put("$cohort." + cohortCode, pv);
			}
			 
			if (pv == null) {
				errorMap.put("cohort", BarcodeError.INVALID_COHORT_CODE);
				return;
			}
			
			result.setCohort(pv.getValue());
		}
	}	
}

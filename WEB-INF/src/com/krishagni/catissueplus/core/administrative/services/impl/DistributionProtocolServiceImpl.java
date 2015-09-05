
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class DistributionProtocolServiceImpl implements DistributionProtocolService {
	private DaoFactory daoFactory;

	private DistributionProtocolFactory distributionProtocolFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionProtocolFactory(DistributionProtocolFactory distributionProtocolFactory) {
		this.distributionProtocolFactory = distributionProtocolFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<DpListCriteria> req) {
		try {
			DpListCriteria crit = req.getPayload();
			AccessCtrlMgr.getInstance().ensureReadDpRights();
			if (!AuthUtil.isAdmin()) {
				User user = daoFactory.getUserDao().getById(AuthUtil.getCurrentUser().getId());
				crit.instituteId(user.getInstitute().getId());
			}
			
			List<DistributionProtocol> dps = daoFactory.getDistributionProtocolDao().getDistributionProtocols(crit);
			List<DistributionProtocolDetail> result = DistributionProtocolDetail.from(dps);
			
			if (crit.includeStat()) {
				addDpStats(result);
			}
						
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadDpRights();
			
			Long protocolId = req.getPayload();
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol dp = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(dp, null);
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(dp);
			return ResponseEvent.response(DistributionProtocolDetail.from(dp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
						
			Long protocolId = req.getPayload().getId();
			String title = req.getPayload().getTitle();
			
			DistributionProtocol existing = null;
			if (protocolId != null) {
				existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			} else {
				existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(title);
			}
			
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
		
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(distributionProtocol, existing);
			
			existing.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void addDpStats(List<DistributionProtocolDetail> dps) {
		if (CollectionUtils.isEmpty(dps)) {
			return;
		}
		
		Map<Long, DistributionProtocolDetail> dpMap = new HashMap<Long, DistributionProtocolDetail>();
		for (DistributionProtocolDetail dp : dps) {
			dpMap.put(dp.getId(), dp);
		}
				
		Map<Long, Integer> countMap = daoFactory.getDistributionProtocolDao()
				.getSpecimensCountByDpIds(dpMap.keySet());
		
		for (Map.Entry<Long, Integer> count : countMap.entrySet()) {
			dpMap.get(count.getKey()).setDistributedSpecimensCount(count.getValue());
		}		
	}
	
	private void ensureUniqueConstraints(DistributionProtocol newDp, DistributionProtocol existingDp) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_TITLE);
		}
		
		if (!isUniqueShortTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_SHORT_TITLE);
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getTitle().equals(existingDp.getTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(newDp.getTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}

	private boolean isUniqueShortTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getShortTitle().equals(existingDp.getShortTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getByShortTitle(newDp.getShortTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderDetail>> getDpHistory(RequestEvent<Long> req) {
		return ResponseEvent.response(getOrdersData(req.getPayload()));
	}
	
	public List<DistributionOrderDetail> getOrdersData(Long id){
		List<DistributionOrderDetail> orders = new ArrayList<DistributionOrderDetail>();
		
		DistributionOrderDetail order1 = new DistributionOrderDetail();
		order1.setName("Distributed to Prof Tin");
		order1.setExecutionDate(new Date(1441174401000L));
		order1.setSpecimenCnt(20L);
		SpecimenInfo spmn = new SpecimenInfo();
		spmn.setType("DNA");
		spmn.setAnatomicSite("Lung");
		spmn.setPathology("Malignant");
		DistributionOrderItemDetail orderDetail = new DistributionOrderItemDetail();
		orderDetail.setSpecimen(spmn);
		List<DistributionOrderItemDetail> orderList = new ArrayList<DistributionOrderItemDetail>();
		orderList.add(orderDetail);
		order1.setOrderItems(orderList);
		
		DistributionOrderDetail order2 = new DistributionOrderDetail();
		order2.setName("Distributed to Prof Tin");
		order2.setExecutionDate(new Date(1362002400000L));
		order2.setSpecimenCnt(508L);
		SpecimenInfo spmn1 = new SpecimenInfo();
		spmn1.setType("RNA");
		spmn1.setAnatomicSite("Lung");
		spmn1.setPathology("Malignant");
		DistributionOrderItemDetail orderDetail1 = new DistributionOrderItemDetail();
		orderDetail1.setSpecimen(spmn1);
		List<DistributionOrderItemDetail> orderList1 = new ArrayList<DistributionOrderItemDetail>();
		orderList1.add(orderDetail1);
		order2.setOrderItems(orderList1);
		
		orders.add(order1);
		orders.add(order2);
		
		return orders;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<File> exportHistory(RequestEvent<Long> req) {
		final String filename = UUID.randomUUID().toString();
		List<String[]> data = new ArrayList<String[]>();
		String[] header = {"Order Name", "Distribution Date", "Specimen Type", "Anatomic Site", "Pathology Status", "Specimen Distributed"};
		data.add(header);
		
		List<DistributionOrderDetail> orders = getOrdersData(req.getPayload());
		
		for(int i=0; i<orders.size(); i++) {
			DistributionOrderDetail orderDetail = orders.get(i);
			List<String> row = new ArrayList<String>();
			
			row.add(orderDetail.getName());
			row.add(orderDetail.getExecutionDate().toString());
			row.add(orderDetail.getOrderItems().get(0).getSpecimen().getType());
			row.add(orderDetail.getOrderItems().get(0).getSpecimen().getAnatomicSite());
			row.add(orderDetail.getOrderItems().get(0).getSpecimen().getPathology());
			row.add(orderDetail.getSpecimenCnt().toString());
			data.add(row.toArray(new String[row.size()]));
		}
		
		FileWriter csvFile;
		CSVWriter csvWriter = null;
		try {
			csvFile = new FileWriter(getExportDataDir() + filename);
			csvWriter = new CSVWriter(csvFile);
			csvWriter.writeAll(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (Exception e) {					
				}				
			}
		}
		
		File file = new File(getExportDataDir() + filename);
		return ResponseEvent.response(file);
	}
	
	private static String getExportDataDir() {
		String path = new StringBuilder()
			.append(ConfigUtil.getInstance().getDataDir()).append(File.separator)
			.append("dp-history").append(File.separator)
			.toString();
		
		File dirFile = new File(path);
		if(!dirFile.exists()) {
			if(!dirFile.mkdirs()) {
				throw new RuntimeException("Error couldn't create directory for exporting history data");
			}
		}
		
		return path;
	}
}

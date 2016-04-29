package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.Status;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.administrative.events.ReturnedSpecimenDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.EntityCrudListener;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.domain.Filter;
import com.krishagni.catissueplus.core.de.domain.Filter.Op;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.rbac.common.errors.RbacErrorCode;

import edu.common.dynamicextensions.query.WideRowMode;

public class DistributionOrderServiceImpl implements DistributionOrderService, ObjectStateParamsResolver {
	private DaoFactory daoFactory;

	private DistributionOrderFactory distributionFactory;
	
	private QueryService querySvc;
	
	private EmailService emailService;

	private List<EntityCrudListener<DistributionOrderDetail, DistributionOrder>> listeners = new ArrayList<>();

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionFactory(DistributionOrderFactory distributionFactory) {
		this.distributionFactory = distributionFactory;
	}
	
	public void setQuerySvc(QueryService querySvc) {
		this.querySvc = querySvc;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderSummary>> getOrders(RequestEvent<DistributionOrderListCriteria> req) {
		try {
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessDistributionOrderSites();
			if (siteIds != null && siteIds.isEmpty()) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			DistributionOrderListCriteria crit = req.getPayload();
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
						
			return ResponseEvent.response(daoFactory.getDistributionOrderDao().getOrders(crit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> getOrder(RequestEvent<Long> req) {
		try {
			DistributionOrder order = getOrder(req.getPayload(), null);
			AccessCtrlMgr.getInstance().ensureReadDistributionOrderRights(order);

			DistributionOrderDetail output = DistributionOrderDetail.from(order);
			listeners.forEach(listener -> listener.onRead(output, order));
			return ResponseEvent.response(output);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> createOrder(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail input = req.getPayload();
			DistributionOrder order = distributionFactory.createDistributionOrder(input, Status.PENDING);
			
			AccessCtrlMgr.getInstance().ensureCreateDistributionOrderRights(order);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraints(null, order, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(order.getOrderItems(), "specimen.label");
			getValidSpecimens(order.getDistributionProtocol(), specimenLabels, ose);

			Status inputStatus = null;
			try {
				inputStatus = Status.valueOf(input.getStatus());
			} catch (IllegalArgumentException iae) {
				ose.addError(DistributionOrderErrorCode.INVALID_STATUS, input.getStatus());
			}

			ose.checkAndThrow();
			
			SpecimenRequest request = order.getRequest();
			if (request != null && request.isClosed()) {
				throw OpenSpecimenException.userError(SpecimenRequestErrorCode.CLOSED, request.getId());
			}

			if (inputStatus == Status.EXECUTED) {
				order.distribute();
			}

			daoFactory.getDistributionOrderDao().saveOrUpdate(order, true);
			sendOrderProcessedEmail(order, null);

			DistributionOrderDetail output = DistributionOrderDetail.from(order);
			listeners.forEach(listener -> listener.onSave(input, output, order));
			return ResponseEvent.response(output);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> updateOrder(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail input = req.getPayload();
			DistributionOrder existingOrder = getOrder(input.getId(), input.getName());
			if (existingOrder.isOrderExecuted()) {
				return ResponseEvent.userError(DistributionOrderErrorCode.CANT_UPDATE_EXEC_ORDER, existingOrder.getName());
			}

			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(existingOrder);
			DistributionOrder newOrder = distributionFactory.createDistributionOrder(input, null);
			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(newOrder);

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraints(existingOrder, newOrder, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(newOrder.getOrderItems(), "specimen.label");
			getValidSpecimens(newOrder.getDistributionProtocol(), specimenLabels, ose);
			
			ose.checkAndThrow();
			
			Status oldStatus = existingOrder.getStatus();
			existingOrder.update(newOrder);
			daoFactory.getDistributionOrderDao().saveOrUpdate(existingOrder, true);
			sendOrderProcessedEmail(existingOrder, oldStatus);

			DistributionOrderDetail output = DistributionOrderDetail.from(existingOrder);
			listeners.forEach(listener -> listener.onSave(input, output, existingOrder));
			return ResponseEvent.response(output);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req) {
		try {
			Long orderId = req.getPayload();
			DistributionOrder order = daoFactory.getDistributionOrderDao().getById(orderId);
			if (order == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadDistributionOrderRights(order);
			if (order.getDistributionProtocol().getReport() == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.RPT_TMPL_NOT_CONFIGURED);
			}
			
			return new ResponseEvent<QueryDataExportResult>(exportOrderReport(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req) {
		try {
			Long dpId = req.getPayload().getDpId();
			List<String> specimenLabels = req.getPayload().getLabels();
			DistributionProtocol dp = daoFactory.getDistributionProtocolDao().getById(dpId);
			if (dp == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			List<Specimen> specimens = getValidSpecimens(dp, specimenLabels, ose);
			ose.checkAndThrow();
			
			return ResponseEvent.response(SpecimenInfo.from(Specimen.sortByLabels(specimens, specimenLabels)));
		} catch(OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderItemDetail>> getDistributedSpecimens(RequestEvent<List<String>> req) {
		try {
			List<Specimen> specimens = getReadAccessSpecimens(req.getPayload());
			if (CollectionUtils.isEmpty(specimens)) {
				throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
			}

			List<String> labels = specimens.stream().map(Specimen::getLabel).collect(Collectors.toList());
			List<DistributionOrderItem> items = daoFactory.getDistributionOrderDao().getDistributedOrderItems(labels);
			Set<DistributionOrder> accessAllowed = new HashSet<>();
			for (DistributionOrderItem item : items) {
				if (accessAllowed.contains(item.getOrder())) {
					continue;
				}

				AccessCtrlMgr.getInstance().ensureReadDistributionOrderRights(item.getOrder());
				accessAllowed.add(item.getOrder());
			}

			return ResponseEvent.response(DistributionOrderItemDetail.from(items));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> returnSpecimens(RequestEvent<List<ReturnedSpecimenDetail>> req) {
		try {
			Map<String, DistributionOrder> ordersMap = new HashMap<>();
			Map<String, StorageContainer> containersMap = new HashMap<>();

			List<Specimen> result = new ArrayList<>();
			for (ReturnedSpecimenDetail returnedSpmn : req.getPayload()) {
				result.add(returnSpecimen(returnedSpmn, ordersMap, containersMap));
			}

			return ResponseEvent.response(SpecimenInfo.from(result));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public void addListener(EntityCrudListener<DistributionOrderDetail, DistributionOrder> listener) {
		listeners.add(listener);
	}

	@Override
	public String getObjectName() {
		return "order";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getDistributionOrderDao().getOrderIds(key, value);
	}


	private void ensureUniqueConstraints(DistributionOrder existingOrder, DistributionOrder newOrder, OpenSpecimenException ose) {
		if (existingOrder == null || !newOrder.getName().equals(existingOrder.getName())) {
			DistributionOrder order = daoFactory.getDistributionOrderDao().getOrder(newOrder.getName());
			if (order != null) {
				ose.addError(DistributionOrderErrorCode.DUP_NAME, newOrder.getName());
			}
		}
	}

	private List<Specimen> getReadAccessSpecimens(List<String> specimenLabels) {
		List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			return null;
		}

		SpecimenListCriteria crit = new SpecimenListCriteria().labels(specimenLabels).siteCps(siteCpPairs);
		return daoFactory.getSpecimenDao().getSpecimens(crit);
	}

	private List<Specimen> getValidSpecimens(DistributionProtocol dp, List<String> specimenLabels, OpenSpecimenException ose) {
		List<Specimen> specimens = getReadAccessSpecimens(specimenLabels);
		if (specimens == null) {
			ose.addError(RbacErrorCode.ACCESS_DENIED);
			return null;
		}

		if (specimens.size() != specimenLabels.size()) {
			List<String> labels = (List<String>) CollectionUtils.subtract(specimenLabels, Utility.<List<String>>collect(specimens, "label"));
			ose.addError(DistributionOrderErrorCode.SPECIMEN_DOES_NOT_EXIST, labels);
			return null;
		}
		
		Set<Long> specimenIds = Utility.<Set<Long>>collect(specimens, "id", true);
		Map<String, Set<Long>> specimenSiteIdsMap = daoFactory.getSpecimenDao().getSpecimenSites(specimenIds);
		Set<Long> orderAllowedIds = AccessCtrlMgr.getInstance().getDistributionOrderAllowedSites(dp);
		for (Map.Entry<String, Set<Long>> specimenSitesMapEntry : specimenSiteIdsMap.entrySet()) {
			if (CollectionUtils.intersection(specimenSitesMapEntry.getValue(), orderAllowedIds).isEmpty()) {
				ose.addError(DistributionOrderErrorCode.INVALID_SPECIMENS_FOR_DP, specimenSitesMapEntry.getKey());
				return null;
			}
		}
		
		return specimens;
	}
	
	private QueryDataExportResult exportOrderReport(final DistributionOrder order) {
		SavedQuery report = order.getDistributionProtocol().getReport();
		Filter filter = new Filter();
		filter.setField("Order.id");
		filter.setOp(Op.EQ);
		filter.setValues(new String[] { order.getId().toString() });
		
		ExecuteQueryEventOp execReportOp = new ExecuteQueryEventOp();
		execReportOp.setDrivingForm("Participant");
		execReportOp.setAql(report.getAql(new Filter[] { filter }));			
		execReportOp.setWideRowMode(WideRowMode.DEEP.name());
		execReportOp.setRunType("Export");
		return querySvc.exportQueryData(execReportOp, new QueryService.ExportProcessor() {			
			@Override
			public void headers(OutputStream out) {
				@SuppressWarnings("serial")
				Map<String, String> headers = new LinkedHashMap<String, String>() {{
					String notSpecified = msg("common_not_specified");
					DistributionProtocol dp = order.getDistributionProtocol();

					put(msg("dist_order_name"),     order.getName());
					put(msg("dist_dp_title"),       dp.getTitle());
					put(msg("dist_dp_short_title"), dp.getShortTitle());
					put(msg("dist_requestor_name"), order.getRequester().formattedName());
					put(msg("dist_requested_date"), Utility.getDateString(order.getExecutionDate()));
					put(msg("dist_receiving_site"), order.getSite() == null ? notSpecified : order.getSite().getName());
					put(msg("dist_tracking_url"),   StringUtils.isBlank(order.getTrackingUrl()) ? notSpecified : order.getTrackingUrl());
					put(msg("dist_comments"),       StringUtils.isBlank(order.getComments()) ? notSpecified : order.getComments());
					put(msg("dp_irb_id"),           StringUtils.isBlank(dp.getIrbId()) ? notSpecified : dp.getIrbId());
					put(msg("dist_exported_by"),    AuthUtil.getCurrentUser().formattedName());
					put(msg("dist_exported_on"),    Utility.getDateString(Calendar.getInstance().getTime()));

					User pi = dp.getPrincipalInvestigator();
					put(msg("dist_dp_pi_inst_depart"), pi.getInstitute().getName() + " / " + pi.getDepartment().getName());
					put(msg("dist_dp_pi_email_addr"),  pi.getEmailAddress());
					put(msg("dist_dp_pi_cont_num"),    StringUtils.isBlank(pi.getPhoneNumber()) ? notSpecified : pi.getPhoneNumber());
					put(msg("dist_dp_pi_addr"),        StringUtils.isBlank(pi.getAddress()) ? notSpecified : pi.getAddress());

					DeObject extension = order.getDistributionProtocol().getExtension();
					if (extension != null) {
						putAll(extension.getLabelValueMap());
					}

					put("", ""); // blank line
				}};

				Utility.writeKeyValuesToCsv(out, headers);
			}
		});
	}

	private String msg(String code) {
		return MessageUtil.getInstance().getMessage(code);
	}
	
	private void sendOrderProcessedEmail(DistributionOrder order, Status oldStatus) {
		Status newStatus = order.getStatus();
		if (!newStatus.equals(Status.EXECUTED) || newStatus.equals(oldStatus)) {
			return;
		}

		String[] rcpts = {
			order.getRequester().getEmailAddress(),
			order.getDistributor().getEmailAddress()
		};
		String[] subjectParams = {order.getName()};

		Map<String, Object> props = new HashMap<String, Object>();
		props.put("$subject", subjectParams);
		props.put("order", order);
		emailService.sendEmail(ORDER_DISTRIBUTED_EMAIL_TMPL, rcpts, props);
	}

	private DistributionOrder getOrder(Long orderId, String orderName) {
		DistributionOrder order = null;
		Object key = null;

		if (orderId != null) {
			order = daoFactory.getDistributionOrderDao().getById(orderId);
			key = orderId;
		} else if (StringUtils.isNotBlank(orderName)) {
			order = daoFactory.getDistributionOrderDao().getOrder(orderName);
			key = orderName;
		}

		if (order == null) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.NOT_FOUND, key);
		}

		return order;
	}

	private Specimen returnSpecimen(
			ReturnedSpecimenDetail detail,
			Map<String, DistributionOrder> ordersMap,
			Map<String, StorageContainer> containersMap) {

		String orderName = detail.getOrderName();
		if (StringUtils.isBlank(orderName)) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.NAME_REQUIRED);
		}

		String label = detail.getSpecimenLabel();
		if (StringUtils.isBlank(label)) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.LABEL_REQUIRED);
		}

		DistributionOrder order = ordersMap.get(orderName);
		if (order == null) {
			order = getOrder(null, detail.getOrderName());
			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(order);
			if (!order.isOrderExecuted()) {
				throw OpenSpecimenException.userError(DistributionOrderErrorCode.NOT_DISTRIBUTED, order.getName());
			}

			ordersMap.put(order.getName(), order);
		}

		DistributionOrderItem item = order.getItemBySpecimen(label);
		if (item == null) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.SPMN_NOT_FOUND, label, orderName);
		}

		returnSpecimen(detail, item, containersMap);
		return item.getSpecimen();
	}


	private void returnSpecimen(ReturnedSpecimenDetail detail, DistributionOrderItem item, Map<String, StorageContainer> containersMap) {
		ensureItemNotReturned(item);
		setItemReturnedQty(item, detail.getQuantity());
		setItemReturnDate(item, detail.getTime());
		setItemReturnedBy(item, detail.getUser());
		setItemReturningPosition(item, detail.getLocation(), containersMap);
		setItemFreezeThawIncrOnReturn(item, detail.getIncrFreezeThaw());
		item.setReturnComments(detail.getComments());
		item.returnSpecimen();
	}

	private void ensureItemNotReturned(DistributionOrderItem item) {
		if (item.isReturned()) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.SPECIMEN_ALREADY_RETURNED, item.getSpecimen().getLabel());
		}
	}

	private void setItemReturnedQty(DistributionOrderItem item, BigDecimal returnQty) {
		if (returnQty == null) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.RETURN_QTY_REQ, item.getSpecimen().getLabel());
		}


		if (NumUtil.lessThanEqualsZero(returnQty) || NumUtil.lessThan(item.getQuantity(), returnQty)) {
			raiseError(DistributionOrderErrorCode.INVALID_RETURN_QUANTITY, item.getSpecimen().getLabel(), returnQty);
		}

		item.setReturnedQuantity(returnQty);
	}

	private void setItemReturnDate(DistributionOrderItem item, Date returnDate) {
		if (returnDate == null) {
			raiseError(DistributionOrderErrorCode.RETURN_DATE_REQ, item.getSpecimen().getLabel());
		}

		if (item.getOrder().getExecutionDate().after(returnDate)) {
			raiseError(DistributionOrderErrorCode.INVALID_RETURN_DATE, item.getSpecimen().getLabel(), returnDate);
		}

		item.setReturnDate(returnDate);
	}

	private void setItemReturningPosition(DistributionOrderItem item, StorageLocationSummary location, Map<String, StorageContainer> containersMap) {
		if (location == null || StringUtils.isBlank(location.getName())) {
			return;
		}

		StorageContainer container = containersMap.get(location.getName());
		if (container == null) {
			Object key = null;
			if (location.getId() != null) {
				container = daoFactory.getStorageContainerDao().getById(location.getId());
				key = location.getId();
			} else {
				container = daoFactory.getStorageContainerDao().getByName(location.getName());
				key = location.getName();
			}

			if (container == null) {
				raiseError(StorageContainerErrorCode.NOT_FOUND, key);
			}

			containersMap.put(location.getName(), container);
		}


		Specimen specimen = item.getSpecimen();
		if (!container.canContain(specimen)) {
			raiseError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, container.getName(), specimen.getLabelOrDesc());
		}

		//
		// TODO: This is duplicate code. Need to consolidate this with specimen/container objects
		//
		StorageContainerPosition position = null;
		String row = location.getPositionY(), column = location.getPositionX();
		if (StringUtils.isNotBlank(row) && StringUtils.isNotBlank(column)) {
			if (container.canSpecimenOccupyPosition(specimen.getId(), column, row)) {
				position = container.createPosition(column, row);
				container.setLastAssignedPos(position);
			} else {
				raiseError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
			}
		} else {
			position = container.nextAvailablePosition(true);
			if (position == null) {
				raiseError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
			}
		}

		item.setReturningContainer(container);
		item.setReturningRow(position.getPosTwo());
		item.setReturningColumn(position.getPosOne());
	}

	private void setItemFreezeThawIncrOnReturn(DistributionOrderItem item, Integer incrFreezeThaw) {
		item.getSpecimen().incrementFreezeThaw(incrFreezeThaw);
		item.setFreezeThawIncrOnReturn(incrFreezeThaw);
	}

	private void setItemReturnedBy(DistributionOrderItem item, UserSummary userDetail) {
		if (userDetail == null || (userDetail.getId() == null && StringUtils.isBlank(userDetail.getEmailAddress()))) {
			raiseError(DistributionOrderErrorCode.RETURNED_BY_REQ, item.getSpecimen().getLabel());
		}

		Object key = null;
		User user = null;
		if (userDetail.getId() != null) {
			key = userDetail.getId();
			user = daoFactory.getUserDao().getById(userDetail.getId());
		} else {
			key = userDetail.getEmailAddress();
			user = daoFactory.getUserDao().getUserByEmailAddress(userDetail.getEmailAddress());
		}

		if (user == null) {
			raiseError(UserErrorCode.NOT_FOUND, key);
		}

		item.setReturnedBy(user);
	}

	private void raiseError(ErrorCode error, Object ... args) {
		throw OpenSpecimenException.userError(error, args);
	}

	private static final String ORDER_DISTRIBUTED_EMAIL_TMPL = "order_distributed";
}

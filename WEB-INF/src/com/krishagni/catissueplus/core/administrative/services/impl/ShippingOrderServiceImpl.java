package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder.Status;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShippingOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ShippingOrderFactory;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ShippingOrderDao;
import com.krishagni.catissueplus.core.administrative.services.ShippingOrderService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ShippingOrderServiceImpl implements ShippingOrderService {
	private static final String ORDER_SHIPPED_EMAIL_TMPL = "order_shipped";
	
	private DaoFactory daoFactory;
	
	private ShippingOrderFactory shippingFactory;
	
	private EmailService emailService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setShippingFactory(ShippingOrderFactory shippingFactory) {
		this.shippingFactory = shippingFactory;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<ShippingOrderDetail>> getOrders(RequestEvent<ShippingOrderListCriteria> req) {
		try {
			ShippingOrderListCriteria listCrit = req.getPayload();
			
			return ResponseEvent.response(ShippingOrderDetail.from(getShippingOrderDao().getShippingOrders(listCrit)));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ShippingOrderDetail> getOrder(RequestEvent<Long> req) {
		try {
			Long orderId = req.getPayload();
			ShippingOrder order = getShippingOrderDao().getById(orderId);
			if (order == null) {
				return ResponseEvent.userError(ShippingOrderErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(ShippingOrderDetail.from(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ShippingOrderDetail> createOrder(RequestEvent<ShippingOrderDetail> req) {
		try {
			ShippingOrderDetail detail = req.getPayload();
			ShippingOrder order = shippingFactory.createShippingOrder(detail, Status.PENDING);
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(null, order, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(order.getOrderItems(), "specimen.label");
			ensureValidSpecimen(specimenLabels, ose);
			ose.checkAndThrow();
			
			Status inputStatus = Status.valueOf(detail.getStatus());
			if (inputStatus == Status.SHIPPED) {
				order.ship();
			}
			
			getShippingOrderDao().saveOrUpdate(order);
			sendOrderProcessedEmail(order, null);
			return ResponseEvent.response(ShippingOrderDetail.from(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ShippingOrderDetail> updateOrder(RequestEvent<ShippingOrderDetail> req) {
		try {
			ShippingOrderDetail detail = req.getPayload();
			ShippingOrder existingOrder = getShippingOrderDao().getById(detail.getId());
			if (existingOrder == null) {
				return ResponseEvent.userError(ShippingOrderErrorCode.NOT_FOUND);
			}
			
			ShippingOrder newOrder = shippingFactory.createShippingOrder(detail, null);
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(existingOrder, newOrder, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(newOrder.getOrderItems(), "specimen.label");
			ensureValidSpecimen(specimenLabels, ose);
			ose.checkAndThrow();
			
			Status oldStatus = existingOrder.getStatus();
			existingOrder.update(newOrder);
			getShippingOrderDao().saveOrUpdate(existingOrder);
			sendOrderProcessedEmail(existingOrder, oldStatus);
			return ResponseEvent.response(ShippingOrderDetail.from(existingOrder));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraint(ShippingOrder existingOrder, ShippingOrder newOrder, OpenSpecimenException ose) {
		if (existingOrder == null || !newOrder.getName().equals(existingOrder.getName())) {
			ShippingOrder order = getShippingOrderDao().getOrderByName(newOrder.getName());
			if (order != null) {
				ose.addError(ShippingOrderErrorCode.DUP_NAME, newOrder.getName());
			}
		}
	}
	
	private void ensureValidSpecimen(List<String> specimenLabels, OpenSpecimenException ose) {
		List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			ose.addError(ShippingOrderErrorCode.INVALID_SPECIMENS);
			return;
		}
		
		SpecimenListCriteria crit = new SpecimenListCriteria()
			.labels(specimenLabels)
			.siteCps(siteCpPairs);
		
		List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit);
		if (specimens.size() != specimenLabels.size()) {
			ose.addError(ShippingOrderErrorCode.INVALID_SPECIMENS);
			return;
		}
		
		List<Specimen> shippedSpecimen = getShippingOrderDao().getShippedSpecimensByLabels(specimenLabels);
		if (!CollectionUtils.isEmpty(shippedSpecimen)) {
			ose.addError(ShippingOrderErrorCode.SPECIMEN_ALREADY_SHIPPED);
		}
	}
	
	private void sendOrderProcessedEmail(ShippingOrder order, Status oldStatus) {
		if (!order.isOrderShipped() ||  order.getStatus().equals(oldStatus)) {
			return;
		}
		
		List<String> emailIds = Utility.<List<String>>collect(order.getSite().getCoordinators(), "emailAddress");
		String[] subjectParams = {order.getName()};
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("$subject", subjectParams);
		props.put("order", order);
		emailService.sendEmail(ORDER_SHIPPED_EMAIL_TMPL, emailIds.toArray(new String[0]), props);
	}
	
	private ShippingOrderDao getShippingOrderDao() {
		return daoFactory.getShippingOrderDao();
	}
}

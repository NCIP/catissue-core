
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			
			Specimen specimen = null;
			if (crit.getId() != null) {
				specimen = daoFactory.getSpecimenDao().getById(crit.getId());
			} else if (crit.getName() != null) {
				specimen = daoFactory.getSpecimenDao().getByLabel(crit.getName());
			}
			
			if (specimen == null) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(SpecimenDetail.from(specimen));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> createSpecimen(RequestEvent<SpecimenDetail> req) {
		try {
			SpecimenDetail detail = req.getPayload();
			Specimen specimen = saveOrUpdate(detail, null, null);
			return ResponseEvent.response(SpecimenDetail.from(specimen));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenDetail> updateSpecimen(RequestEvent<SpecimenDetail> req) {
		try {
			SpecimenDetail detail = req.getPayload();
			
			Specimen existing = null;
			if (detail.getId() != null) {
				existing = daoFactory.getSpecimenDao().getById(detail.getId());
			} else if (StringUtils.isNotBlank(detail.getLabel())) {
				existing = daoFactory.getSpecimenDao().getByLabel(detail.getLabel());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(SpecimenErrorCode.NOT_FOUND);
			}
			
			saveOrUpdate(detail, existing, null);
			return ResponseEvent.response(SpecimenDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> collectSpecimens(RequestEvent<List<SpecimenDetail>> req) {
		try {
			List<SpecimenDetail> result = new ArrayList<SpecimenDetail>();
			for (SpecimenDetail detail : req.getPayload()) {
				Specimen specimen = collectSpecimen(detail, null);
				result.add(SpecimenDetail.from(specimen));
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
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<String> req) {
		return ResponseEvent.response(daoFactory.getSpecimenDao().getByLabel(req.getPayload()) != null);
	}
	
	private void ensureUniqueLabel(String label, OpenSpecimenException ose) {
		if (StringUtils.isBlank(label)) {
			return;
		}
		
		if (daoFactory.getSpecimenDao().getByLabel(label) != null) {
			ose.addError(SpecimenErrorCode.DUP_LABEL);
		}
	}

	private void ensureUniqueBarcode(String barcode, OpenSpecimenException ose) {
		if (daoFactory.getSpecimenDao().getByBarcode(barcode) != null) {
			ose.addError(SpecimenErrorCode.DUP_BARCODE);
		}
	}

	private Specimen collectSpecimen(SpecimenDetail detail, Specimen parent) {
		Specimen existing = null;
		if (detail.getId() != null) {
			existing = daoFactory.getSpecimenDao().getById(detail.getId());
			if (existing == null) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND);
			}
		}
		
		Specimen specimen = saveOrUpdate(detail, existing, parent);
		if (detail.getChildren() != null) {
			for (SpecimenDetail childDetail : detail.getChildren()) {
				collectSpecimen(childDetail, specimen);
			}
		}
		
		return specimen;
	}
	
	private Specimen saveOrUpdate(SpecimenDetail detail, Specimen existing, Specimen parent) {
		Specimen specimen = specimenFactory.createSpecimen(detail, parent);

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		if (existing == null || // no specimen before 
			StringUtils.isBlank(existing.getLabel()) || // no label was specified before 
			!existing.getLabel().equals(specimen.getLabel())) { // new label differs from existing
			
			ensureUniqueLabel(specimen.getLabel(), ose); // check for label uniqueness
		}

		String barcode = specimen.getBarcode();
		if (StringUtils.isNotBlank(barcode) &&
			(existing == null || !barcode.equals(existing.getBarcode()))) {
			ensureUniqueBarcode(specimen.getBarcode(), ose);
		}

		ose.checkAndThrow();

		boolean newSpecimen = true;
		if (existing != null) {
			existing.update(specimen);
			specimen = existing;
			newSpecimen = false;
		} else if (specimen.getParentSpecimen() != null) {
			specimen.getParentSpecimen().addSpecimen(specimen);
		} else {
			specimen.checkQtyConstraints(); // TODO: Should we be calling this at all?
			specimen.occupyPosition();
		}

		specimen.setLabelIfEmpty();
		daoFactory.getSpecimenDao().saveOrUpdate(specimen);
		if (newSpecimen) {
			addEvents(specimen);
		}
		return specimen;
	}
	
	private void addEvents(Specimen specimen) {
		if (!specimen.isCollected()) {
			return;
		}
		
		specimen.addCollRecvEvents();
	}
}

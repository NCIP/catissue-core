package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.krishagni.catissueplus.core.administrative.events.RequirementDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolRequirementService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolRequirementServiceImpl implements DistributionProtocolRequirementService {

	private List<RequirementDetail> data = getRequirementList();
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<RequirementDetail>> getRequirements(RequestEvent<Long> req) {
		return ResponseEvent.response(data);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<RequirementDetail> getRequirement(RequestEvent<Long> req) {
		return ResponseEvent.response(getRequirementById(req.getPayload()));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<RequirementDetail> addRequirement(RequestEvent<RequirementDetail> detail) {
		RequirementDetail request = detail.getPayload();
		RequirementDetail req = createRequirement(request.getType(), request.getAnatomicSite(), request.getPathology(),
			request.getSpecimenReq(), request.getSpecimenDistributed(), request.getPrice(), request.getComments());
		data.add(req);
		return ResponseEvent.response(req);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<RequirementDetail> updateRequirement(RequestEvent<RequirementDetail> req) {
		RequirementDetail request = req.getPayload();
		RequirementDetail detail = null;
		for (RequirementDetail requirementDetail : data) {
			if(requirementDetail.getId().equals(request.getId())) {
				requirementDetail.setAnatomicSite(request.getAnatomicSite());
				requirementDetail.setComments(request.getComments());
				requirementDetail.setPathology(request.getPathology());
				requirementDetail.setPrice(request.getPrice());
				requirementDetail.setSpecimenDistributed(request.getSpecimenDistributed());
				requirementDetail.setSpecimenReq(request.getSpecimenReq());
				requirementDetail.setType(request.getType());
				detail = requirementDetail;
			}
			break;
		}
		
		return ResponseEvent.response(detail);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> deleteRequirement(RequestEvent<Long> req) {
		Long id = null;
		
		for (RequirementDetail requirementDetail : data) {
			if(requirementDetail.getId().equals(req.getPayload())) {
				id = requirementDetail.getId();
				data.remove(requirementDetail);
				break;
			}
		}
		return ResponseEvent.response(id);
	}
	
	private RequirementDetail getRequirementById(Long id) {
		RequirementDetail resp = null;
		
		for (RequirementDetail requirementDetail : data) {
			if (requirementDetail.getId().equals(id)) {
				resp = requirementDetail;
				break;
			}
		}
		return resp;
	}
	
	private List<RequirementDetail> getRequirementList() {
		List<RequirementDetail> data = new ArrayList<RequirementDetail>();
		data.add(createRequirement("Body Cavity Fluid", "Jejunum", "Metastatic", 20L, 0L, 34.78, ""));
		data.add(createRequirement("cDNA", "Cornea, NOS", "Malignant, Invasive", 56L, 0L, 75, "Sample comment"));
		data.add(createRequirement("Feces", "Ileum", "Not Specified", 150L, 0L, 150, ""));
		data.add(createRequirement("RNA", "Lower gum", "Pre-Malignant", 6L, 0L, 3, ""));
		data.add(createRequirement("Sweat", "Ovary", "Non-Malignant", 100L, 0L, 45, ""));
		data.add(createRequirement("Whole Blood", "Pelvis, NOS", "Metastatic", 75L, 0L, 65, ""));
		return data;
	}
	
	private RequirementDetail createRequirement(String type, String site, String pathology, Long req, Long dis, double price, String comment) {
		RequirementDetail detail = new RequirementDetail();
		Long id = (long)new Random().nextInt(500-1) + 1;
		detail.setId(id);
		detail.setType(type);
		detail.setAnatomicSite(site);
		detail.setPathology(pathology);
		detail.setSpecimenReq(req);
		detail.setSpecimenDistributed(dis != null ? dis : 0);
		detail.setPrice(price);
		detail.setComments(comment);
		
		return detail;
	}
}
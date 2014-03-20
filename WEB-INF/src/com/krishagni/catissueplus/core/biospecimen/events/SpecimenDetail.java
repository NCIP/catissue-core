
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;

public class SpecimenDetail {

	private Long id;

	private String tissueSite;

	private String tissueSide;

	private String pathologicalStatus;

	private String lineage;

	private Double initialQuantity;

	private String specimenClass;

	private String specimenType;

	private Double concentrationInMicrogramPerMicroliter;

	private String label;

	private String activityStatus;

	private Boolean isAvailable;

	private String barcode;

	private String comment;

	private Date createdOn;

	private Double availableQuantity;

	private String collectionStatus;

	private Long scgId;

	private Long requirementId;

	private SpecimenPosition specimenPosition;

	private Specimen parentSpecimen;

	private Set<SpecimenEventParameters> eventCollection = new HashSet<SpecimenEventParameters>();

	private Set<Biohazard> biohazardCollection = new HashSet<Biohazard>();

	private Set<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	public static SpecimenDetail fromDomain(Specimen specimen) {
		SpecimenDetail detail = new SpecimenDetail();
		return detail;
	}
}

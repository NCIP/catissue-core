package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReviewEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLGroup;
import gov.nih.nci.system.query.cql.CQLLogicalOperator;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

public class CqlUtility {

	public static CQLQuery getParticipantsForCP(String shortTitle) {
		CQLAttribute association2Attribute = createAttribute("shortTitle",
				shortTitle, CQLPredicate.EQUAL_TO);
		CQLAssociation association2 = createAssociation(
				CollectionProtocol.class, "collectionProtocol");
		association2.setAttribute(association2Attribute);

		CQLAttribute association1Attribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group2 = createGroup(CQLLogicalOperator.AND,
				association1Attribute, association2);

		CQLAssociation association1 = createAssociation(
				CollectionProtocolRegistration.class,
				"collectionProtocolRegistrationCollection");
		association1.setGroup(group2);

		CQLAttribute targetAttribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group1 = createGroup(CQLLogicalOperator.AND, targetAttribute,
				association1);

		return createTargetQuery(Participant.class, group1);
	}

	public static CQLQuery getFluidSpecimensWithReviewEventRecordForCP(
			String shortTitle) {
		CQLAssociation association2 = createSCGAssociationWithIDandCPRAssociation(shortTitle);

		CQLAttribute association1Attribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLAssociation association1 = createAssociation(
				ReviewEventParameters.class, "specimenEventCollection");
		association1.setAttribute(association1Attribute);

		CQLAttribute targetAttribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group = createGroup(CQLLogicalOperator.AND, targetAttribute,
				association1, association2);

		return createTargetQuery(FluidSpecimen.class, group);
	}

	public static CQLQuery getTissueSpecimensForCP(String shortTitle) {
		CQLAssociation association2 = createSCGAssociationWithIDandCPRAssociation(shortTitle);
		CQLAttribute targetAttribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group = createGroup(CQLLogicalOperator.AND, targetAttribute,
				association2);

		return createTargetQuery(TissueSpecimen.class, group);
	}

//	public static CQLQuery getMolecularSpecimensWithFrozenEvent() {
//		CQLAttribute attribute = createAttribute("id", null,
//				CQLPredicate.IS_NOT_NULL);
//		CQLAssociation association = createAssociation(
//				FrozenEventParameters.class, "specimenEventCollection");
//		association.setAttribute(attribute);
//
//		CQLAttribute targetAttribute = createAttribute("id", null,
//				CQLPredicate.IS_NOT_NULL);
//		CQLGroup group = createGroup(CQLLogicalOperator.AND, targetAttribute,
//				association);
//
//		return createTargetQuery(MolecularSpecimen.class, group);
//	}

	public static CQLQuery getSpecimenMalePPI(String ppi) {
		CQLAssociation association3 = createAssociationWithIDNotNullandOtherAttribute(
				Participant.class, "participant", "gender", "Male Gender",
				CQLPredicate.EQUAL_TO, CQLLogicalOperator.AND);
		CQLAttribute attribute1Association2 = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLAttribute attribute2Association2 = createAttribute(
				"protocolParticipantIdentifier", ppi, CQLPredicate.EQUAL_TO);
		CQLGroup group3 = createGroup(CQLLogicalOperator.AND,
				attribute1Association2, attribute2Association2, association3);

		CQLAssociation association2 = createAssociation(
				CollectionProtocolRegistration.class,
				"collectionProtocolRegistration");
		association2.setGroup(group3);
		CQLAttribute attribute1Association1 = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group2 = createGroup(CQLLogicalOperator.AND,
				attribute1Association1, association2);

		CQLAssociation association1 = createAssociation(
				SpecimenCollectionGroup.class, "specimenCollectionGroup");
		association1.setGroup(group2);

		CQLAttribute targetAttribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group1 = createGroup(CQLLogicalOperator.AND, targetAttribute,
				association1);

		return createTargetQuery(Specimen.class, group1);
	}

//	public static CQLQuery getSpecimenNeedleBiopsy() {
//		CQLAttribute attribute = createAttribute("id", null,
//				CQLPredicate.IS_NOT_NULL);
//		CQLAssociation association1 = createAssociationWithIDNotNullandOtherAttribute(
//				CollectionEventParameters.class, "specimenEventCollection",
//				"collectionProcedure", "%Needle Core Biopsy%",
//				CQLPredicate.LIKE, CQLLogicalOperator.AND);
//		CQLAssociation association2 = createAssociationWithIDNotNullandOtherAttribute(
//				CollectionProtocol.class, "collectionProtocol", "title",
//				"%Prostate%", CQLPredicate.LIKE, CQLLogicalOperator.AND);
//
//		CQLGroup group = createGroup(CQLLogicalOperator.AND, attribute,
//				association1, association2);
//		return createTargetQuery(Specimen.class, group);
//	}

	private static CQLQuery createTargetQuery(Class<?> klass, Object... objects) {
		CQLObject target = new CQLObject();
		target.setName(klass.getName());

		for (Object object : objects) {
			if (object instanceof CQLAttribute) {
				target.setAttribute((CQLAttribute) object);
			} else if (object instanceof CQLAssociation) {
				target.setAssociation((CQLAssociation) object);
			} else if (object instanceof CQLGroup) {
				target.setGroup((CQLGroup) object);
			}
		}

		CQLQuery cqlQuery = new CQLQuery();
		cqlQuery.setTarget(target);

		return cqlQuery;
	}

	private static CQLAttribute createAttribute(String name, String value,
			CQLPredicate predicate) {
		CQLAttribute attribute = new CQLAttribute();
		attribute.setName(name);

		if (CQLPredicate.IS_NOT_NULL != predicate
				|| CQLPredicate.IS_NULL != predicate) {
			attribute.setValue(value);
		}

		attribute.setPredicate(predicate);
		return attribute;
	}

	private static CQLGroup createGroup(CQLLogicalOperator operator,
			Object... objects) {
		CQLGroup group = new CQLGroup();

		for (Object object : objects) {
			if (object instanceof CQLAttribute) {
				group.addAttribute((CQLAttribute) object);
			} else if (object instanceof CQLAssociation) {
				group.addAssociation((CQLAssociation) object);
			}
		}
		group.setLogicOperator(operator);

		return group;
	}

	private static CQLAssociation createAssociation(Class<?> klass,
			String targetRoleName) {
		CQLAssociation association = new CQLAssociation();
		association.setName(klass.getName());
		association.setTargetRoleName(targetRoleName);

		return association;
	}

	private static CQLAssociation createAssociationWithIDNotNullandOtherAttribute(
			Class<?> klass, String roleName, String name, String value,
			CQLPredicate predicate, CQLLogicalOperator operator) {
		CQLAttribute attribute1 = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLAttribute attribute2 = createAttribute(name, value, predicate);

		CQLGroup group = createGroup(operator, attribute1, attribute2);

		CQLAssociation association = createAssociation(
				CollectionProtocol.class, roleName);
		association.setGroup(group);

		return association;
	}

	private static CQLAssociation createCPAssociationWithIDandTitle(
			String shortTitle) {
		return createAssociationWithIDNotNullandOtherAttribute(
				CollectionProtocol.class, "collectionProtocol", "shortTitle",
				shortTitle, CQLPredicate.EQUAL_TO, CQLLogicalOperator.AND);
	}

	private static CQLAssociation createCPRAssociationWithIDandCPAssociation(
			String shortTitle) {
		CQLAssociation association1 = createCPAssociationWithIDandTitle(shortTitle);
		CQLAttribute attribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group = createGroup(CQLLogicalOperator.AND, attribute,
				association1);

		CQLAssociation association2 = createAssociation(
				CollectionProtocolRegistration.class,
				"collectionProtocolRegistration");
		association2.setGroup(group);

		return association2;
	}

	private static CQLAssociation createSCGAssociationWithIDandCPRAssociation(
			String shortTitle) {
		CQLAssociation association1 = createCPRAssociationWithIDandCPAssociation(shortTitle);
		CQLAttribute attribute = createAttribute("id", null,
				CQLPredicate.IS_NOT_NULL);
		CQLGroup group = createGroup(CQLLogicalOperator.AND, attribute,
				association1);

		CQLAssociation association2 = createAssociation(
				SpecimenCollectionGroup.class, "specimenCollectionGroup");
		association2.setGroup(group);

		return association2;
	}

}

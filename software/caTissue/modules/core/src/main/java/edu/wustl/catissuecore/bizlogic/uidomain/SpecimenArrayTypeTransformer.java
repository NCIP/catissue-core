package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(SpecimenArrayTypeForm.class)
public class SpecimenArrayTypeTransformer extends ContainerTypeTransformer<SpecimenArrayTypeForm, SpecimenArrayType> {

    @Override
    public SpecimenArrayType createDomainObject(SpecimenArrayTypeForm uiRepOfDomain) {
    	InstanceFactory<SpecimenArrayType> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
        SpecimenArrayType specimenArrayType = instFact.createObject();// new SpecimenArrayType();
        overwriteDomainObject(specimenArrayType, uiRepOfDomain);
        return specimenArrayType;
    }

    @Override
    public void overwriteDomainObject(SpecimenArrayType domainObject, SpecimenArrayTypeForm uiRepOfDomain) {
        // SEE comments in superclass
        if (domainObject.getSpecimenTypeCollection() == null) {
            domainObject.setSpecimenTypeCollection(new HashSet<String>());
        }

        domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));
        domainObject.setName(uiRepOfDomain.getName());
        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getCapacity())) {
            domainObject.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject()); //new Capacity()
        }

        domainObject.getCapacity().setOneDimensionCapacity(Integer.valueOf(uiRepOfDomain.getOneDimensionCapacity()));
        domainObject.getCapacity().setTwoDimensionCapacity(Integer.valueOf(uiRepOfDomain.getTwoDimensionCapacity()));
        domainObject.setComment(uiRepOfDomain.getComment());
        domainObject.setSpecimenClass(uiRepOfDomain.getSpecimenClass());
        final String[] specimenTypes = uiRepOfDomain.getSpecimenTypes();
        if ((specimenTypes != null) && (specimenTypes.length > 0)) {
            for (final String specimenType : specimenTypes) {
                if (specimenType != null) {
                    domainObject.getSpecimenTypeCollection().add(specimenType);
                }
            }
        }
        domainObject.setActivityStatus("Active");
    }

}

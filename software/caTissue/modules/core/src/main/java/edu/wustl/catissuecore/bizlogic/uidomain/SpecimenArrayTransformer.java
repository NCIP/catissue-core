package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Validator;

@InputUIRepOfDomain(SpecimenArrayForm.class)
public class SpecimenArrayTransformer extends AbstractSpecimenArrayTransformer<SpecimenArrayForm> {

    @Override
    public void overwriteDomainObject(SpecimenArray domainObject, SpecimenArrayForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);

        // following is picked from Container.setAllValues() (it wasn't used
        // anywhere else)
        if (SearchUtil.isNullobject(domainObject.getCapacity())) {
            domainObject.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject());//new Capacity()
        }

        new Validator();
        domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));

        if (Validator.isEmpty(uiRepOfDomain.getBarcode())) {
            domainObject.setBarcode(null);
        } else {
            domainObject.setBarcode(uiRepOfDomain.getBarcode());
        }

        domainObject.setFull(Boolean.valueOf(uiRepOfDomain.getIsFull()));
        domainObject.setName(uiRepOfDomain.getName());
        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
        // domainObject.getPositionDimensionOne() = new
        // Integer(uiRepOfDomain.getPositionDimensionOne());
        // domainObject.getPositionDimensionTwo() = new
        // Integer(uiRepOfDomain.getPositionDimensionTwo());
        domainObject.setComment(uiRepOfDomain.getComment());
        domainObject.getCapacity().setOneDimensionCapacity(Integer.valueOf(uiRepOfDomain.getOneDimensionCapacity()));
        domainObject.getCapacity().setTwoDimensionCapacity(Integer.valueOf(uiRepOfDomain.getTwoDimensionCapacity()));
        // Remaining:put code about parent children container relationship

        // END CODE FROM Container.setAllValues()

        // rest is from SpecimenArray.setAllValues()

        domainObject.getSpecimenArrayType().setId(Long.valueOf(uiRepOfDomain.getSpecimenArrayTypeId()));

        if (domainObject.getLocatedAtPosition() == null) {
        	InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
        	domainObject.setLocatedAtPosition(instFact.createObject());
            //domainObject.setLocatedAtPosition(new ContainerPosition());
        }
        if (uiRepOfDomain.getStContSelection() == 1) {
            domainObject.getLocatedAtPosition().getParentContainer().setId(
                    Long.valueOf(uiRepOfDomain.getStorageContainer()));

            domainObject.getLocatedAtPosition().setPositionDimensionOne(
                    Integer.valueOf(uiRepOfDomain.getPositionDimensionOne()));
            domainObject.getLocatedAtPosition().setPositionDimensionTwo(
                    Integer.valueOf(uiRepOfDomain.getPositionDimensionTwo()));
            domainObject.getLocatedAtPosition().setOccupiedContainer(domainObject);

        } else {
/*        	String containerId = uiRepOfDomain.getContainerId();
        	if("".equals(containerId) || containerId == null)
        	{
        		containerId = uiRepOfDomain.getStorageContainer();
        	}
        	domainObject.getLocatedAtPosition().getParentContainer().setId(Long.parseLong(containerId));*/
            domainObject.getLocatedAtPosition().getParentContainer().setName(uiRepOfDomain.getSelectedContainerName());
            if (uiRepOfDomain.getPos1() != null && !uiRepOfDomain.getPos1().trim().equals(Constants.DOUBLE_QUOTES)
                    && uiRepOfDomain.getPos2() != null && !uiRepOfDomain.getPos2().trim().equals("")) {
                domainObject.getLocatedAtPosition().setPositionDimensionOne(Integer.valueOf(uiRepOfDomain.getPos1()));
                domainObject.getLocatedAtPosition().setPositionDimensionTwo(Integer.valueOf(uiRepOfDomain.getPos2()));
                domainObject.getLocatedAtPosition().setOccupiedContainer(domainObject);
            }
        }
        if (domainObject.getCreatedBy() == null) {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            domainObject.setCreatedBy(instFact.createObject());
        }
        domainObject.getCreatedBy().setId(Long.valueOf(uiRepOfDomain.getCreatedBy()));
        // done in Container class
        /*
         * capacity.setOneDimensionCapacity (new
         * Integer(uiRepOfDomain.getOneDimensionCapacity()));
         * capacity.setTwoDimensionCapacity (new
         * Integer(uiRepOfDomain.getTwoDimensionCapacity()));
         */
        domainObject.setSpecimenArrayContentCollection(uiRepOfDomain.getSpecArrayContentCollection());
        // SpecimenArrayUtil.getSpecimenContentCollection(uiRepOfDomain.
        // getSpecimenArrayGridContentList());

        // Ordering System
        try {
            if (uiRepOfDomain.getIsDefinedArray().equalsIgnoreCase("True")) {
                final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
                final SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
                        .getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
                final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = specimenArrayBizLogic
                        .getNewSpecimenArrayOrderItem(Long.valueOf(uiRepOfDomain.getNewArrayOrderItemId()));
                final Collection tempColl = new HashSet();
                tempColl.add(newSpecimenArrayOrderItem);
                domainObject.setNewSpecimenArrayOrderItemCollection(tempColl);
                newSpecimenArrayOrderItem.setSpecimenArray(domainObject);
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

}

/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(TransferEventParametersForm.class)
public class TransferEventParametersTransformer
        extends
            AbstractSpecimenEventParametersTransformer<TransferEventParametersForm, TransferEventParameters> {

    public TransferEventParameters createDomainObject(TransferEventParametersForm uiRepOfDomain) {
        TransferEventParameters transferEventParameters = (TransferEventParameters)DomainInstanceFactory.getInstanceFactory(TransferEventParameters.class).createObject();//new TransferEventParameters();
        overwriteDomainObject(transferEventParameters, uiRepOfDomain);
        return transferEventParameters;
    }

    @Override
    public void overwriteDomainObject(TransferEventParameters domainObject, TransferEventParametersForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
        final StorageContainer toObj = scInstFact.createObject();//new StorageContainer();
        if (uiRepOfDomain.getStContSelection() == 1) {
            domainObject.setToPositionDimensionOne(Integer.valueOf(uiRepOfDomain.getPositionDimensionOne()));
            domainObject.setToPositionDimensionTwo(Integer.valueOf(uiRepOfDomain.getPositionDimensionTwo()));
            toObj.setId(Long.valueOf(uiRepOfDomain.getStorageContainer()));
        } else {
            if (uiRepOfDomain.getPos1() != null && !uiRepOfDomain.getPos1().trim().equals(Constants.DOUBLE_QUOTES)
                    && uiRepOfDomain.getPos2() != null
                    && !uiRepOfDomain.getPos2().trim().equals(Constants.DOUBLE_QUOTES)) {
                domainObject.setToPositionDimensionOne(Integer.valueOf(uiRepOfDomain.getPos1()));
                domainObject.setToPositionDimensionTwo(Integer.valueOf(uiRepOfDomain.getPos2()));
            }
            toObj.setName(uiRepOfDomain.getSelectedContainerName());
        }

        domainObject.setToStorageContainer(toObj);

        if (uiRepOfDomain.getFromPositionData() !=null && !uiRepOfDomain.getFromPositionData().equalsIgnoreCase("virtual Location")) 
        {
        
        	/*int colonIndex=uiRepOfDomain.getFromPositionData() .indexOf(":");
    		int posIndex=uiRepOfDomain.getFromPositionData() .indexOf("Pos");
        	int index=uiRepOfDomain.getFromPositionData().lastIndexOf("(");
    		int lastindex=uiRepOfDomain.getFromPosition().lastIndexOf(")");
    		String[] s=uiRepOfDomain.getFromPosition().substring(index+1,lastindex).split(",");
    		String stId= uiRepOfDomain.getFromPositionData() .substring(colonIndex+1, posIndex).trim();*/
    		
    		 final StorageContainer fromObj = scInstFact.createObject();
    		 fromObj.setId(Long.valueOf(uiRepOfDomain.getFromStorageContainerId()));
    		 domainObject.setFromStorageContainer(fromObj);
    		 
    		domainObject.setFromPositionDimensionOne(Integer.valueOf(uiRepOfDomain.getFromPositionDimensionOne()));
            domainObject.setFromPositionDimensionTwo(Integer.valueOf(uiRepOfDomain.getFromPositionDimensionTwo()));
        }
        
       /* if (uiRepOfDomain.getFromStorageContainerId() == 0) {
            domainObject.setFromStorageContainer(null);
            domainObject.setFromPositionDimensionOne(null);
            domainObject.setFromPositionDimensionTwo(null);
        } else {
            final StorageContainer fromObj = scInstFact.createObject();
            fromObj.setId(Long.valueOf(uiRepOfDomain.getFromStorageContainerId()));
            domainObject.setFromStorageContainer(fromObj);

            domainObject.setFromPositionDimensionOne(Integer.valueOf(uiRepOfDomain.getFromPositionDimensionOne()));
            domainObject.setFromPositionDimensionTwo(Integer.valueOf(uiRepOfDomain.getFromPositionDimensionTwo()));
        }
*/
        // catch (final Exception excp)
        // {
        // TransferEventParameters.logger.error(excp.getMessage(),excp);
        // excp.printStackTrace();
        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
        // throw new AssignDataException(errorKey, null,
        // "TransferEventParameters.java :");
        // }
    }

}

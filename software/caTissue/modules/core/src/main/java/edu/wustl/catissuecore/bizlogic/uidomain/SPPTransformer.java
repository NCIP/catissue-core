package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.SPPForm;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(SPPForm.class)
public class SPPTransformer implements UIDomainTransformer<SPPForm, SpecimenProcessingProcedure>
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.UIDomainTransformer#createDomainObject(edu.wustl.common.domain.UIRepOfDomain)
	 */
	@Override
	public SpecimenProcessingProcedure createDomainObject(SPPForm uiRepOfDomain)
	{
		SpecimenProcessingProcedure spp = (SpecimenProcessingProcedure)DomainInstanceFactory.getInstanceFactory(SpecimenProcessingProcedure.class).createObject();
	    overwriteDomainObject(spp, uiRepOfDomain);
	    return spp;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.UIDomainTransformer#overwriteDomainObject(edu.wustl.common.domain.AbstractDomainObject, edu.wustl.common.domain.UIRepOfDomain)
	 */
	@Override
	public void overwriteDomainObject(SpecimenProcessingProcedure spp, SPPForm uiRepOfDomain)
	{
		spp.setName(uiRepOfDomain.getName());
		String barcode = uiRepOfDomain.getBarcode();
		if(barcode!=null && barcode.length() == 0)
		{
			spp.setBarcode(null);
		}
		else
		{
			spp.setBarcode(barcode);
		}
	}
}

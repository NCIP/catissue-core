package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.SOPForm;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(SOPForm.class)
public class SOPTransformer implements UIDomainTransformer<SOPForm, SOP>
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.UIDomainTransformer#createDomainObject(edu.wustl.common.domain.UIRepOfDomain)
	 */
	@Override
	public SOP createDomainObject(SOPForm uiRepOfDomain)
	{
		SOP sop = (SOP)DomainInstanceFactory.getInstanceFactory(SOP.class).createObject();
	    overwriteDomainObject(sop, uiRepOfDomain);
	    return sop;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.UIDomainTransformer#overwriteDomainObject(edu.wustl.common.domain.AbstractDomainObject, edu.wustl.common.domain.UIRepOfDomain)
	 */
	@Override
	public void overwriteDomainObject(SOP sop, SOPForm uiRepOfDomain)
	{
		sop.setName(uiRepOfDomain.getName());
		String barcode = uiRepOfDomain.getBarcode();
		if(barcode!=null && barcode.length() == 0)
		{
			sop.setBarcode(null);
		}
		else
		{
			sop.setBarcode(barcode);
		}
	}
}

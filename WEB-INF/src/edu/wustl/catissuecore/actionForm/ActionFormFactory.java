/**
 * <p>Title: ActionFormFactory Class>
 * <p>Description:	ActionFormFactory is a factory that returns instances of action formbeans 
 * as per the domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 19, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.List;

import edu.wustl.catissuecore.domainobject.Biohazard;
import edu.wustl.catissuecore.domainobject.CancerResearchGroup;
import edu.wustl.catissuecore.domainobject.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domainobject.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domainobject.CollectionEventParameters;
import edu.wustl.catissuecore.domainobject.CollectionProtocol;
import edu.wustl.catissuecore.domainobject.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domainobject.Department;
import edu.wustl.catissuecore.domainobject.DisposalEventParameters;
import edu.wustl.catissuecore.domainobject.Distribution;
import edu.wustl.catissuecore.domainobject.DistributionProtocol;
import edu.wustl.catissuecore.domainobject.EmbeddedEventParameters;
import edu.wustl.catissuecore.domainobject.FixedEventParameters;
import edu.wustl.catissuecore.domainobject.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domainobject.FrozenEventParameters;
import edu.wustl.catissuecore.domainobject.Institution;
import edu.wustl.catissuecore.domainobject.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domainobject.Participant;
import edu.wustl.catissuecore.domainobject.ProcedureEventParameters;
import edu.wustl.catissuecore.domainobject.ReceivedEventParameters;
import edu.wustl.catissuecore.domainobject.Site;
import edu.wustl.catissuecore.domainobject.Specimen;
import edu.wustl.catissuecore.domainobject.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domainobject.SpunEventParameters;
import edu.wustl.catissuecore.domainobject.StorageContainer;
import edu.wustl.catissuecore.domainobject.StorageType;
import edu.wustl.catissuecore.domainobject.ThawEventParameters;
import edu.wustl.catissuecore.domainobject.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domainobject.TransferEventParameters;
import edu.wustl.catissuecore.domainobject.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * ActionFormFactory is a factory that returns instances of action formbeans 
 * as per the domain objects.
 * @author aniruddha_phadnis
 */
public class ActionFormFactory
{
	/**
     * Returns the instance of formbean as per given domain object.
     * @param object The instance of domain object
     * @param operation The operation(Add/Edit) that is to be performed
     * @return the instance of formbean.
     * @see #setMessageList(List)
     */
	public static AbstractActionForm getFormBean(Object object,String operation) throws Exception
	{
		AbstractActionForm form = null;

		if(object instanceof User)
		{
			form = new UserForm();
		}
		else if(object instanceof Institution)
		{
			form = new InstitutionForm();
		}
		else if(object instanceof Department)
		{
			form = new DepartmentForm();
		}
		else if(object instanceof CancerResearchGroup)
		{
			form = new CancerResearchGroupForm();
		}
		else if(object instanceof Site)
		{
			form = new SiteForm();
		}
		else if(object instanceof StorageType)
		{
			form = new StorageTypeForm();
		}
		else if(object instanceof StorageContainer)
		{
			form = new StorageContainerForm();
		}
		else if(object instanceof Biohazard)
		{
			form = new BiohazardForm();
		}
		else if(object instanceof CollectionProtocol)
		{
			form = new CollectionProtocolForm();
		}
		else if(object instanceof DistributionProtocol)
		{
			form = new DistributionProtocolForm();
		}
		else if(object instanceof Participant)
		{
			form = new ParticipantForm();
		}
		else if(object instanceof CollectionProtocolRegistration)
		{
			form = new CollectionProtocolRegistrationForm();
		}
		else if(object instanceof SpecimenCollectionGroup)
		{
			form = new SpecimenCollectionGroupForm();
		}
		else if(object instanceof Specimen)
		{
			if(operation.equals(Constants.EDIT))
			{
				form = new NewSpecimenForm();
			}
			else
			{
				Specimen specimen = (Specimen)object;

				if(specimen.getSpecimenCollectionGroup() != null)
				{
					form = new NewSpecimenForm();
				}
				else
				{
					form = new CreateSpecimenForm();
				}
			}
		}
		else if(object instanceof Distribution)
		{
			form = new DistributionForm();
		}
		else if(object instanceof CellSpecimenReviewParameters)
		{
			form = new CellSpecimenReviewParametersForm();
		}
		else if(object instanceof CheckInCheckOutEventParameter)
		{
			form = new CheckInCheckOutEventParametersForm();
		}
		else if(object instanceof CollectionEventParameters)
		{
			form = new CollectionEventParametersForm();
		}
		else if(object instanceof DisposalEventParameters)
		{
			form = new DisposalEventParametersForm();
		}
		else if(object instanceof EmbeddedEventParameters)
		{
			form = new EmbeddedEventParametersForm();
		}
		else if(object instanceof FixedEventParameters)
		{
			form = new FixedEventParametersForm();
		}
		else if(object instanceof FluidSpecimenReviewEventParameters)
		{
			form = new FluidSpecimenReviewEventParametersForm();
		}
		else if(object instanceof FrozenEventParameters)
		{
			form = new FrozenEventParametersForm();
		}
		else if(object instanceof MolecularSpecimenReviewParameters)
		{
			form = new MolecularSpecimenReviewParametersForm();
		}
		else if(object instanceof ProcedureEventParameters)
		{
			form = new ProcedureEventParametersForm();
		}
		else if(object instanceof ReceivedEventParameters)
		{
			form = new ReceivedEventParametersForm();
		}
		else if(object instanceof SpunEventParameters)
		{
			form = new SpunEventParametersForm();
		}
		else if(object instanceof ThawEventParameters)
		{
			form = new ThawEventParametersForm();
		}
		else if(object instanceof TransferEventParameters)
		{
			form = new TransferEventParametersForm();
		}
		else if(object instanceof TissueSpecimenReviewEventParameters)
		{
			form = new TissueSpecimenReviewEventParametersForm();
		}
		else
		{
		    throw new Exception("Invalid Object for Add/Edit Operation");
		}
		
		return form;
	}
}
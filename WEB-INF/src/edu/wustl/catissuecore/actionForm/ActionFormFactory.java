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

import java.lang.reflect.Method;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.factory.AbstractActionFormFactory;

/**
 * ActionFormFactory is a factory that returns instances of action formbeans 
 * as per the domain objects.
 * @author aniruddha_phadnis
 */
public class ActionFormFactory extends AbstractActionFormFactory
{
	/**
     * Returns the instance of formbean as per given domain object.
     * @param object The instance of domain object
     * @param operation The operation(Add/Edit) that is to be performed
     * @return the instance of formbean.
     * @see #setMessageList(List)
     */
	public AbstractActionForm getFormBean(Object object,String operation) throws Exception
	{
		if (operation.equals(Constants.LOGIN))
		{
		    LoginForm loginForm = new LoginForm();
		    edu.wustl.catissuecore.domain.User user = (edu.wustl.catissuecore.domain.User) object;
		    loginForm.setLoginName(user.getLoginName());
//		    loginForm.setPassword(user.getPassword());
		    return loginForm;
		}
		else if (operation.equals(Constants.LOGOUT))
		{
		    return null;
		}
		
		if(object == null)
		{
			throw new Exception("Object should not be null while performing Add/Edit operation");
		}
		
		if(isIdNull(object))
		{
			throw new Exception("Id field of an object should not be null");
		}
		
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
				else if(specimen.getParentSpecimen() != null)
				{
					form = new CreateSpecimenForm();
				}
				else
				{
					throw new Exception("Either Specimen Collection Group or Parent Specimen should be present.");
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
		else if (object instanceof SpecimenArrayType)
		{
			form = new SpecimenArrayTypeForm();
		}
		else if (object instanceof SpecimenArray)
		{
			form = new SpecimenArrayForm();
		} else
		{
		    throw new Exception("Invalid Object for Add/Edit Operation");
		}
		
		form.setOperation(operation);
		return form;
	}
	
	private static boolean isIdNull(Object domainObject) throws Exception
	{
		Method getIdMethod=domainObject.getClass().getMethod("getId",new Class[]{});
		Object object = getIdMethod.invoke(domainObject,new Object[]{});
		
		if(object == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;

public class HookEntityUtil 
{
	public static void updateSCGRecordEntry(SpecimenCollectionGroup scg, SpecimenCollectionGroup persistentSCG)
	{
		Collection<SCGRecordEntry> externalIdentifierCollection = scg.getScgRecordEntryCollection();
		
		if (externalIdentifierCollection != null) {
			Iterator<SCGRecordEntry> iterator = externalIdentifierCollection
					.iterator();
			Collection<SCGRecordEntry> perstExIdColl = persistentSCG
					.getScgRecordEntryCollection();
			while (iterator.hasNext()) {
				final SCGRecordEntry exId = (SCGRecordEntry) iterator
						.next();
				updateRecordEntry(persistentSCG, perstExIdColl, exId);
			}
			persistentSCG
			.setScgRecordEntryCollection(perstExIdColl);
		}
	}
	
	public static void updateSpecimenRecordEntry(Specimen specimen, Specimen persistentSpecimen)
	{
		Collection<SpecimenRecordEntry> externalIdentifierCollection = specimen.getSpecimenRecordEntryCollection();
		
		if (externalIdentifierCollection != null) {
			Iterator<SpecimenRecordEntry> iterator = externalIdentifierCollection
					.iterator();
			Collection<SpecimenRecordEntry> perstExIdColl = persistentSpecimen.getSpecimenRecordEntryCollection();
			while (iterator.hasNext()) {
				final SpecimenRecordEntry exId = (SpecimenRecordEntry) iterator
						.next();
				updateRecordEntry(persistentSpecimen, perstExIdColl, exId);
			}
			persistentSpecimen.setSpecimenRecordEntryCollection(perstExIdColl);
		}
	}

	private static void updateRecordEntry(
			Object persistentSCG,
			Collection perstExIdColl, final AbstractRecordEntry exId) {
		AbstractRecordEntry persistExId = null;
		if (exId.getId() == null) 
		{
			if(exId instanceof SCGRecordEntry)
				((SCGRecordEntry)exId).setSpecimenCollectionGroup((SpecimenCollectionGroup)persistentSCG);
			else if(exId instanceof SpecimenRecordEntry)
				((SpecimenRecordEntry)exId).setSpecimen((Specimen)persistentSCG);
			else if(exId instanceof ParticipantRecordEntry)
				((ParticipantRecordEntry)exId).setParticipant((Participant)persistentSCG);
			
			persistExId = exId;
			perstExIdColl.add(persistExId);
		} else {
			persistExId = (AbstractRecordEntry)getCorrespondingOldObject(perstExIdColl,
							exId.getId());
			persistExId.setActivityStatus(exId.getActivityStatus());
			persistExId.setAdminuser(exId.getAdminuser());
			persistExId.setFacilityId(exId.getFacilityId());
			persistExId.setModifiedBy(exId.getModifiedBy());
			persistExId.setModifiedDate(exId.getModifiedDate());
			persistExId.setRoleId(exId.getRoleId());
		}
	}
	
	public static Object getCorrespondingOldObject(Collection objectCollection, Long identifier)
	{
		Iterator iterator = objectCollection.iterator();
		AbstractDomainObject abstractDomainObject = null;
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObj = (AbstractDomainObject) iterator.next();

			if (identifier != null && identifier.equals(abstractDomainObj.getId()))
			{
				abstractDomainObject = abstractDomainObj;
				break;
			}
		}
		return abstractDomainObject;
	}
	
	public static void handleScgSppDataEntry(SpecimenCollectionGroup scg,SessionDataBean sessionDataBean) throws IllegalArgumentException, BizLogicException, IllegalAccessException, InvocationTargetException
	{
		if(scg.getSppApplicationCollection() != null)
		{
			Collection<SpecimenProcessingProcedureApplication> sppColl = scg.getSppApplicationCollection();
			for (SpecimenProcessingProcedureApplication sppApp : sppColl) 
			{
				if(sppApp.getId() == null)
				{
					Collection<ActionApplication> appColl = sppApp.getSppActionApplicationCollection();
						handleActionAppRcdNtry(appColl, sessionDataBean, sppApp.getSpp());
				}
			}
		}
	}
	
	public static void handleSPPDataNtry(Specimen persistentSpecimen,SessionDataBean sessionDataBean) throws BizLogicException, IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		if(persistentSpecimen.getActionApplicationCollection() != null)
		{
			handleActionAppRcdNtry(persistentSpecimen.getActionApplicationCollection(),sessionDataBean,null);
		}
		if(persistentSpecimen.getProcessingSPPApplication() != null)
		{
			SpecimenProcessingProcedureApplication application = persistentSpecimen.getProcessingSPPApplication();
			if(application.getSppActionApplicationCollection() != null)
			{
				handleActionAppRcdNtry(application.getSppActionApplicationCollection(), sessionDataBean,application.getSpp());
			}
		}
	}
	public static void handleActionAppRcdNtry(Collection<ActionApplication> actionApplicationCollection,SessionDataBean sessionDataBean,
			SpecimenProcessingProcedure spp) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, BizLogicException
	{
		if(actionApplicationCollection != null)
		{
			for (ActionApplication actionApp : actionApplicationCollection) 
			{
				if(actionApp.getId() == null) 
				{
					//if(actionApp.getPerformedBy() = null)
					User user = new User();
					user.setId(sessionDataBean.getUserId());
					actionApp.setPerformedBy(user);
					ActionApplicationRecordEntry entry = actionApp.getApplicationRecordEntry();
					if(entry != null)
					{
						Method[] methods = entry.getClass().getMethods();
						for (Method method : methods) 
						{
							if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
							{
								Object val = method.invoke(entry, (Object[])null);
								if(val != null && ((val instanceof Set && !((Set)val).isEmpty()) || (val instanceof Collection && !((Collection)val).isEmpty())))
								{
									AnnotationBizLogic bizLogic = new AnnotationBizLogic();
									if(spp == null)
									{
										updateRecNtry(sessionDataBean.getUserName(), entry,
												val);
									}
									else
									{
										updateRecNtry(sessionDataBean.getUserName(), entry,
												val,spp.getId());
									}
									
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void updateScgSpp(SpecimenCollectionGroup scg, SpecimenCollectionGroup persistentScg)
	{
		final Collection<SpecimenProcessingProcedureApplication> sppAppColl = scg
		.getSppApplicationCollection();
		if (sppAppColl != null) 
		{
			final Iterator<SpecimenProcessingProcedureApplication> sppAppItr = sppAppColl
					.iterator();
			final Collection<SpecimenProcessingProcedureApplication> persistSppAppColl = persistentScg
					.getSppApplicationCollection();
			while (sppAppItr.hasNext()) 
			{
				final SpecimenProcessingProcedureApplication sppApp = (SpecimenProcessingProcedureApplication) sppAppItr
						.next();
				
				SpecimenProcessingProcedureApplication persistSppApp = null;
				if (sppApp.getId() == null)
				{
					persistSppApp = sppApp;
					persistSppAppColl.add(sppApp);
				} 
				else 
				{
					persistSppApp = (SpecimenProcessingProcedureApplication)
							getCorrespondingOldObject(persistSppAppColl,
									sppApp.getId());
					updateActionSpp(persistSppApp,sppApp);
				}
			}
			persistentScg.setSppApplicationCollection(persistSppAppColl);
		}

	}

	private static void updateActionSpp(
			SpecimenProcessingProcedureApplication persistSppApp,
			SpecimenProcessingProcedureApplication sppApp) 
	{
		Collection<ActionApplication> actionAppColl = sppApp.getSppActionApplicationCollection();
		Collection<ActionApplication> persistActionAppColl = persistSppApp.getSppActionApplicationCollection();
		for (ActionApplication actionApplication : actionAppColl) 
		{
			ActionApplication application = null;
			if(actionApplication.getId()==null)
			{
				persistActionAppColl.add(actionApplication);
			}
			else
			{
				application = (ActionApplication)getCorrespondingOldObject(persistActionAppColl, actionApplication.getId());
				
			}
		}
		persistSppApp.setSppActionApplicationCollection(persistActionAppColl);
	}
	
	public static void updateRecNtry(String userName,
			ActionApplicationRecordEntry recordEntry, Object val, Long sppId) throws BizLogicException 
	{
		 AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		Long id = AnnotationBizLogic.getContainerId(getClassName(val)); 
		Action action = null;  
		String query = "select spp.actionCollection from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure spp " +
				" join spp.actionCollection action where spp.id="+sppId+" and" +
				" action.containerId="+id;
		
		List<Action> sfcList = bizLogic.executeQuery(query);
		for (Action action2 : sfcList) 
		{
			if(id.equals(action2.getContainerId()))
			{
				action = action2;
				break;
			}
		}
		recordEntry.setFormContext(action);
		recordEntry.setModifiedDate(new Date());
		recordEntry.setModifiedBy(userName);
		
	}
	
	private static String getClassName(Object val) {
		String className = null;
		if(val instanceof Set && !((Set) val).isEmpty())
			className = ((java.util.Set) val).iterator().next().getClass().getName();
		else if(val instanceof Collection && !((Collection)val).isEmpty())
			className = ((Collection) val).iterator().next().getClass().getName();
		return className;
	}

	
	public static void updateRecNtry(String userName, AbstractRecordEntry recordEntry, Object val)
	throws BizLogicException 
	{

		String className = null;
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		if(val instanceof Set && !((java.util.Set) val).isEmpty())
			className = ((java.util.Set) val).iterator().next().getClass().getName();
		else if(val instanceof Collection && !((Collection) val).isEmpty())
			className = ((Collection) val).iterator().next().getClass().getName();
		
		if(className != null)
		{
			Long id = AnnotationBizLogic.getContainerId(className);
			StudyFormContext context = bizLogic.getStudyFormContext(id);//tudyFormContextFactory.getInstance().createObject();
			recordEntry.setFormContext(context);
			recordEntry.setModifiedDate(new Date());
			recordEntry.setModifiedBy(userName);
		}
	}

}

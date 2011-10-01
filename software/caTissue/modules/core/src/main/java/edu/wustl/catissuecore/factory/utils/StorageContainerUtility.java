package edu.wustl.catissuecore.factory.utils;

import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;


public class StorageContainerUtility
{
	/**
	 * @param form StorageContainerForm
	 * @throws ApplicationException ApplicationException
	 */
	private void setSpTypeColl(final StorageContainerForm form, final StorageContainer container) throws ApplicationException
	{
		container.setHoldsSpecimenTypeCollection(new HashSet<String>());
		if (form.getSpecimenOrArrayType().equals("Specimen"))
		{
			StorageContainerUtil.setSpTypeColl(form, container);
		}
	}

/*	*//**
	 * @param form StorageContainerForm
	 *//*
	private void setClassColl(final StorageContainerForm form, StorageContainer container)
	{
		container.setHoldsSpecimenClassCollection(new HashSet<String>());
		if (form.getSpecimenOrArrayType().equals("Specimen"))
		{
			final String[] specimenClassArr = form.getHoldsSpecimenClassTypes();
			if (specimenClassArr != null)
			{
				for (final String element : specimenClassArr)
				{
					if (element.equals("-1"))
					{
						container.getHoldsSpecimenClassCollection()
								.addAll(AppUtility.getSpecimenClassTypes());
						break;
					}
					else
					{
						container.getHoldsSpecimenClassCollection().add(element);
					}
				}
			}
		}
	}*/

}

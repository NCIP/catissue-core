package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.domain.ContainerType;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.UIDomainTransformer;

/* TODO There is no ContainerTypeForm */
public abstract class ContainerTypeTransformer<U extends AbstractActionForm, D extends ContainerType>
implements
UIDomainTransformer<U, D> {

public abstract D createDomainObject(U uiRepOfDomain);

// TODO I curse thee; thou shalt not have a body until there arrives a
// ContainerTypeForm...
// besides, ContainerType.setAllValues() does NOTHING (WHY??).
public abstract void overwriteDomainObject(D domainObject, U uiRepOfDomain);

}

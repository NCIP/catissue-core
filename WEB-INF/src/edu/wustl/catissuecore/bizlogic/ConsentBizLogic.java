package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.dao.HibernateDAO;


public class ConsentBizLogic  extends CatissueDefaultBizLogic
{

    public void updateConsent(ConsentTierResponse consentResponse,HibernateDAO hibernateDAO){
        consentResponse.getConsentTier();
    }
}

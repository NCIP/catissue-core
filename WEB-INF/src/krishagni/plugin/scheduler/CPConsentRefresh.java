
package krishagni.plugin.scheduler;

import java.sql.ResultSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.dao.HibernateDAO;

public class CPConsentRefresh
{

    public static void refreshCP(Long cpId) throws Exception
    {
        HibernateDAO hibernateDAO = null;
        try
        {
            String hql = "from CollectionProtocolRegistration cpr join cpr.collectionProtocol cp join cp.consentTierCollection  "
                    + "  where cp.id = 323";
            System.out.println("");
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);
            List<?> resultList = hibernateDAO.executeQuery(hql, null);
            System.out.println("");
            
            hql = " select response "+ 
                    "from CollectionProtocolRegistration cpr "+
                    "join cpr.collectionProtocol.consentTierCollection consentTier "+
                    " left join cpr.consentTierResponseCollection response " +
                    "where cpr.id=915  order by consentTier.id  asc ";
            resultList = hibernateDAO.executeQuery(hql, null);
            String sql = "select catissue_consent_tier.identifier as ct_id,catissue_consent_tier.statement as ct_stmt,"
                    + "catissue_consent_tier_response.identifier as ctr_id, cpr.identifier as cpr_id from catissue_consent_tier "
                    + "left join CATISSUE_CONSENT_TIER_RESPONSE  on CATISSUE_CONSENT_TIER.identifier = catissue_consent_tier_response.consent_tier_id "
                    + "left join catissue_coll_prot_reg cpr on cpr.collection_protocol_id = catissue_consent_tier.coll_protocol_id "
                    + "where catissue_consent_tier.coll_protocol_id = 323 and catissue_consent_tier_response.identifier is null";
            ResultSet rs = hibernateDAO.executeNamedSQLQuery("getNewCpConsent", null);
            
            while (rs.next())
            {
                rs.getLong("ct_id");
                rs.getString("ct_stmt");
                ConsentTierResponse consentResponse = new ConsentTierResponse();
                ConsentTier consetTier = new ConsentTier();
                consetTier.setId(rs.getLong("ct_id"));
                consentResponse.setConsentTier(consetTier);
                consentResponse.setResponse(Constants.NOT_SPECIFIED);
                
                CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)hibernateDAO.retrieveById(CollectionProtocolRegistration.class.getName(), rs.getLong("cpr_id"));
                cpr.getConsentTierResponseCollection().add(consentResponse);
                hibernateDAO.update(cpr);
                
            }
            hibernateDAO.commit();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally
        {
            if (hibernateDAO != null)
            {
                AppUtility.closeDAOSession(hibernateDAO);
            }
        }
    }
}

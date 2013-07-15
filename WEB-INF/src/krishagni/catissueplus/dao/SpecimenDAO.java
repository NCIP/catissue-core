
package krishagni.catissueplus.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class SpecimenDAO
{

    public Long getCpIdFromSpecimenId(Long specimenId, HibernateDAO hibernateDao) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, specimenId));
        List specimenDetailColl = hibernateDao.executeNamedQuery("getCpIdFromSpecimenId", params);
        return (Long) specimenDetailColl.get(0);
    }

    public Long getCpIdFromSpecimenLabel(String specimenLable, HibernateDAO hibernateDao) throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, specimenLable));
        List specimenDetailColl = hibernateDao.executeNamedQuery("getCpIdFromSpecimenLabel", params);
        return (Long) specimenDetailColl.get(0);
    }

    public Specimen getParentSpecimen(HibernateDAO hibernateDao, String label) throws ApplicationException
    {
        Specimen specimen = null;
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();

        Collection<?> specimenDetailColl;

        params.put("0", new NamedQueryParam(DBTypes.STRING, label));

        specimenDetailColl = hibernateDao.executeNamedQuery("selectParentSpecimenDetailsForAliquot", params);
        if (specimenDetailColl == null || specimenDetailColl.isEmpty())
        {

            specimenDetailColl = hibernateDao.executeNamedQuery("selectParentSpecimenDetailsForAliquotByBarcode",
                    params);
        }

        if (specimenDetailColl == null || specimenDetailColl.isEmpty())
        {
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_LABEL_BARCODE.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_LABEL_BARCODE.getCode());

            //			throw new BizLogicException(null, null, Constants.INVALID_LABEL_BARCODE);
        }
        Iterator specimenDetailIterator = specimenDetailColl.iterator();
        if (specimenDetailIterator.hasNext())
        {

            final Object[] valArr = (Object[]) specimenDetailIterator.next();
            if (valArr != null)
            {
                //specimenDTO = getSpecimenDTOObject(valArr);
                specimen = new Specimen();
                SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
                scg.setId(Long.parseLong(valArr[1].toString()));
                specimen.setSpecimenCollectionGroup(scg);
                specimen.setId(Long.parseLong(valArr[2].toString()));
                specimen.setLabel(valArr[3].toString());
                specimen.setBarcode(valArr[4].toString());
                specimen.setSpecimenClass(valArr[5].toString());
                specimen.setSpecimenType(valArr[6].toString());
                specimen.setPathologicalStatus(valArr[7].toString());
                SpecimenCharacteristics specimenChar = new SpecimenCharacteristics();
                specimenChar.setTissueSite(valArr[8].toString());
                specimenChar.setTissueSide(valArr[9].toString());
                specimen.setSpecimenCharacteristics(specimenChar);
                specimen.setAvailableQuantity(Double.parseDouble(valArr[10].toString()));
                if (valArr[11] != null)
                    specimen.setConcentrationInMicrogramPerMicroliter(Double.parseDouble(valArr[11].toString()));
                specimen.setInitialQuantity(Double.parseDouble(valArr[12].toString()));

            }

        }
        return specimen;

    }

}

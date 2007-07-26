package edu.wustl.catissuecore.util.global.namegenerator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * SpecimenLabelGenerator for Michgam University.
 * Format for specimen: site_yy_ddd_4DigitAutoIncrementingNumber
 * Format for derived specimen: parentSpecimenLabel_childCount+1
 * Format for derived specimen: parentSpecimenLabel_childCount+1
 */
public class SpecimenLabelGeneratorForMichigan extends DefaultSpecimenLableGenerator {

	public SpecimenLabelGeneratorForMichigan() {
		super();
	}

	/**
	 * This is a init() function it is called from the default constructor of
	 * Base class. When getInstance of base class called then this init function
	 * will be called. This method will first check the Datatbase Name and then
	 * set function name that will convert lable from int to String
	 */
	protected void init() 
	{
		String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		currentLable = new Long("0");
		try {
			jdbcDao.openSession(null);
			List resultList = jdbcDao.executeQuery(sql, null, false, null);
			if (resultList != null && !resultList.isEmpty()) {
				String number = (String) ((List) resultList.get(0)).get(0);
				if (number != null && !number.equals("")) {
					currentLable = Long.parseLong(number);
				}
			}
			jdbcDao.closeSession();
		} catch (DAOException daoexception) {
			daoexception.printStackTrace();
		} catch (ClassNotFoundException classnotfound) {
			classnotfound.printStackTrace();
		}
	}

	@Override
	/**
	 * Get next specimen Label
	 * Format for specimen: site_yy_ddd_4DigitAutoIncrementingNumber
	 * @param obj
	 * @return Specimen label
	 */
	public synchronized String getNextAvailableSpecimenlabel(Object obj) 
	{
		validate(obj);
		String siteName = getSiteName((Map) obj);

		currentLable = currentLable + 1;

		String year = new SimpleDateFormat("yy").format(new Date());
		String day = format(Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
				"000");
		String nextNumber = format(currentLable, "0000");

		persistLabelCount();
		return siteName + "-" + year + "-" + day + "-" + nextNumber;
	}

	/**
	 * Returns the next 'count' number of specimen labels
	 * Format for specimen: site_yy_ddd_4DigitAutoIncrementingNumber
	 * @param obj input values
	 * @return Collection of Specimen label
	 */
	public List<String> getNextAvailableSpecimenlabel(Object obj, int count) 
	{
		validate(obj);
		String siteName = getSiteName((Map) obj);

		List<String> labels = new ArrayList<String>();
		String year = new SimpleDateFormat("yy").format(new Date());
		String day = format(Calendar.getInstance().get(Calendar.DAY_OF_YEAR), "000");
		StringBuffer prefix = new StringBuffer(siteName + "-" + year + "-" + day + "-");
		StringBuffer label;
		
		for (int i = 0; i < count; i++) 
		{
			currentLable = currentLable + 1;
			label = new StringBuffer().append(prefix).append( format(currentLable, "0000") );
			labels.add(label.toString());
		}

		persistLabelCount();
		return labels;
	}

	@Override
	/**
	 * Get next derived label
	 * Format for specimen: parentLabel_2DigitChildCount
	 * @param obj
	 * @return Derive Specimen label
	 */
	public synchronized String getNextAvailableDeriveSpecimenlabel(Object parentSpecimenMap) 
	{
		String parentSpecimenId = (String) (((Map) parentSpecimenMap)
				.get(Constants.PARENT_SPECIMEN_ID_KEY));
		String parentSpecimenLabel = (String) (((Map) parentSpecimenMap)
				.get(Constants.PARENT_SPECIMEN_LABEL_KEY));

		long aliquotCount = getChildCount(parentSpecimenId);
		return parentSpecimenLabel + "_" + (format((aliquotCount + 1), "00"));
	}

	@Override
	/**
	 * Get next available aliquot labels. It will return list containing 'count' number of labels
	 * Format for specimen: parentLabel_2DigitChildCount
	 * @param obj
	 * @return aliquot label collection
	 */
	public synchronized List<String> getNextAvailableAliquotSpecimenlabel(Object parentSpecimenMap, int count) 
	{
		String parentSpecimenId = (String) (((Map) parentSpecimenMap)
				.get(Constants.PARENT_SPECIMEN_ID_KEY));
		String parentSpecimenLabel = (String) (((Map) parentSpecimenMap)
				.get(Constants.PARENT_SPECIMEN_LABEL_KEY));

		List<String> labels = new ArrayList<String>();
		long aliquotCount = getChildCount(parentSpecimenId);

		for (int i = 0; i < count; i++) {
			labels.add(parentSpecimenLabel + "_"
					+ format((++aliquotCount), "00"));
		}

		return labels;
	}

	private String format(long input, String pattern) 
	{
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	private void persistLabelCount() 
	{
		String sql = "update CATISSUE_SPECIMEN_LABEL_COUNT SET LABEL_COUNT='"
				+ currentLable + "'";

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		try {
			jdbcDao.openSession(null);
			jdbcDao.executeUpdate(sql);
			jdbcDao.closeSession();
		} catch (DAOException daoexception) {
			daoexception.printStackTrace();
		}
	}

	private void validate(Object obj) {
		if (!(obj instanceof Map) || !(((Map) obj).containsKey(Constants.SCG_NAME_KEY))) {
			throw new RuntimeException(
					"SCG name not provided to LabelGenerator");
		}
	}

	private String getSiteName(Map map) {
		String scgName = (String) map.get(Constants.SCG_NAME_KEY);
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();

		String sourceObjectName = SpecimenCollectionGroup.class.getName();
		String[] selectColumnName = { "site.name" };
		String[] whereColumnName = { "name" };
		String[] whereColumnCondition = { "=" };
		Object[] whereColumnValue = { scgName };
		String joinCondition = null;
		String siteName = "";

		try {
			List list = defaultBizLogic.retrieve(sourceObjectName,
					selectColumnName, whereColumnName, whereColumnCondition,
					whereColumnValue, joinCondition);

			if (list.size() > 0) {
				siteName = (String) list.get(0);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return siteName;
	}

}

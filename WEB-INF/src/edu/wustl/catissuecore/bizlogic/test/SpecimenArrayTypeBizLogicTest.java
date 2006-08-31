/*
 * <p>Title: SpecimenArrayTypeBizLogicTest Class </p>
 * <p>Description:This class is used to test various operations performed by SpecimenArrayTypeBizLogic.</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on August 31,2006
 */
package edu.wustl.catissuecore.bizlogic.test;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;

import edu.wustl.catissuecore.bizlogic.SpecimenArrayTypeBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.global.Constants;

/**
 * <p>This class is used to test various operations performed by SpecimenArrayTypeBizLogic.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayTypeBizLogicTest extends BaseBizLogicTest {
	
	/**
	 * @param name -- name of test case
	 */
	public SpecimenArrayTypeBizLogicTest(String name) {
		super(name);
	}
	
	/**
	 * Test Insert operation of Specimen Array Type Biz Logic  
	 */
	public void testInsert() {
		SpecimenArrayTypeBizLogic specimenArrayTypeBizLogic = new SpecimenArrayTypeBizLogic();
		SpecimenArrayType specimenArrayType = populateSpecimenArrayType();
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibernateDAOMock.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibernateDAOMock.expect("openSession",fullConstraintMatcher);
		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
		hibernateDAOMock.expect("insert",insertConstraintMatcher);
		hibernateDAOMock.expect("insert",insertConstraintMatcher);
		hibernateDAOMock.expect("closeSession");
		
		try
		{
			specimenArrayTypeBizLogic.insert(specimenArrayType,sessionDataBean,Constants.HIBERNATE_DAO);
			assertTrue("Specimen Array Type inserted successfully",true);
		}
		catch (NullPointerException e) {
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
	}
	
	/**
	 * populate Specimen Array Type object
	 * @return specimen array type
	 */
	public SpecimenArrayType populateSpecimenArrayType() {
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setId(new Long(1));
		specimenArrayType.setName("SpecimenArray1");
		Capacity capacity = new Capacity();
		capacity.setId(new Long(1));
		capacity.setOneDimensionCapacity(new Integer(4));
		capacity.setTwoDimensionCapacity(new Integer(4));
		specimenArrayType.setCapacity(capacity);
		specimenArrayType.setSpecimenClass("Tissue");
		return specimenArrayType;
	}
}

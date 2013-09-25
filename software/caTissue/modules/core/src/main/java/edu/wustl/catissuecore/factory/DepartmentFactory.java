/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.Department;

public class DepartmentFactory implements InstanceFactory<Department>
{
	private static DepartmentFactory departmentFactory;

	private DepartmentFactory()
	{
		super();
	}

	public static synchronized DepartmentFactory getInstance()
	{
		if(departmentFactory==null){
			departmentFactory = new DepartmentFactory();
		}
		return departmentFactory;
	}

	public Department createClone(Department obj)
	{
		Department department = createObject();
		department.setName(obj.getName());
		return department;
	}

	public Department createObject()
	{
		Department department = new Department();
		initDefaultValues(department);
		return department;
	}


	public void initDefaultValues(Department obj)
	{

	}
}

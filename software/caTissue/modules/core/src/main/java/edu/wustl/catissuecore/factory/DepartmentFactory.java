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

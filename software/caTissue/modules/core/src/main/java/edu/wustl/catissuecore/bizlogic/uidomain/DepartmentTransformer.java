/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(DepartmentForm.class)
public class DepartmentTransformer implements UIDomainTransformer<DepartmentForm, Department> {

    public Department createDomainObject(DepartmentForm form) {
        Department department = (Department)DomainInstanceFactory.getInstanceFactory(Department.class).createObject();//new Department();
        overwriteDomainObject(department, form);
        return department;
    }

    public void overwriteDomainObject(Department department, DepartmentForm form) {
        department.setName(form.getName().trim());
    }

}

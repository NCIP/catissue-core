
package com.krishagni.catissueplus.core.extapp.services;

public interface CrudService {

	public String insert(Object domainObj, String studyId);

	public String update(Object domainObj, String studyId);

	public String delete();

}

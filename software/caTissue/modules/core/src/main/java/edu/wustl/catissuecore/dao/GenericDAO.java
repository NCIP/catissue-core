/**
 * 
 */
package edu.wustl.catissuecore.dao;

import java.util.Collection;

import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author Denis G. Krylov
 * 
 */
public interface GenericDAO {

	/**
	 * @param domainObject
	 */
	public void save(AbstractDomainObject domainObject);

	/**
	 * @param <T>
	 * @param notification
	 * @return
	 */
	public <T extends AbstractDomainObject> Collection<T> findByExample(
			T domainObject);

	public <T extends AbstractDomainObject> int getCountByExample(T domainObject);

	/**
	 * @param domainObject
	 */
	public void update(AbstractDomainObject domainObject);
	
	/**
	 * @param domainObject
	 */
	public void initialize(Object domainObject);
	
	public void refresh(AbstractDomainObject domainObject);
	
	public void delete(AbstractDomainObject domainObject);
	
	/**
	 * Clears persistence context completely. 
	 */
	public void clear();

	public <T extends AbstractDomainObject> T getById(long id, Class<T> cls);
	
	public void synchronizeWithDatabase();

	public abstract void bulkUpdate(String queryString);

}

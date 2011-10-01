package edu.wustl.catissuecore.cacore;

import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.WritableDAO;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.ExampleManipulationQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import gov.nih.nci.system.query.hql.DeleteHQLQuery;
import gov.nih.nci.system.query.hql.HQLManipulationQuery;
import gov.nih.nci.system.query.hql.InsertHQLQuery;
import gov.nih.nci.system.query.hql.UpdateHQLQuery;

/**
 * Class which implements DAO of caCORE SDK. This class gives implementation to
 * query CaTissueCore database by calling delegator.
 *
 */
public class CatissueCoreWritableDAOImpl extends CatissueCoreDAOImpl implements
		WritableDAO {

	/**
	 * Method to query.
	 *
	 * @param request
	 *            Request object
	 * @return Response object
	 * @throws DAOException
	 *             caCORE DAO Exception (non-Javadoc)
	 * @see gov.nih.nci.system.dao.DAO#query(gov.nih.nci.system.dao.Request)
	 */
	public Response query(Request request) throws DAOException {
		Response response = null;
		Object queryObject = request.getRequest();
		if (queryObject instanceof ExampleManipulationQuery
				|| queryObject instanceof HQLManipulationQuery) {
			Object result = null;
			if (queryObject instanceof ExampleManipulationQuery) {
				result = runExampleQuery((SDKQuery) queryObject);
			} else if (queryObject instanceof HQLManipulationQuery) {
				result = runHQLQuery((SDKQuery) queryObject);
			}

			response = new Response();
			response.setResponse(result);
			response.getRowCount();
		} else {
			response = super.query(request);
		}

		return response;
	}

	private Object runExampleQuery(SDKQuery query) throws DAOException {
		String userName = CaTissueCoreAuthenticationUtil.retreiveUsernameFromSecurityContext();
		Object result = null;
		if (query instanceof InsertExampleQuery) {
			Object domainObject = ((InsertExampleQuery) query).getExample();
			try {
				domainObject = getApplicationServiceDelegator().insertObject(userName, domainObject);
			} catch (Exception e) {
				throw new DAOException("Error occured while running InsertExampleQuery.", e);
			}
			result = new SDKQueryResult(domainObject);
		} else if (query instanceof DeleteExampleQuery) {
			Object domainObject = ((DeleteExampleQuery) query).getExample();
			try {
				getApplicationServiceDelegator().deleteObject(domainObject);
			} catch (Exception e) {
				throw new DAOException("Error occured while running DeleteExampleQuery.", e);
			}
			result = new SDKQueryResult(true);
		} else if (query instanceof UpdateExampleQuery) {
			Object domainObject = ((UpdateExampleQuery) query).getExample();
			try {
				domainObject = getApplicationServiceDelegator().updateObject(userName, domainObject);
			} catch (Exception e) {
				throw new DAOException("Error occured while running UpdateExampleQuery.", e);
			}
			result = new SDKQueryResult(domainObject);
		}
		return result;
	}

	private Object runHQLQuery(SDKQuery query) {
		Object result = null;
		if (query instanceof InsertHQLQuery) {
			throw new UnsupportedOperationException(
					"InsertHQLQuery will be supported in the next release");
		} else if (query instanceof DeleteHQLQuery) {
			throw new UnsupportedOperationException(
					"DeleteHQLQuery will be supported in the next release");
		} else if (query instanceof UpdateHQLQuery) {
			throw new UnsupportedOperationException(
					"UpdateHQLQuery will be supported in the next release");
		}
		return result;
	}

}

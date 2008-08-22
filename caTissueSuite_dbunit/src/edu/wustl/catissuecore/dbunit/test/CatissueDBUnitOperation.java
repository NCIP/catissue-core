/**
 * @Class CatissueDBUnitOperation.java
 * @Author abhijit_naik
 * @Created on Aug 7, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.operation.AbstractOperation;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.InsertOperation;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;


/**
 * @author abhijit_naik
 *
 */
public class CatissueDBUnitOperation extends AbstractOperation
{

	public CatissueDBUnitOperation()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see org.dbunit.operation.DatabaseOperation#execute(org.dbunit.database.IDatabaseConnection, org.dbunit.dataset.IDataSet)
	 */
	public void execute(IDatabaseConnection connection, IDataSet dataSet)
			throws DatabaseUnitException, SQLException
	{
		System.out.println("***Inserting dataset ******");
		DatabaseOperation insert = DatabaseOperation.INSERT;
		insert.execute(connection, dataSet);
		ITableIterator iterator = dataSet.iterator();
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		while(iterator.next())
		{
			ITable table = iterator.getTable();
			Column[] dbColums = table.getTableMetaData().getColumns();
			for (int rowNo=0;rowNo<table.getRowCount();rowNo++)
			{
				Object objectId = table.getValue(rowNo, dbColums[0].getColumnName());
				String className = HibernateMetaData.getClassName(table.getTableMetaData().getTableName());
				insertCSMRecord(defaultBizLogic, objectId, className);
			}
		}
		
	}

	/**
	 * @param defaultBizLogic
	 * @param objectId
	 * @param className
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	private void insertCSMRecord(DefaultBizLogic defaultBizLogic, Object objectId, String className)
			throws DatabaseUnitException
	{
		try
		{
			Object domainObject = defaultBizLogic.retrieve(className, Long.valueOf(objectId.toString()));
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(className);
			bizLogic.createProtectionElement(domainObject);
		}
		catch(DAOException daoException)
		{
			throw new DatabaseUnitException(daoException);
		}
		catch(BizLogicException bizLogicException)
		{
			throw new DatabaseUnitException(bizLogicException);
		}
	}

}
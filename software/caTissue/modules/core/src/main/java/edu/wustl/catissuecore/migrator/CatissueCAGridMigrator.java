package edu.wustl.catissuecore.migrator;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.catissuecore.gridgrouper.GridGroupSync;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.AbstractMigrator;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;

public class CatissueCAGridMigrator extends AbstractMigrator
{

    public CatissueCAGridMigrator(IDPInterface targetDomain)
    {
        super(targetDomain);
    }
    
    @Override
    public void migrate(final UserDetails userDetails) throws MigratorException, DAOException
    {
        executeMigrationInsertQuery(userDetails, MigrationState.MIGRATED);
        GridGroupSync sync = new GridGroupSync();
        try {
			sync.syncSingleUser(userDetails.getLoginName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MigratorException(e);
		}
    }
    
    private void executeMigrationInsertQuery(final UserDetails userDetails, final MigrationState state)
    throws MigratorException, DAOException
		{
		try
		{
		    
			 final IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
					 userDetails.getTargetIDP());
			 final LoginCredentials loginCredentials = new LoginCredentials();
		     loginCredentials.setLoginName(userDetails.getMigratedLoginName());
		     loginCredentials.setPassword(userDetails.getPassword());
		     final String identity = authManager.getIdentity(loginCredentials);
		     
			
			final String queryStr = "INSERT INTO CSM_MIGRATE_USER(LOGIN_NAME,MIGRATED_LOGIN_NAME, TARGET_IDP_NAME, MIGRATION_STATUS , IDENTITY ) VALUES"
		            + "(?,?,?,?,?)";
		    final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
		    final ColumnValueBean loginNameBean = new ColumnValueBean(userDetails.getLoginName());
		    final ColumnValueBean migratedLoginNameBean = new ColumnValueBean(userDetails.getMigratedLoginName());
		    final ColumnValueBean targetDomainNameBean = new ColumnValueBean(userDetails.getTargetIDP());
		    final ColumnValueBean migrationStatusBean = new ColumnValueBean(state.getState());
		    final ColumnValueBean identityBean = new ColumnValueBean(identity);
		
		    parameters.add(loginNameBean);
		    parameters.add(migratedLoginNameBean);
		    parameters.add(targetDomainNameBean);
		    parameters.add(migrationStatusBean);
		    parameters.add(identityBean);
		    
		    Utility.executeQueryUsingDataSource(queryStr, parameters, true, "WUSTLKey");
		}
		// ------Niranjan's changes start here @Bugid 19485
		catch(final DAOException d)
		{
			throw d;
		}
		//-------Niranjan's changes end here
		catch (final ApplicationException appException)
		{
		    throw new MigratorException(appException);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			throw new MigratorException(e);
		}
}

    
    public void finalize()
        throws Throwable
    {
    }

}




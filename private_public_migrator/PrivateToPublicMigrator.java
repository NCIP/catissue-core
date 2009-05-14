import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.LoggerConfig;

public class PrivateToPublicMigrator
{
	private Runtime runtime;
	public static void main(String[] args) 
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
		PrivateToPublicMigrator mig=new PrivateToPublicMigrator();
		mig.init();
		try {
			mig.migrateData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init()
	{
		ApplicationProperties.initBundle("commands");
		runtime=Runtime.getRuntime();
	}
	
	public void migrateData() throws IOException, InterruptedException
	{
		createStagingPrivateInstance();
		maskData();
		importDataInPublicInstance();
	}
	
	private void maskData()
	{
		// run simple JDBC queries to mask data
		// use java date and random abilities where needed  }  private createStagingPrivateInstance()  {
		//export from production
		// delete private staging schema
		//import to private staging
		try
		{
			MaskUsingDEMetatdata mask=new MaskUsingDEMetatdata();
			
			mask.maskIdentifiedData();
		}
		catch (Exception e) 
		{
			System.out.println("Error while masking:"+e);
		}
		
	}
	private void createStagingPrivateInstance() throws IOException, InterruptedException
	{
		//TODO stop public jboss and grid service -- do not bother now --
		
		String dropPublicDBCmd=ApplicationProperties.getValue("createPrivateDump");
		executeCommand(dropPublicDBCmd);
		
		String createPubliDumpCmd=ApplicationProperties.getValue("dropStagingDB");
		executeCommand(createPubliDumpCmd);
		
		String createPublicDB=ApplicationProperties.getValue("createStagingDB");
		executeCommand(createPublicDB);
		//TODO start public jboss and grid service -- do not bother now --   
	}
	
	private void importDataInPublicInstance() throws IOException, InterruptedException
	{
		String dropPrivateDBCmd=ApplicationProperties.getValue("createStagingDump");
		executeCommand(dropPrivateDBCmd);
		
		String createPrivateDumpCmd=ApplicationProperties.getValue("dropPublicDB");
		executeCommand(createPrivateDumpCmd);
		
		String createPrivateDB=ApplicationProperties.getValue("createPublicDB");
		executeCommand(createPrivateDB);
	}
	
	private void executeCommand(String command) throws IOException, InterruptedException
	{
		Process process=null;
		
		if(!command.equalsIgnoreCase(""))
		{
			process=runtime.exec(command);
				
			 // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(process.getErrorStream());            
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(process.getInputStream());
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            
			int i=process.waitFor();
			process.destroy();
			System.out.println(command+":"+i);
		}
	}
}



class StreamGobbler extends Thread
{
    InputStream is;
    
    StreamGobbler(InputStream is)
    {
        this.is = is;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }

}

Prerequisite :
DE models war should have been deployed in the Jboss along with the caTissue war.
To deploy the de wars there is a target 'deploy_ca_model_war' in the build.xml of caTissue application.

Steps to run the demo client.
1. Run ant target 
<CATISSUE_HOME>\ant deploy_ca_model_war.
2. Change directory to 'catissue_de_integration_client'
3. Modify clientInstall.properties file.
4. Run command 
<CLIENT_HOME>\ant runDemo_CAModel' 
The above command will run the demo programme to insert the data into the ClinicalAnnotations model as well as will query the data inserted using the API's.
For more details please refer <CLIENT_HOME>\src\client\ClientDemo_CA.java
Note :
Similary there are targets like 'ant runDemo_SpecimenModel' & 'ant runDemo_SCGModel' which will query on the Pathology_specimen & Pathology_scg model respectively.
For more help please run following command
<CLIENT_HOME>ant -p
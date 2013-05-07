caTissue Suite v1.2
======================================
Deployment, User and Technical Guide available at
http://gforge.nci.nih.gov/docman/index.php?group_id=689&selected_doc_group_id=6035&language_id=1
For more information, visit the Tissue/Biospecimen Banking and Technology 
Tools Knowledge Center website: https://cabig-kc.nci.nih.gov/Biospecimen/KC/index.php/Main_Page

Prerequisites:
=======================================
Certified Server OS: Windows Microsoft Windows XP Professional Version 2002 Service Pack 2,
Linux Red Hat 9 or Red Hat Enterprise ES/AS 2.1 or higher
Certified Databases: Oracle 10.2.0.2.0, MySQL 5.0.45
Note: The Oracle client is required on the machine hosting the JBoss server.
Build tool: Ant 1.7
Application server: JBoss 4.2.2 GA
Java: JDK 1.5
Web browsers: Internet Explorer 8.0, Mozilla Firefox 3.6.3, Safari (Mac) 5.0
If deploying caTIES:
a. Download MMTx from http://catissuecore.wustl.edu/caties_datafiles/
b. Download Metathesaurus from http://catissuecore.wustl.edu/caties_datafiles/

Directories
========================================
modules: This folder contains files related to various caTissue modules
SQL: This folder has all the database creation SQL scripts in it

Files
========================================
build.xml: This is the main deploy process that manages all of the project to project dependencies,
and has a call-through ability to deploy any subproject.

caTissueInstall.properties: Before installing the application, all parameters required for the 
deployment process are expected to be defined by the user in this file.

Executing
========================================
To deploy: ant deploy_all
To upgrade: ant upgrade_all

Support (caBIG(tm) TBPT Knowledge Center)
========================================
Main Page: https://cabig-kc.nci.nih.gov/Biospecimen/KC/index.php/Main_Page
Discussion Forum: https://cabig-kc.nci.nih.gov/Biospecimen/forums/
Contact: tbpt_kc_support@mga.wustl.edu

Release Notes:
======================================
Link: http://gforge.nci.nih.gov/docman/index.php?group_id=689&selected_doc_group_id=6154&language_id=1

SVN Tags
========================================
https://ncisvn.nci.nih.gov/svn/catissue_persistent/catissuecore/tags/caTissue_v1.2_RC6_TAG

Thank you for using caTissue Suite.
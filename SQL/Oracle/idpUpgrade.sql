/** This table is to support WUSTL Key migration */
CREATE TABLE csm_migrate_user
(                                                                                       
 LOGIN_NAME varchar2(100 Byte) NOT NULL,                                                                        
 WUSTLKEY varchar2(100 Byte) default NULL UNIQUE,                                                                                 
 primary key (LOGIN_NAME)              
);
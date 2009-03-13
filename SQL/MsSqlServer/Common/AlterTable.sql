SET IDENTITY_INSERT catissue_user ON;
insert into catissue_user (IDENTIFIER, ACTIVITY_STATUS, CSM_USER_ID, EMAIL_ADDRESS,LOGIN_NAME,FIRST_NAME,LAST_NAME) values (1,'Active',1,'admin@admin.com','admin@admin.com','Admin','Admin');
SET IDENTITY_INSERT catissue_user OFF;
SET IDENTITY_INSERT catissue_password ON;
insert into catissue_password (IDENTIFIER,PASSWORD,UPDATE_DATE,USER_ID) values ( '1','6c416f576765696c6e63316f326d3365',NULL,'1');
SET IDENTITY_INSERT catissue_password OFF;

--commit;
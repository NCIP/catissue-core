insert into catissue_user (IDENTIFIER, ACTIVITY_STATUS, CSM_USER_ID, EMAIL_ADDRESS,LOGIN_NAME,FIRST_NAME,LAST_NAME) values (CATISSUE_USER_SEQ.nextval,'Active',1,'admin@admin.com','admin@admin.com','Admin','Admin');
insert into catissue_password (IDENTIFIER,PASSWORD,UPDATE_DATE,USER_ID) values ( CATISSUE_PASSWORD_SEQ.nextval,'6c416f576765696c6e63316f326d3365',NULL,'1');
ALTER TABLE CATISSUE_PART_MEDICAL_ID ADD constraint mrn_site_unique UNIQUE (MEDICAL_RECORD_NUMBER,SITE_ID);
commit;
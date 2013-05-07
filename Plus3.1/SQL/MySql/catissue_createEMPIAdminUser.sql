
insert into catissue_user (IDENTIFIER, ACTIVITY_STATUS, CSM_USER_ID, EMAIL_ADDRESS,LOGIN_NAME,FIRST_NAME,LAST_NAME,FIRST_TIME_LOGIN) values (CATISSUE_USER_SEQ.nextval,'Active',@@id@@
,'catissue_eMPI@admin.com','catissue_eMPI@admin.com','catissue_eMPIadmin','catissue_eMPIadmin',0);

insert into catissue_password (IDENTIFIER,PASSWORD,UPDATE_DATE,USER_ID) VALUES (CATISSUE_PASSWORD_SEQ.NEXTVAL,'xxits++sTge8j2uyHEABIQ==',sysdate,(select IDENTIFIER from catissue_user where LOGIN_NAME = 'catissue_eMPIadmin@admin.com'));

INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE) VALUES (CSM_USER_USER_ID_SEQ.NEXTVAL,'catissue_eMPI@admin.com','catissue_eMPIadmin','catissue_eMPIadmin',NULL,NULL,NULL,NULL,'xxits++sTge8j2uyHEABIQ==','catissue_eMPI@admin.com',date('2011-06-20'),NULL,to_date('2011-06-20'));
insert into csm_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)  values (CSM_USER_GROU_USER_GROUP_I_SEQ.NEXTVAL,(select USER_ID from CSM_USER where LOGIN_NAME = 'catissue_eMPI@admin.com'),
(select GROUP_ID from csm_group where APPLICATION_ID=(select application_id from csm_application where application_name='catissuecore') and group_name= 'ADMINISTRATOR_GROUP' ));


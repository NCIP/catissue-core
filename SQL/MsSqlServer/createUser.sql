SET IDENTITY_INSERT dbo.catissue_department ON
insert into catissue_department(IDENTIFIER,NAME) values(1,'@@first.admin.department@@');
SET IDENTITY_INSERT dbo.catissue_department OFF

SET IDENTITY_INSERT dbo.catissue_institution ON
insert into catissue_institution(IDENTIFIER,NAME) values(1,'@@first.admin.institution@@');
SET IDENTITY_INSERT dbo.catissue_institution OFF

SET IDENTITY_INSERT dbo.catissue_cancer_research_group ON
insert into catissue_cancer_research_group(IDENTIFIER,NAME)  values(1,'@@first.admin.cancerresearchgroup@@');
SET IDENTITY_INSERT dbo.catissue_cancer_research_group OFF

SET IDENTITY_INSERT dbo.catissue_address ON
insert into catissue_address (identifier,state,country,zipcode) values(1,null,null,null);
SET IDENTITY_INSERT dbo.catissue_address OFF

UPDATE CSM_USER SET LOGIN_NAME='@@first.admin.emailAddress@@',
					DEPARTMENT=1,
					EMAIL_ID='@@first.admin.emailAddress@@',
					PASSWORD='@@first.admin.encodedPassword@@'
				WHERE USER_ID=1;	

UPDATE catissue_user SET EMAIL_ADDRESS='@@first.admin.emailAddress@@',
							LOGIN_NAME='@@first.admin.emailAddress@@',
							DEPARTMENT_ID=1,
							INSTITUTION_ID=1,
							CANCER_RESEARCH_GROUP_ID=1,
							ADDRESS_ID=1
						WHERE 
							IDENTIFIER = 1;

							
UPDATE catissue_password set PASSWORD='@@first.admin.encodedPassword@@',
						UPDATE_DATE={fn NOW()}
						WHERE 
							IDENTIFIER = 1;

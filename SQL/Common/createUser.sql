insert into catissue_department (name) values('@@first.admin.department@@');
insert into catissue_institution (name) values('@@first.admin.institution@@');
insert into catissue_cancer_research_group (name) values('@@first.admin.cancerresearchgroup@@');
insert into catissue_address (state,country,zipcode) values(null,null,null);

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
							ADDRESS_ID=1,
							PASSWORD='@@first.admin.encodedPassword@@'
						WHERE 
							IDENTIFIER = 1;

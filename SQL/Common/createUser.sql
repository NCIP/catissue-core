insert into catissue_department values(1,'d');
insert into catissue_institution  values(1,'i');
insert into catissue_cancer_research_group  values(1,'c');
insert into catissue_address (identifier,state,country,zipcode) values(1,null,null,null);

UPDATE CSM_USER SET LOGIN_NAME='admin@admin.com',
					DEPARTMENT=1,
					EMAIL_ID='admin@admin.com',
					PASSWORD='4c416f576765696c6e63316f326d3365'
				WHERE USER_ID=1;	

UPDATE catissue_user SET EMAIL_ADDRESS='admin@admin.com',
							LOGIN_NAME='admin@admin.com',
							DEPARTMENT_ID=1,
							INSTITUTION_ID=1,
							CANCER_RESEARCH_GROUP_ID=1,
							ADDRESS_ID=1
						WHERE 
							IDENTIFIER = 1;

							
UPDATE catissue_password set PASSWORD='4c416f576765696c6e63316f326d3365',
						UPDATE_DATE=now()
						WHERE 
							IDENTIFIER = 1;
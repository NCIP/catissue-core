insert into catissue_department (name) values('Department');
insert into catissue_institution (name) values('Institution');
insert into catissue_cancer_research_group (name) values('CRG');
insert into catissue_address (state,country,zipcode) values(null,null,null);

UPDATE CSM_USER SET LOGIN_NAME='admin@admin.com',
					DEPARTMENT=1,
					EMAIL_ID='admin@admin.com',
					PASSWORD='6c416f576765696c6e63316f326d3365'
				WHERE USER_ID=1;	

UPDATE catissue_user SET EMAIL_ADDRESS='admin@admin.com',
							LOGIN_NAME='admin@admin.com',
							DEPARTMENT_ID=1,
							INSTITUTION_ID=1,
							CANCER_RESEARCH_GROUP_ID=1,
							ADDRESS_ID=1
						WHERE 
							IDENTIFIER = 1;

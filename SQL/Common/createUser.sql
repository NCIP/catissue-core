insert into catissue_department (name) values('MD Depart1');
insert into catissue_institution (name) values('MD Inst1');
insert into catissue_cancer_research_group (name) values('MD CRG1');
insert into catissue_address (state,country,zipcode) values(null,null,null);

UPDATE CSM_USER SET LOGIN_NAME='mandar_deshmukh@persistent.co.in',
					DEPARTMENT=1,
					EMAIL_ID='mandar_deshmukh@persistent.co.in',
					PASSWORD='6c416f576765696c6e63316f326d3365'
				WHERE USER_ID=1;	

UPDATE catissue_user SET EMAIL_ADDRESS='mandar_deshmukh@persistent.co.in',
							LOGIN_NAME='mandar_deshmukh@persistent.co.in',
							DEPARTMENT_ID=1,
							INSTITUTION_ID=1,
							CANCER_RESEARCH_GROUP_ID=1,
							ADDRESS_ID=1
						WHERE 
							IDENTIFIER = 1;

							
UPDATE catisue_password set PASSWORD='6c416f576765696c6e63316f326d3365'	
						WHERE 
							IDENTIFIER = 1;	
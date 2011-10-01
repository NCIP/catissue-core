catissue_user;select FIRST_NAME,LAST_NAME,DEPARTMENT_ID,CANCER_RESEARCH_GROUP_ID,INSTITUTION_ID,EMAIL_ADDRESS from catissue_user where EMAIL_ADDRESS='supervisor_1firstname@aaa.com'
catissue_site_users;select SITE_ID from catissue_site_users where USER_ID IN(select IDENTIFIER from catissue_user where EMAIL_ADDRESS='supervisor_1firstname@aaa.com')
catissue_address;select CITY,STATE,COUNTRY,ZIPCODE,PHONE_NUMBER from catissue_address where IDENTIFIER IN(select ADDRESS_ID from catissue_user where EMAIL_ADDRESS='supervisor_1firstname@aaa.com')
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='EMAIL_ADDRESS' 
catissue_audit_event;select EVENT_TYPE from catissue_audit_event



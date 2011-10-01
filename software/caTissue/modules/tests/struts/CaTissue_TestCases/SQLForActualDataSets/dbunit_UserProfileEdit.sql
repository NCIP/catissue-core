catissue_user;select FIRST_NAME,LAST_NAME from catissue_user where IDENTIFIER='1'
catissue_address;select CITY,STATE,COUNTRY,ZIPCODE,PHONE_NUMBER from catissue_address where IDENTIFIER IN(select ADDRESS_ID from catissue_user where IDENTIFIER='1')
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='FIRST_NAME' 
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 

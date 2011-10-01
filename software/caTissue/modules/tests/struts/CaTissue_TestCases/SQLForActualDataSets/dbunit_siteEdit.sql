catissue_site;select IDENTIFIER,NAME,TYPE,EMAIL_ADDRESS,ACTIVITY_STATUS,USER_ID from catissue_site where IDENTIFIER=181
catissue_address;select STREET,CITY,STATE,COUNTRY,ZIPCODE,PHONE_NUMBER,FAX_NUMBER from catissue_address where IDENTIFIER IN(select ADDRESS_ID from catissue_site where IDENTIFIER=181)
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 
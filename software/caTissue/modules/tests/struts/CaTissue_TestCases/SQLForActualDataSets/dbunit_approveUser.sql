catissue_user;select ACTIVITY_STATUS from catissue_user where IDENTIFIER='561'
catissue_site_users;select SITE_ID from catissue_site_users where USER_ID='561'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='ACTIVITY_STATUS' 
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 

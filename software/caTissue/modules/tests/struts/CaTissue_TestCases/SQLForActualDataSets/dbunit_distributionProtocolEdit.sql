catissue_specimen_protocol;select PRINCIPAL_INVESTIGATOR_ID,TITLE,SHORT_TITLE,IRB_IDENTIFIER,START_DATE,ENROLLMENT,DESCRIPTION_URL,ACTIVITY_STATUS from catissue_specimen_protocol where IDENTIFIER='361'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='TITLE'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 


catissue_participant;select LAST_NAME,FIRST_NAME,MIDDLE_NAME from catissue_participant where IDENTIFIER='562'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='RACE_NAME' 
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 

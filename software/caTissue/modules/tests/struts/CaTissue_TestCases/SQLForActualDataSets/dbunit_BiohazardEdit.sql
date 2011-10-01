catissue_biohazard;select NAME,COMMENTS from catissue_biohazard where IDENTIFIER=1
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 
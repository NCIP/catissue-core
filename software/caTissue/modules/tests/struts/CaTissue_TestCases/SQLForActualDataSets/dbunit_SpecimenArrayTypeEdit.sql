catissue_container_type;select NAME from catissue_container_type where IDENTIFIER='363'
catissue_specimen_array_type;select SPECIMEN_CLASS from catissue_specimen_array_type where IDENTIFIER='363'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 
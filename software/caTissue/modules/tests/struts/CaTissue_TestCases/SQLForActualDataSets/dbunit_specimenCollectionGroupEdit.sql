catissue_abs_speci_coll_group;select CLINICAL_DIAGNOSIS,CLINICAL_STATUS from catissue_abs_speci_coll_group where IDENTIFIER='581'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='CLINICAL_STATUS'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 



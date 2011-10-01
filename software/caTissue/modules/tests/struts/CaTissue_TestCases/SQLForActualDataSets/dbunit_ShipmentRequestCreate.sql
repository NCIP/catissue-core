catissue_base_shipment;select LABEL,SENDER_SITE_ID,ACTIVITY_STATUS from catissue_base_shipment where LABEL='Specimen_L2'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='LABEL' 
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog'
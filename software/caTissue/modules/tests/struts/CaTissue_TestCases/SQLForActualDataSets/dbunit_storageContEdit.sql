catissue_storage_container;select SITE_ID,TEMPERATURE from catissue_storage_container where IDENTIFIER='461'
catissue_site;select NAME from catissue_site where IDENTIFIER IN(select SITE_ID from catissue_storage_container where IDENTIFIER='461') 
catissue_container;select NAME,ACTIVITY_STATUS from catissue_container where IDENTIFIER='461'
catissue_capacity;select ONE_DIMENSION_CAPACITY,TWO_DIMENSION_CAPACITY from catissue_capacity where IDENTIFIER IN(select CAPACITY_ID from catissue_container where IDENTIFIER='461')
catissue_st_cont_st_type_rel;select STORAGE_TYPE_ID from catissue_st_cont_st_type_rel where STORAGE_CONTAINER_ID='461'
catissue_stor_cont_spec_class;select SPECIMEN_CLASS from catissue_stor_cont_spec_class where STORAGE_CONTAINER_ID='461'
catissue_stor_cont_spec_type;select SPECIMEN_TYPE from catissue_stor_cont_spec_type where STORAGE_CONTAINER_ID='461'
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event WHERE COMMENTS IS NULL OR COMMENTS NOT LIKE 'QueryLog' 
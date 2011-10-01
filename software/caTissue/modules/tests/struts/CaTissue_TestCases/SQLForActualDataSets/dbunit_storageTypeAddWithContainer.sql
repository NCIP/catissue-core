catissue_container_type;select NAME,ONE_DIMENSION_LABEL,TWO_DIMENSION_LABEL from catissue_container_type where NAME='freezer_racker'
catissue_storage_type;select DEFAULT_TEMPERATURE from catissue_storage_type where IDENTIFIER IN(select IDENTIFIER from catissue_container_type where NAME='freezer_racker')
catissue_capacity;select ONE_DIMENSION_CAPACITY,TWO_DIMENSION_CAPACITY from catissue_capacity where IDENTIFIER IN(select CAPACITY_ID from catissue_container_type where NAME='freezer_racker')
catissue_stor_type_spec_class;select SPECIMEN_CLASS from catissue_stor_type_spec_class where STORAGE_TYPE_ID IN(select IDENTIFIER from catissue_container_type where NAME='freezer_racker')
catissue_stor_type_spec_type;select SPECIMEN_TYPE from catissue_stor_type_spec_type where STORAGE_TYPE_ID IN(select IDENTIFIER from catissue_container_type where NAME='freezer_racker')
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME' and CURRENT_VALUE='freezer_racker'
catissue_audit_event;select EVENT_TYPE from catissue_audit_event
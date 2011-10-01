catissue_container_type;select NAME,ONE_DIMENSION_LABEL,TWO_DIMENSION_LABEL from catissue_container_type where Name='freezer rack box_1'
catissue_storage_type;select DEFAULT_TEMPERATURE from catissue_storage_type where IDENTIFIER IN(select IDENTIFIER from catissue_container_type where Name='freezer rack box_1')
catissue_capacity;select ONE_DIMENSION_CAPACITY,TWO_DIMENSION_CAPACITY from catissue_capacity where IDENTIFIER IN(select CAPACITY_ID from catissue_container_type where Name='freezer rack box_1')
catissue_stor_type_holds_type;select STORAGE_TYPE_ID from catissue_stor_type_holds_type where HOLDS_STORAGE_TYPE_ID IN(select IDENTIFIER from catissue_container_type where Name='freezer rack box_1')
catissue_stor_type_spec_class;select SPECIMEN_CLASS from catissue_stor_type_spec_class where STORAGE_TYPE_ID IN(select IDENTIFIER from catissue_container_type where Name='freezer rack box_1')
catissue_stor_type_spec_type;select SPECIMEN_TYPE from catissue_stor_type_spec_type where STORAGE_TYPE_ID IN(select IDENTIFIER from catissue_container_type where Name='freezer rack box_1')
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;select EVENT_TYPE from catissue_audit_event
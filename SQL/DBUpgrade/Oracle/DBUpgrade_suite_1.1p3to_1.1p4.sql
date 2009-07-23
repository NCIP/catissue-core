alter table catissue_audit_event_query_log ADD QUERY_ID number(20) default NULL; 
alter table catissue_audit_event_query_log add TEMP_TABLE_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add IF_TEMP_TABLE_DELETED number(1) default 0;
alter table catissue_audit_event_query_log add ROOT_ENTITY_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add COUNT_OF_ROOT_RECORDS number(20) default null;

alter table catissue_audit_event_query_log add column QUERY_ID bigint(20) default null; 
alter table catissue_audit_event_query_log add column TEMP_TABLE_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column IF_TEMP_TABLE_DELETED tinyint(1) default false;
alter table catissue_audit_event_query_log add column ROOT_ENTITY_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column COUNT_OF_ROOT_RECORDS bigint(20) default null;

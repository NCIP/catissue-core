declare v_exists integer;
begin
select count(*) into v_exists from dba_indexes where lower(index_name)='audit_index';
if v_exists = 0 then
execute immediate 'CREATE INDEX INDX_CAT_AUDIT_QUERY_AUDITID ON CATISSUE_AUDIT_EVENT_QUERY_LOG (AUDIT_EVENT_ID)';
end if;
end;
/
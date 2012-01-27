UPDATE dyextn_tagged_value SET t_value=CONCAT('gov.nih.nci.dynext.',t_value)
WHERE abstract_metadata_id IN (
SELECT identifier FROM dyextn_abstract_metadata WHERE identifier IN (
SELECT identifier FROM dyextn_entity_group WHERE is_system_generated = 0)
) AND t_key LIKE 'PackageName' AND t_value NOT LIKE '%.%';

update dyextn_entity_group set IS_SYSTEM_GENERATED = 1 where identifier in ( 
select container.entity_group_id from dyextn_container container  where container.entity_group_id is not null
and container.abstract_entity_id not in (select target_entity_id from 
dyextn_association ));

commit;
UPDATE dyextn_tagged_value SET t_value=CONCAT('edu.wustl.',t_value)
WHERE abstract_metadata_id IN (
SELECT identifier FROM dyextn_abstract_metadata WHERE identifier IN (
SELECT identifier FROM dyextn_entity_group WHERE is_system_generated = 0)
) AND t_key LIKE 'PackageName' AND t_value NOT LIKE '%.%';
UPDATE dyextn_tagged_value SET t_value=CONCAT('gov.nih.nci.dynext.',t_value)
WHERE abstract_metadata_id IN (
SELECT identifier FROM dyextn_abstract_metadata WHERE identifier IN (
SELECT identifier FROM dyextn_entity_group WHERE is_system_generated = 0)
) AND t_key LIKE 'PackageName' AND t_value NOT LIKE '%.%';


update dyextn_abstract_metadata meta4 join dyextn_entity ent 
on meta4.identifier = ent.identifier join dyextn_entity_group eg 
on eg.identifier = ent.entity_group_id join dyextn_abstract_metadata meta 
on meta.identifier = eg.identifier and meta.name like 'title'join dyextn_abstract_metadata meta2
on meta2.identifier = ent.identifier and meta2.name like 'title' set meta4.name ='TitleForm';

update  dyextn_container set caption='TitleForm' where abstract_entity_id  =(select ent.identifier from dyextn_entity ent join dyextn_entity_group eg on 
eg.identifier = ent.entity_group_id join dyextn_abstract_metadata meta on
meta.identifier = eg.identifier and meta.name like 'title'join dyextn_abstract_metadata meta2
on meta2.identifier = ent.identifier and meta2.name like 'title');

commit;
-- SQL's for importing SPP metadata into database starts
update dyextn_abstract_metadata set NAME = 'Catissue_Suite' where identifier = 1;

update dyextn_entity_group set SHORT_NAME = 'Catissue_Suite' where identifier = 1;

update dyextn_abstract_metadata set name = 'Specimen' where name = 'edu.wustl.catissuecore.domain.Specimen';

update dyextn_abstract_metadata set name = 'SpecimenRequirement' where name = 'edu.wustl.catissuecore.domain.SpecimenRequirement';

update dyextn_abstract_metadata set name = 'CollectionProtocolEvent' where name = 'edu.wustl.catissuecore.domain.CollectionProtocolEvent';

update dyextn_abstract_metadata set name = 'SpecimenCollectionGroup' where name = 'edu.wustl.catissuecore.domain.SpecimenCollectionGroup';
-- SQL's for importing SPP metadata into database ends

--INSERT INTO CATISSUE_QUERY_TABLE_DATA(TABLE_NAME,DISPLAY_NAME,ALIAS_NAME,PRIVILEGE_ID,FOR_SQI) VALUES ('catissue_spp','SPP','SPP',2,null)
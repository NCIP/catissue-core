insert into @@databasename@@.specimen_type
(id, name)
SELECT identifier ,value FROM CATISSUE_PERMISSIBLE_VALUE 
where parent_identifier in (1,2,3,4);


insert into @@databasename@@.collection_protocol
( id, name, patients_planned,  patients_enrolled,principanInv)
select p.identifier, p.title, enrollment, count(*),PRINCIPAL_INVESTIGATOR_ID
from catissue_specimen_protocol p,
catissue_coll_prot_reg r
where p.identifier = r.collection_protocol_id
GROUP BY p.identifier, p.title, enrollment, PRINCIPAL_INVESTIGATOR_ID;


insert into @@databasename@@.collection_protocol
(       id,
        name
)
select p.identifier, p.title from catissue_specimen_protocol p,catissue_distribution_protocol d
where p.identifier = d.identifier;
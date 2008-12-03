insert into @@databasename@@.specimen_type
(id, name)
SELECT identifier ,value FROM CATISSUE_PERMISSIBLE_VALUE 
where parent_identifier in (1,2,3,4);

insert into @@databasename@@.collection_protocol
(       id, name, patients_planned,  patients_enrolled,principanInv
)
select p.identifier, p.title, enrollment, count(*),PRINCIPAL_INVESTIGATOR_ID
from catissue_specimen_protocol p,
catissue_coll_prot_reg r
where p.identifier = r.collection_protocol_id
group by p.identifier;

insert into @@databasename@@.collection_protocol
(       id,
        name
)
select p.identifier, p.title from catissue_specimen_protocol p,catissue_distribution_protocol d
where p.identifier = d.identifier;


insert into @@databasename@@.specimen_inventory
(
        specimen_count,specimen_type_id, collection_protocol_id 
)
SELECT count(*),p.identifier, cpr.collection_protocol_id 
FROM catissue_specimen c, catissue_specimen_coll_group cc, catissue_coll_prot_reg cpr,
catissue_permissible_value p,catissue_abstract_specimen a
WHERE cc.IDENTIFIER=c.specimen_collection_group_id  AND cpr.IDENTIFIER=cc.collection_protocol_reg_id
and c.identifier = a.identifier and p.value = a.SPECIMEN_TYPE and parent_identifier in (1,2,3,4) 
group by SPECIMEN_TYPE, collection_protocol_id, p.identifier;


insert into @@databasename@@.number_distributed
(number_distributed,specimen_type_id,collection_protocol_id,report_period_id)
SELECT count(*) as number_distributed,st.id as specimen_type_id, dp.identifier as protocol_id,
concat(date_format(event_timestamp,'%Y'),ceiling(date_format(event_timestamp,'%c')/3)) as report_period_id
FROM CATISSUE_DISTRIBUTION d,catissue_specimen_protocol dp,catissue_abstract_specimen a,
CATISSUE_DISTRIBUTED_ITEM di,catissue_specimen s , @@databasename@@.specimen_type st WHERE d.distribution_protocol_id = dp.identifier
and di.distribution_id = d.identifier and s.identifier = di.specimen_id and a.SPECIMEN_TYPE= st.name and s.identifier = a.identifier group by st.id,report_period_id,dp.identifier;
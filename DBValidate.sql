/**This Script is for Validating data after Upgradation to suite*/

select count(*) from catissue_user;
select count(*) from catissue_institution;
select count(*) from catissue_cancer_research_group;
select count(*) from catissue_biohazard;
select count(*) from catissue_address;
select count(*) from catissue_department;

select count(*) from catissue_container;
select count(*) from catissue_container where parent_container_id is not null;

select count(*) from catissue_collection_protocol;
select count(*) from catissue_specimen_protocol;

select count(*) from catissue_specimen_protocol sp join catissue_collection_protocol cp
where sp.IDENTIFIER = cp.IDENTIFIER;

select count(*) from catissue_specimen_protocol sp join catissue_distribution_protocol dp
where sp.IDENTIFIER = dp.IDENTIFIER; 

select count(*) from catissue_participant;

select count(*) from catissue_coll_prot_reg;

select count(*) from catissue_coll_prot_reg where consent_witness is not null;


/*For Validating Specimen Data*/
select count(*) from catissue_specimen;
select count(*) from catissue_specimen where specimen_class like 'Tissue';
select count(*) from catissue_specimen where specimen_class like 'Cell';
select count(*) from catissue_specimen where specimen_class like 'Fluid';
select count(*) from catissue_specimen where specimen_class like 'Molecular';
select count(*) from catissue_specimen where COLLECTION_STATUS like 'Collected';
select count(*) from catissue_specimen where lineage like 'New';
select count(*) from catissue_specimen where lineage like 'Derived';
select count(*) from catissue_specimen where lineage like 'Aliquot';

/*For Validating Specimen Collection Group Data*/
select count(*) from catissue_specimen_coll_group;
select count(*) from catissue_specimen_coll_group where collection_status like 'Pending';
select count(*) from catissue_specimen_coll_group where collection_status like 'Complete';

select count(*) from catissue_speci_coll_req_group;

/*For Validating CSM Data*/
select count(*) from csm_user;
select count(*) from csm_protection_group;
select count(*) from csm_protection_element;
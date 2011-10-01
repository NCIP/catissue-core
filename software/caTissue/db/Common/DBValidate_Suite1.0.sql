/**This Script is for Validating data of CaTissueSuiteV1.0*/

/* Returns count of Users added*/ 
select count(*) from catissue_user;

/* Returns count of Institution*/ 
select count(*) from catissue_institution;

/* Returns count of Cancer Research Group*/ 
select count(*) from catissue_cancer_research_group;

/* Returns count of Biohazard*/ 
select count(*) from catissue_biohazard;

/* Returns count of Address*/ 
select count(*) from catissue_address;

/* Returns count of Department*/ 
select count(*) from catissue_department;

/* Returns count of Containers*/ 
select count(*) from catissue_container;

/* Returns count of Containers having no parent containers*/ 
select count(*) from catissue_container where parent_container_id is not null;

/* Returns count of Collection Protocol*/
select count(*) from catissue_collection_protocol;

/* Returns count of specimen requirement under Collection Protocol */ 
select count(*) from catissue_specimen sp where sp.IS_COLL_PROT_REQ=1;  

/* Returns count of Specimen Protocol */ 
select count(*) from catissue_specimen_protocol;

/* Returns count of Collection Protocol in Specimen Protocol*/ 
select count(*) from catissue_specimen_protocol sp join catissue_collection_protocol cp
where sp.IDENTIFIER = cp.IDENTIFIER;

/* Returns count of Distribution Protocol in Specimen Protocol */ 
select count(*) from catissue_specimen_protocol sp join catissue_distribution_protocol dp
where sp.IDENTIFIER = dp.IDENTIFIER; 

/* Returns count of Participant added*/ 
select count(*) from catissue_participant;

/* Returns count of Collection Protocol Registration*/ 
select count(*) from catissue_coll_prot_reg;

/*For Validating Specimen Data*/
select count(*) from catissue_specimen sp where sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where specimen_class like 'Tissue' and sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where specimen_class like 'Cell' and sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where specimen_class like 'Fluid' and sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where specimen_class like 'Molecular' and sp.IS_COLL_PROT_REQ=0;

select count(*) from catissue_specimen sp where lineage like 'New' and sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where lineage like 'Derived' and sp.IS_COLL_PROT_REQ=0;
select count(*) from catissue_specimen sp where lineage like 'Aliquot' and sp.IS_COLL_PROT_REQ=0;

/*For Validating Specimen Collection Group Data*/
select count(*) from catissue_specimen_coll_group;
/****** Inserts generic collection protocol required for caTIES   *******/
insert into catissue_specimen_protocol select max(identifier)+1,1,'@@COLL_PROT_TITLE@@','generic collection protocol','','2007-02-16',NULL,NULL,'','Active' from catissue_specimen_protocol;

insert into catissue_collection_protocol values((select identifier from catissue_specimen_protocol where title='@@COLL_PROT_TITLE@@'),'',0,0);

insert into catissue_coll_prot_event select max(identifier)+1,'New Diagnosis','TestGCP',1,(select identifier from catissue_specimen_protocol where title='@@COLL_PROT_TITLE@@') from catissue_coll_prot_event;

insert into catissue_quantity select max(identifier)+1,0 from catissue_quantity;

insert into catissue_specimen_requirement select max(identifier)+1,'Tissue','Fixed Tissue','Abdomen, NOS', 'Malignant',(select max(identifier) from catissue_quantity) from catissue_specimen_requirement;

insert catissue_coll_specimen_req values ((select max(identifier) from catissue_coll_prot_event) , (select max(identifier) from catissue_specimen_requirement));
/* Adding WUSTLKEY column in catissue_user table*/
ALTER TABLE CATISSUE_USER ADD COLUMN WUSTLKEY varchar(100) UNIQUE;

/**  Bug 13225 -  Clinical Diagnosis subset at CP definition level 
 */
drop table if exists CATISSUE_CLINICAL_DIAGNOSIS;
create table CATISSUE_CLINICAL_DIAGNOSIS (
   IDENTIFIER BIGINT not null auto_increment,
   CLINICAL_DIAGNOSIS varchar(255),
   COLLECTION_PROTOCOL_ID BIGINT,
   primary key (IDENTIFIER),
   CONSTRAINT FK_CD_COLPROT FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER)
);

/*Bulk Operations from UI*/
create table catissue_bulk_operation (
	IDENTIFIER BIGINT(20) not null auto_increment,
	OPERATION VARCHAR(100) not null,
	CSV_TEMPLATE VARCHAR(5000) not null,
	XML_TEMPALTE VARCHAR(15000) not null,
	DROPDOWN_NAME VARCHAR(100) not null,
	PRIMARY KEY  (`IDENTIFIER`), UNIQUE KEY OPERATION (`OPERATION`), UNIQUE KEY DROPDOWN_NAME (`DROPDOWN_NAME`)
);

/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_type_spec_type 
(                                                                       
  STORAGE_TYPE_ID bigint(20) NOT NULL,                                                                          
  SPECIMEN_TYPE varchar(50)
);     
alter table catissue_stor_type_spec_type add index STORAGE_TYPE_ID_FK (STORAGE_TYPE_ID), add constraint STORAGE_TYPE_ID_FK foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_cont_spec_type
(                                                                                 
  STORAGE_CONTAINER_ID bigint(20) NOT NULL,                                                                                  
  SPECIMEN_TYPE varchar(50) 
);      
alter table catissue_stor_cont_spec_type add index SPECIMEN_TYPE_ST_ID_FK (STORAGE_CONTAINER_ID), add constraint SPECIMEN_TYPE_ST_ID_FK foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);

CREATE INDEX INDX_STOR_TYPE_SPEC ON catissue_stor_type_spec_type(STORAGE_TYPE_ID);
CREATE INDEX INDX_STOR_CONT_SPEC ON catissue_stor_cont_spec_type (STORAGE_CONTAINER_ID);
/**
 * Populate catissue_stor_type_spec_type
 */
insert into catissue_stor_type_spec_type
select STORAGE_TYPE_ID, t.typevalue from catissue_stor_type_spec_class sp,(select p.value, c.value typevalue from catissue_permissible_value p,catissue_permissible_value c
where c.parent_identifier = p.identifier and p.value in ('Molecular','Cell','Fluid','Tissue')) t
where t.value = sp.SPECIMEN_CLASS;

/**
 * Populate catissue_stor_cont_spec_type
 */
insert into catissue_stor_cont_spec_type
select STORAGE_CONTAINER_ID, t.typevalue from catissue_stor_cont_spec_class sp,(select p.value, c.value typevalue from catissue_permissible_value p,catissue_permissible_value c
where c.parent_identifier = p.identifier and p.value in ('Molecular','Cell','Fluid','Tissue')) t
where t.value = sp.SPECIMEN_CLASS;
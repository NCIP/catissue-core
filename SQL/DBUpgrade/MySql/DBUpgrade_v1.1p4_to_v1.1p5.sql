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
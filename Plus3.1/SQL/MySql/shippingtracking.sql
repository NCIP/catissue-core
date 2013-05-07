SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;

drop table if exists CATISSUE_BASE_SHIPMENT;
drop table if exists CATISSUE_SHIPMENT;
drop table if exists CATISSUE_SHIPMENT_CONTAINR_REL;
drop table if exists CATISSUE_SHIPMENT_REQUEST;
drop table if exists CATISSUE_SPECI_SHIPMNT_REQ_REL;

SET FOREIGN_KEY_CHECKS=1;

create table CATISSUE_BASE_SHIPMENT (
    IDENTIFIER bigint not null auto_increment,
    LABEL varchar(50),
    SENDER_SITE_ID bigint,
    RECEIVER_SITE_ID bigint,
    SENDER_USER_ID bigint,
    RECEIVER_USER_ID bigint,
    SENDER_COMMENTS varchar(255),
    RECEIVER_COMMENTS varchar(255),
    CREATED_DATE date,
    SEND_DATE datetime,
    ACTIVITY_STATUS varchar(50),
    primary key (IDENTIFIER)
);

create table CATISSUE_SHIPMENT (
    IDENTIFIER bigint not null,
    SHIPMENT_REQUEST_ID bigint,
    BARCODE varchar(255),
    primary key (IDENTIFIER)
);

create table CATISSUE_SHIPMENT_CONTAINR_REL (
    BASE_SHIPMENT_ID bigint not null,
    CONTAINER_ID bigint not null,
    primary key (BASE_SHIPMENT_ID, CONTAINER_ID)
);

create table CATISSUE_SHIPMENT_REQUEST (
    IDENTIFIER bigint not null,
    primary key (IDENTIFIER)
);


create table CATISSUE_SPECI_SHIPMNT_REQ_REL (
    SHIPMENT_REQ_ID bigint not null,
    SPECIMEN_ID bigint not null,
    primary key (SHIPMENT_REQ_ID, SPECIMEN_ID)
);

alter table CATISSUE_BASE_SHIPMENT 
    add index FK84DE7ECCCAF26781 (RECEIVER_SITE_ID), 
    add constraint FK84DE7ECCCAF26781 
    foreign key (RECEIVER_SITE_ID) 
    references CATISSUE_SITE (IDENTIFIER);

alter table CATISSUE_BASE_SHIPMENT 
    add index FK84DE7ECC4502E901 (RECEIVER_USER_ID), 
    add constraint FK84DE7ECC4502E901 
    foreign key (RECEIVER_USER_ID) 
    references CATISSUE_USER (IDENTIFIER);

alter table CATISSUE_BASE_SHIPMENT 
    add index FK84DE7ECC2ABD7347 (SENDER_USER_ID), 
    add constraint FK84DE7ECC2ABD7347 
    foreign key (SENDER_USER_ID) 
    references CATISSUE_USER (IDENTIFIER);

alter table CATISSUE_BASE_SHIPMENT 
    add index FK84DE7ECCB0ACF1C7 (SENDER_SITE_ID), 
    add constraint FK84DE7ECCB0ACF1C7 
    foreign key (SENDER_SITE_ID) 
    references CATISSUE_SITE (IDENTIFIER);

alter table CATISSUE_SHIPMENT 
    add index FK76CEEF96C30B7BC6 (IDENTIFIER), 
    add constraint FK76CEEF96C30B7BC6 
    foreign key (IDENTIFIER) 
    references CATISSUE_BASE_SHIPMENT (IDENTIFIER);

alter table CATISSUE_SHIPMENT_CONTAINR_REL 
    add index FKF56D72B29088E48F (BASE_SHIPMENT_ID), 
    add constraint FKF56D72B29088E48F 
    foreign key (BASE_SHIPMENT_ID) 
    references CATISSUE_BASE_SHIPMENT (IDENTIFIER);

alter table CATISSUE_SHIPMENT_CONTAINR_REL 
    add index FKF56D72B2D72D70D6 (CONTAINER_ID), 
    add constraint FKF56D72B2D72D70D6 
    foreign key (CONTAINER_ID) 
    references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);

alter table CATISSUE_SHIPMENT_REQUEST 
    add index FK85331EC6C30B7BC6 (IDENTIFIER), 
    add constraint FK85331EC6C30B7BC6 
    foreign key (IDENTIFIER) 
    references CATISSUE_BASE_SHIPMENT (IDENTIFIER);
    
alter table CATISSUE_SPECI_SHIPMNT_REQ_REL 
    add index FKF56D72B29088E48G (SHIPMENT_REQ_ID), 
    add constraint FKF56D72B29088E48G 
    foreign key (SHIPMENT_REQ_ID) 
    references CATISSUE_BASE_SHIPMENT (IDENTIFIER);

alter table CATISSUE_SPECI_SHIPMNT_REQ_REL 
    add index FKF56D72B2D72D70DH (SPECIMEN_ID), 
    add constraint FKF56D72B2D72D70DH 
    foreign key (SPECIMEN_ID) 
    references CATISSUE_SPECIMEN (IDENTIFIER);
    
alter table CATISSUE_SHIPMENT
    add index FKF56D72B2D72D70IJ (SHIPMENT_REQUEST_ID), 
    add constraint FKF56D72B2D72D70IJ 
    foreign key (SHIPMENT_REQUEST_ID) 
    references CATISSUE_SHIPMENT_REQUEST (IDENTIFIER);

insert into catissue_site 
	(NAME,TYPE,EMAIL_ADDRESS,USER_ID,ACTIVITY_STATUS,ADDRESS_ID) values 
	('In Transit','Repository','admin@admin.com',1,'Active',1);

insert into catissue_capacity 
	(ONE_DIMENSION_CAPACITY,TWO_DIMENSION_CAPACITY) 
	values (100,1);

insert into catissue_container_type 
	(CAPACITY_ID,NAME,ONE_DIMENSION_LABEL,TWO_DIMENSION_LABEL,COMMENTS,ACTIVITY_STATUS) 
	values ((select Max(IDENTIFIER) from catissue_capacity),'Shipment Container','X','Y',NULL,'Active');

insert into catissue_storage_type 
	(IDENTIFIER,DEFAULT_TEMPERATURE) values 
	((select Max(IDENTIFIER) from catissue_container_type),-80);

insert into catissue_stor_type_spec_class 
	(STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
	((select Max(IDENTIFIER) from catissue_storage_type),'Fluid');

insert into catissue_stor_type_spec_class 
	(STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
	((select Max(IDENTIFIER) from catissue_storage_type),'Tissue');

insert into catissue_stor_type_spec_class 
	(STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
	((select Max(IDENTIFIER) from catissue_storage_type),'Molecular');

insert into catissue_stor_type_spec_class 
	(STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
	((select Max(IDENTIFIER) from catissue_storage_type),'Cell');

insert into catissue_stor_type_holds_type 
	(STORAGE_TYPE_ID,HOLDS_STORAGE_TYPE_ID) 
	values (1,(select Max(IDENTIFIER) from catissue_storage_type));

insert into CATISSUE_QUERY_TABLE_DATA  
	(TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values 
	('CATISSUE_SHIPMENT', 'Shipment', 'Shipment', 2);

insert into CATISSUE_QUERY_TABLE_DATA  
	(TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values 
	('CATISSUE_SHIPMENT_REQUEST', 'Shipment Request', 'ShipmentRequest', 2);
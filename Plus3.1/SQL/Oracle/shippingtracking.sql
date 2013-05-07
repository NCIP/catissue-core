create table CATISSUE_BASE_SHIPMENT (
    IDENTIFIER number(19,0) not null,
    LABEL varchar(50),
    SENDER_SITE_ID number(19,0),
    RECEIVER_SITE_ID number(19,0),
    SENDER_USER_ID number(19,0),
    RECEIVER_USER_ID number(19,0),
    SENDER_COMMENTS varchar(255),
    RECEIVER_COMMENTS varchar(255),
    CREATED_DATE date,
    SEND_DATE date,
    ACTIVITY_STATUS varchar(50),
    primary key (IDENTIFIER)
);

create sequence CATISSUE_BASE_SHIPMENT_SEQ;

create table CATISSUE_SHIPMENT (
    IDENTIFIER number(19,0) not null,
    SHIPMENT_REQUEST_ID number(19,0),
    BARCODE varchar(255),
    primary key (IDENTIFIER)
);

create table CATISSUE_SHIPMENT_CONTAINR_REL (
    BASE_SHIPMENT_ID number(19,0) not null,
    CONTAINER_ID number(19,0) not null,
    primary key (BASE_SHIPMENT_ID, CONTAINER_ID)
);

create table CATISSUE_SHIPMENT_REQUEST (
    IDENTIFIER number(19,0) not null,
    primary key (IDENTIFIER)
);

create table CATISSUE_SPECI_SHIPMNT_REQ_REL (
    SHIPMENT_REQ_ID number(19,0) not null,
    SPECIMEN_ID number(19,0) not null,
    primary key (SHIPMENT_REQ_ID, SPECIMEN_ID)
);


alter table CATISSUE_BASE_SHIPMENT 
  add constraint FK84DE7ECCCAF26781 
  foreign key (RECEIVER_SITE_ID) 
  references CATISSUE_SITE ;

alter table CATISSUE_BASE_SHIPMENT 
  add constraint FK84DE7ECC4502E901 
  foreign key (RECEIVER_USER_ID) 
  references CATISSUE_USER ;

alter table CATISSUE_BASE_SHIPMENT 
  add constraint FK84DE7ECCB0ACF1C7 
  foreign key (SENDER_SITE_ID) 
  references CATISSUE_SITE ;

alter table CATISSUE_BASE_SHIPMENT 
  add constraint FK84DE7ECC2ABD7347 
  foreign key (SENDER_USER_ID) 
  references CATISSUE_USER ;

alter table CATISSUE_SHIPMENT 
  add constraint FK76CEEF96C30B7BC6 
  foreign key (IDENTIFIER) 
  references CATISSUE_BASE_SHIPMENT ;

alter table CATISSUE_SHIPMENT_CONTAINR_REL 
  add constraint FKF56D72B29088E48F 
  foreign key (BASE_SHIPMENT_ID) 
  references CATISSUE_BASE_SHIPMENT ;

alter table CATISSUE_SHIPMENT_CONTAINR_REL 
  add constraint FKF56D72B2D72D70D6 
  foreign key (CONTAINER_ID) 
  references CATISSUE_STORAGE_CONTAINER ;

alter table CATISSUE_SHIPMENT_REQUEST 
  add constraint FK85331EC6C30B7BC6 
  foreign key (IDENTIFIER) 
  references CATISSUE_BASE_SHIPMENT ;

alter table CATISSUE_SPECI_SHIPMNT_REQ_REL 
  add constraint FKF56D72B29088E48G 
  foreign key (SHIPMENT_REQ_ID) 
  references CATISSUE_BASE_SHIPMENT ;

alter table CATISSUE_SPECI_SHIPMNT_REQ_REL 
  add constraint FKF56D72B2D72D70DH 
  foreign key (SPECIMEN_ID) 
  references CATISSUE_SPECIMEN ;
  
alter table CATISSUE_SHIPMENT
    add constraint FKF56D72B2D72D70IJ 
    foreign key (SHIPMENT_REQUEST_ID) 
    references CATISSUE_SHIPMENT_REQUEST ;

insert into catissue_site 
  (IDENTIFIER,NAME,TYPE,EMAIL_ADDRESS,USER_ID,ACTIVITY_STATUS) values 
  (CATISSUE_SITE_SEQ.NEXTVAL,'In Transit','Repository','admin@admin.com',1,'Active');

insert into catissue_capacity 
  (IDENTIFIER,ONE_DIMENSION_CAPACITY,TWO_DIMENSION_CAPACITY) values 
  (CATISSUE_CAPACITY_SEQ.NEXTVAL,100,1);

insert into catissue_container_type 
  (IDENTIFIER,CAPACITY_ID,NAME,ONE_DIMENSION_LABEL,TWO_DIMENSION_LABEL,COMMENTS,ACTIVITY_STATUS) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.NEXTVAL,CATISSUE_CAPACITY_SEQ.CURRVAL,'Shipment Container','X','Y',NULL,'Active');

insert into catissue_storage_type 
  (IDENTIFIER,DEFAULT_TEMPERATURE) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL,-80);

insert into catissue_stor_type_spec_class 
  (STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL,'Fluid');

insert into catissue_stor_type_spec_class 
  (STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL,'Tissue');

insert into catissue_stor_type_spec_class 
  (STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL,'Molecular');

insert into catissue_stor_type_spec_class 
  (STORAGE_TYPE_ID,SPECIMEN_CLASS) values 
  (CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL,'Cell');

insert into catissue_stor_type_holds_type 
  (STORAGE_TYPE_ID,HOLDS_STORAGE_TYPE_ID) 
  values (1,CATISSUE_CONTAINER_TYPE_SEQ.CURRVAL);

insert into CATISSUE_QUERY_TABLE_DATA  
  (TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values 
  ((select Max(TABLE_ID)+1 from CATISSUE_QUERY_TABLE_DATA), 'CATISSUE_SHIPMENT', 'Shipment', 'Shipment', 2);

insert into CATISSUE_QUERY_TABLE_DATA  
  (TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME, PRIVILEGE_ID) values 
  ((select Max(TABLE_ID)+1 from CATISSUE_QUERY_TABLE_DATA), 'CATISSUE_SHIPMENT_REQUEST', 'Shipment Request', 'ShipmentRequest', 2);

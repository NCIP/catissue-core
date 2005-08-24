insert into CATISSUE_INSTITUTION VALUES (1,'Washington University');

insert into CATISSUE_DEPARTMENT VALUES (1,'Cardiology');
insert into CATISSUE_DEPARTMENT VALUES (2,'Pathology');

insert into CATISSUE_CANCER_RESEARCH_GROUP VALUES (1,'Siteman Cancer Center');
insert into CATISSUE_CANCER_RESEARCH_GROUP VALUES (2,'Washington University');

insert into CATISSUE_ADDRESS values (1,'abc','abc','asd','abc','abc','asdas','asdas');
insert into CATISSUE_SITE VALUES (1,'SITE1',"LAB","as@b.cn",47,'Active',1);
insert into CATISSUE_STORAGE_container_capacity VALUES (1,5,5,'abc','abc');
insert into CATISSUE_STORAGE_TYPE VALUES (1,'Box',50,1);
insert into CATISSUE_STORAGE_container VALUES (1,'name1',50,1,'abc','Active',1,1,null,1,0,0);
insert into CATISSUE_STORAGE_CONTAINER values(2,'name2',50,false,'acb','Active',1,null,1,1,0,1)

CREATE TABLE CATISSUE_QUERY_INTERFACE_TABLE_DATA
(
	  TABLE_ID bigint not null auto_increment, 
	  
      TABLE_NAME varchar(50),

      DISPLAY_NAME varchar(50),
      
      primary key (TABLE_ID)
);

CREATE TABLE CATISSUE_QUERY_INTERFACE_COLUMN_DATA
(
	  IDENTIFIER bigint not null auto_increment,

      TABLE_ID bigint not null,

      COLUMN_NAME varchar(50),

      DISPLAY_NAME varchar(50),
      
	  primary key (IDENTIFIER)
);

commit

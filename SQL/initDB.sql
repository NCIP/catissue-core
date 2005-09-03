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
	
      ALIAS_NAME varchar(50) 	
      
      primary key (TABLE_ID)
);


CREATE TABLE CATISSUE_QUERY_INTERFACE_COLUMN_DATA
(
	  IDENTIFIER bigint not null auto_increment,

      TABLE_ID bigint not null,

      COLUMN_NAME varchar(50),

      DISPLAY_NAME varchar(50),
      
      ATTRIBUTE_TYPE varchar(30),
      
	  primary key (IDENTIFIER)
);

CREATE TABLE CATISSUE_TABLE_RELATIONS
(
      PARENT_TABLE_ID bigint,      
      
      CHILD_TABLE_ID bigint
);


CREATE TABLE CATISSUE_CLASS_DATA
(
      IDENTIFIER bigint not null auto_increment, 
	  
      CLASS_NAME varchar(50),

      DISPLAY_NAME varchar(50),
      
      primary key (IDENTIFIER)
);

CREATE TABLE CATISSUE_ATTRIBUTE_DATA
(
      IDENTIFIER bigint not null auto_increment,

      CLASS_ID bigint not null,

      ATTRIBUTE_NAME varchar(50),

      DISPLAY_NAME varchar(50),
      
      primary key (IDENTIFIER)
);

CREATE TABLE CATISSUE_CLASS_RELATIONS
(
      PARENT_CLASS_ID bigint not null,      
      
      CHILD_CLASS_ID bigint not null
);

commit

insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('User','User');
insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('Department','Department');
insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('Institution','Institution');
insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('Address','Address');
insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('CancerResearchGroup','Cancer Research Group');
insert into CATISSUE_CLASS_DATA (CLASS_NAME,DISPLAY_NAME) values ('User','CSM User');


insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (1,'activityStatus','Activity Status');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (1,'comments','Comments');

insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (2,'name','Name');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (3,'name','Name');

insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'street','Street');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'city','City');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'state','State');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'country','Country');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'zipCode','Zip Code');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'phoneNumber','Phone Number');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (4,'faxNumber','Fax Number');

insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (5,'name','Name');

insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (6,'loginName','Login Name');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (6,'lastName','Last Name');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (6,'firstName','First Name');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (6,'emailId','Email Address');
insert into CATISSUE_ATTRIBUTE_DATA (CLASS_ID,ATTRIBUTE_NAME,DISPLAY_NAME) values (6,'startDate','Start Date');



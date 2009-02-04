drop table CATISSUE_QUERY_TABLE_DATA cascade constraints;

CREATE TABLE CATISSUE_QUERY_TABLE_DATA
(
      TABLE_ID number not null, 
      TABLE_NAME varchar(50),
      DISPLAY_NAME varchar(50),
      ALIAS_NAME varchar(50),
      PRIVILEGE_ID number(1,0),
      FOR_SQI number(1,0),                                                                        
      primary key (TABLE_ID)
);
drop table CATISSUE_INTERFACE_COLUMN_DATA cascade constraints;
CREATE TABLE CATISSUE_INTERFACE_COLUMN_DATA
(
      IDENTIFIER number(30) not null,
      TABLE_ID number(30) not null,
      COLUMN_NAME varchar(50),
      ATTRIBUTE_TYPE varchar(30),
      primary key (IDENTIFIER)
);

drop TABLE CATISSUE_TABLE_RELATION;
CREATE TABLE CATISSUE_TABLE_RELATION
(
      RELATIONSHIP_ID number(30),
      PARENT_TABLE_ID number(30),
      CHILD_TABLE_ID number(30),
      TABLES_IN_PATH varchar(50),
      primary key (RELATIONSHIP_ID)
);

drop table CATISSUE_SEARCH_DISPLAY_DATA;
CREATE TABLE CATISSUE_SEARCH_DISPLAY_DATA
(
      RELATIONSHIP_ID number(30) not null,
      COL_ID number(30) not null,
      DISPLAY_NAME varchar(50),
      DEFAULT_VIEW_ATTRIBUTE number(1,0)
);
drop table CATISSUE_RELATED_TABLES_MAP;
CREATE TABLE CATISSUE_RELATED_TABLES_MAP
(
      FIRST_TABLE_ID number(30),
      SECOND_TABLE_ID number(30),
      FIRST_TABLE_JOIN_COLUMN varchar(50),
      SECOND_TABLE_JOIN_COLUMN varchar(50)
);
alter table CATISSUE_RELATED_TABLES_MAP add constraint RELATED_TABLES_KEY unique (FIRST_TABLE_ID,SECOND_TABLE_ID);
alter table CATISSUE_SEARCH_DISPLAY_DATA add constraint SEARCH_DATA_KEY unique (RELATIONSHIP_ID,COL_ID);

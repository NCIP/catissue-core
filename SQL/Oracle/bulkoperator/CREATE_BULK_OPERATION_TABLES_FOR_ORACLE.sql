/*L
   Copyright Washington University in St. Louis
   Copyright SemanticBits
   Copyright Persistent Systems
   Copyright Krishagni

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
L*/

CREATE SEQUENCE CATISSUE_BULK_OPERATION_SEQ NOCACHE;
CREATE SEQUENCE JOB_DETAILS_SEQ NOCACHE;

create table catissue_bulk_operation
(
	IDENTIFIER number(19,0) not null,
	OPERATION VARCHAR(255) not null unique,
	CSV_TEMPLATE CLOB not null,
	XML_TEMPALTE CLOB not null,
	DROPDOWN_NAME VARCHAR(100) not null unique,
	CONSTRAINT CATISSUE_BULK_OPERATION_SEQ PRIMARY KEY (IDENTIFIER)
);
CREATE TABLE JOB_DETAILS (
  IDENTIFIER NUMBER(38) NOT NULL,
  JOB_NAME varchar(255) NOT NULL,
  JOB_STATUS varchar(50) default NULL,
  TOTAL_RECORDS_COUNT NUMBER(20) default NULL,
  FAILED_RECORDS_COUNT NUMBER(20) default NULL,
  TIME_TAKEN NUMBER(20) default NULL,
  LOG_FILE blob,
  JOB_STARTED_BY NUMBER(20) default NULL,
  START_TIME date default NULL,
  CURRENT_RECORDS_PROCESSED NUMBER(20) default NULL,
  LOG_FILE_NAME varchar(1024) default NULL,
  CONSTRAINT JOB_DETAILS_SEQ PRIMARY KEY  (IDENTIFIER)
);
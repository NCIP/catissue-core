SET storage_engine=InnoDB;
/
-- To add unique constraint for Bugid:19485
--alter table csm_migrate_user add unique index(TARGET_IDP_NAME, MIGRATED_LOGIN_NAME);
/

-- This is required for model change
DROP PROCEDURE IF EXISTS caCORE_UpgradeProc;
/

CREATE PROCEDURE caCORE_UpgradeProc()
BEGIN
	DECLARE total_sale INT DEFAULT 0;
	DECLARE loopcondition BOOLEAN DEFAULT 0;
	DECLARE scgId bigint(20);
	DECLARE isprId bigint(20);
	DECLARE dsprId bigint(20);

	-- Declare cursor for the join of tables 'catissue_identified_report' and 'catissue_specimen_coll_group'.
	DECLARE ispr CURSOR FOR
		SELECT scg_id, ispr.IDENTIFIER
		FROM catissue_identified_report as ispr, catissue_specimen_coll_group as scg
		WHERE scg.IDENTIFIER=ispr.scg_id;

	-- Declare cursor for the join of tables 'catissue_deidentified_report' and 'catissue_specimen_coll_group'
	DECLARE dspr CURSOR FOR
		SELECT scg_id, dspr.IDENTIFIER
		FROM catissue_deidentified_report as dspr, catissue_specimen_coll_group as scg
		WHERE scg.IDENTIFIER=dspr.scg_id;

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET loopcondition = 1;

	-- Alter tables to add the appropriate columns and the constraints.
	ALTER TABLE catissue_pathology_report
	ADD SCG_ID bigint(20),
	ADD CONSTRAINT fk_scgid FOREIGN KEY (SCG_ID)REFERENCES catissue_specimen_coll_group(IDENTIFIER);

	ALTER TABLE catissue_specimen_coll_group
	ADD DSPR_ID bigint(20),ADD ISPR_ID bigint(20),
	ADD CONSTRAINT fk_dspr_id FOREIGN KEY (DSPR_ID)REFERENCES catissue_deidentified_report(IDENTIFIER),
	ADD CONSTRAINT fk_ispr_id FOREIGN KEY (ISPR_ID)REFERENCES catissue_identified_report(IDENTIFIER);

	-- Add the appropriate data to respective tables.
	OPEN ispr;
	FETCH ispr into scgId, isprId;
	REPEAT
		UPDATE catissue_pathology_report
		SET SCG_ID = scgId
		WHERE IDENTIFIER = isprId;

		UPDATE catissue_specimen_coll_group
		SET ISPR_ID = isprId
		WHERE IDENTIFIER = scgId;

		FETCH ispr into scgId, isprId;
	UNTIL loopcondition END REPEAT;
	CLOSE ispr;
	SET loopcondition = 0;

	-- Add the appropriate data to respective tables.
	OPEN dspr;
	FETCH dspr into scgId, dsprId;
	REPEAT
		UPDATE catissue_pathology_report
		SET SCG_ID = scgId
		WHERE IDENTIFIER = dsprId;

		UPDATE catissue_specimen_coll_group
		SET DSPR_ID = dsprId
		WHERE IDENTIFIER = scgId;

		FETCH dspr into scgId, dsprId;
	UNTIL loopcondition END REPEAT;
	CLOSE dspr;

	COMMIT;

	-- The following code drops the columns that are no longer required.
	-- Since there are foreign key constraints on the columns they could not be dropped.
	-- set foreign_key_checks=0;
	-- ALTER TABLE catissue_identified_report DROP SCG_ID;
	-- ALTER TABLE catissue_deidentified_report DROP SCG_ID;
	-- set foreign_key_checks=1;
END;
/

CALL caCORE_UpgradeProc();
/
DROP PROCEDURE caCORE_UpgradeProc;
/

-- These SQL's are for creating SPP related tables and coresponding changes in the model for SPP
create table catissue_spp (IDENTIFIER bigint auto_increment, NAME varchar(50) unique, BARCODE varchar(50) unique,spp_template_xml blob,  primary key (IDENTIFIER));
/

create table catissue_abstract_application (IDENTIFIER bigint NOT NULL AUTO_INCREMENT, REASON_DEVIATION text, TIMESTAMP timestamp, USER_DETAILS bigint, COMMENTS text, primary key (IDENTIFIER), foreign key (USER_DETAILS) references catissue_user (IDENTIFIER));
/

create table catissue_default_action (IDENTIFIER bigint(20) NOT NULL, PRIMARY KEY (IDENTIFIER));
/

create table catissue_spp_application (IDENTIFIER bigint, SPP_IDENTIFIER bigint, SCG_IDENTIFIER bigint, primary key (IDENTIFIER), foreign key (IDENTIFIER) references  catissue_abstract_application (IDENTIFIER) ,foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER), foreign key (SCG_IDENTIFIER) references catissue_specimen_coll_group (IDENTIFIER));
/

create table catissue_action_application (IDENTIFIER bigint, SPP_APP_IDENTIFIER bigint, SPECIMEN_ID bigint, SCG_ID bigint, primary key (IDENTIFIER), foreign key (IDENTIFIER) references catissue_abstract_application (IDENTIFIER), foreign key (SPP_APP_IDENTIFIER) references catissue_spp_application (IDENTIFIER), foreign key (SPECIMEN_ID) references catissue_specimen (IDENTIFIER), foreign key (SCG_ID) references catissue_specimen_coll_group (IDENTIFIER));
/

create table catissue_action_app_rcd_entry (IDENTIFIER bigint, ACTION_APP_ID bigint, primary key (IDENTIFIER), foreign key (ACTION_APP_ID) references catissue_action_application (IDENTIFIER));
/

create table catissue_action (IDENTIFIER bigint, BARCODE varchar(50), ACTION_ORDER bigint, ACTION_APP_RECORD_ENTRY_ID bigint, SPP_IDENTIFIER bigint, UNIQUE_ID varchar(50) not null, IS_SKIPPED bit default 0, primary key (IDENTIFIER), foreign key (ACTION_APP_RECORD_ENTRY_ID) references catissue_action_app_rcd_entry (IDENTIFIER), foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER));
/
ALTER TABLE catissue_action ADD CONSTRAINT spp_unique_id UNIQUE (SPP_IDENTIFIER,UNIQUE_ID);
/
create table catissue_cpe_spp (cpe_identifier bigint(20),spp_identifier bigint(20),CONSTRAINT catissue_cpe_spp_1 FOREIGN KEY (cpe_identifier) REFERENCES catissue_coll_prot_event (IDENTIFIER),CONSTRAINT catissue_cpe_spp_2 FOREIGN KEY (spp_identifier) REFERENCES catissue_spp (IDENTIFIER));
/
alter table catissue_action_application add (ACTION_IDENTIFIER bigint, ACTION_APP_RECORD_ENTRY_ID bigint, foreign key (ACTION_IDENTIFIER) references catissue_action (IDENTIFIER), foreign key (ACTION_APP_RECORD_ENTRY_ID) references catissue_action_app_rcd_entry (IDENTIFIER));
/
SET foreign_key_checks = 0;
/

alter table catissue_cp_req_specimen add (SPP_IDENTIFIER bigint, ACTION_IDENTIFIER bigint, foreign key (SPP_IDENTIFIER) references catissue_spp (IDENTIFIER), foreign key (ACTION_IDENTIFIER) references catissue_action (IDENTIFIER));
/

alter table catissue_specimen add (SPP_APPLICATION_ID bigint, ACTION_APPLICATION_ID bigint, foreign key (SPP_APPLICATION_ID) references catissue_spp_application (IDENTIFIER), foreign key (ACTION_APPLICATION_ID) references catissue_action_application (IDENTIFIER));
/

SET foreign_key_checks = 1;
/

-- SQL's for SPP tables creation end

--SQL's for inserting SPP metadata for simple search
INSERT INTO CATISSUE_QUERY_TABLE_DATA  select max(TABLE_ID)+1, 'catissue_spp', 'Specimen Processing Procedure', 'SpecimenProcessingProcedure',2,1 FROM CATISSUE_QUERY_TABLE_DATA;
/
INSERT INTO CATISSUE_TABLE_RELATION select max(RELATIONSHIP_ID)+1,(SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA),(SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), NULL FROM CATISSUE_TABLE_RELATION;
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA select max(IDENTIFIER)+1, (SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), 'IDENTIFIER', 'bigint' FROM CATISSUE_INTERFACE_COLUMN_DATA;
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ( (select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'Identifier',1,1);
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA select max(IDENTIFIER)+1, (SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), 'NAME', 'varchar' FROM CATISSUE_INTERFACE_COLUMN_DATA;
/
INSERT INTO CATISSUE_QUERY_EDITLINK_COLS VALUES((SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA));
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'SPP Name',1,2);
/
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA select max(IDENTIFIER)+1, (SELECT max(TABLE_ID) FROM CATISSUE_QUERY_TABLE_DATA), 'BARCODE', 'varchar' FROM CATISSUE_INTERFACE_COLUMN_DATA;
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select max(RELATIONSHIP_ID) FROM CATISSUE_TABLE_RELATION), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'Barcode',1,3);
/
-- SQLs for Grid Grouper integration
create table CATISSUE_CP_GRID_PRVG (
   IDENTIFIER BIGINT not null auto_increment,
   GROUP_NAME varchar(255),
   STEM_NAME varchar(255),
   PRIVILEGES_STRING varchar(255),
   STATUS varchar(255),
   COLLECTION_PROTOCOL_ID BIGINT,
   primary key (IDENTIFIER),
   CONSTRAINT FK_GRID_GRP_COLPROT FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER)
);
/
-- SQLs for Grid Grouper integration end
-- SQLs to make GSID querieable
INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA select max(IDENTIFIER)+1, (SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME = 'CATISSUE_SPECIMEN'), 'GLOBAL_SPECIMEN_IDENTIFIER', 'varchar' FROM CATISSUE_INTERFACE_COLUMN_DATA;
/
INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA (RELATIONSHIP_ID, COL_ID, DISPLAY_NAME, DEFAULT_VIEW_ATTRIBUTE, ATTRIBUTE_ORDER) VALUES ((select RELATIONSHIP_ID FROM CATISSUE_TABLE_RELATION WHERE PARENT_TABLE_ID = (SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME = 'CATISSUE_SPECIMEN') AND CHILD_TABLE_ID = (SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME = 'CATISSUE_SPECIMEN')), (SELECT max(IDENTIFIER) FROM CATISSUE_INTERFACE_COLUMN_DATA), 'GSID',1,3);
/
-- SQLs to make GSID querieable ends
COMMIT;
/
-- Script for Age at Collection
ALTER TABLE catissue_specimen_coll_group ADD Column AGE_AT_COLLECTION integer;
/
COMMIT;
/
	-- SQL Script for Copy the Encounter TimeStamp Data.
	UPDATE catissue_specimen_coll_group scg
		JOIN catissue_specimen_event_param sep ON sep.SPECIMEN_COLL_GRP_ID=scg.identifier
		JOIN catissue_coll_event_param cep ON sep.identifier = cep.identifier
		JOIN catissue_coll_prot_reg cpr ON scg.COLLECTION_PROTOCOL_REG_ID=cpr.identifier
		JOIN catissue_participant part ON part.identifier=cpr.PARTICIPANT_ID
	Set scg.ENCOUNTER_TIMESTAMP=sep.EVENT_TIMESTAMP,scg.AGE_AT_COLLECTION=ROUND((datediff(scg.ENCOUNTER_TIMESTAMP,part.BIRTH_DATE)/365),0)
	WHERE
		sep.EVENT_TIMESTAMP IS NOT NULL
		AND sep.SPECIMEN_COLL_GRP_ID IS NOT NULL;
/
Alter table QUERY_PARAMETERIZED_QUERY add column SHOW_TREE bit default 0;
/
Insert into catissue_permissible_value(PARENT_IDENTIFIER,VALUE) select 3,'Buffy Coat' from dual where not exists (select * from catissue_permissible_value where PARENT_IDENTIFIER=3 and Value like 'buffy coat')
/
COMMIT;
/	
alter table CATISSUE_SPECIMEN_PROTOCOL add column LABEL_FORMAT varchar(255) default null;

alter table CATISSUE_SPECIMEN_PROTOCOL add column DERIV_LABEL_FORMAT varchar(255) default null;
alter table CATISSUE_SPECIMEN_PROTOCOL add column ALIQUOT_LABEL_FORMAT varchar(255) default null;

CREATE TABLE KEY_SEQ_GENERATOR
   (	IDENTIFIER bigint(20) NOT NULL auto_increment,
	KEY_VALUE VARCHAR(50) NOT NULL,
	KEY_SEQUENCE_ID VARCHAR(50) NOT NULL,
	KEY_TYPE VARCHAR(255) NOT NULL,
	 PRIMARY KEY (IDENTIFIER)
	 );


update dyextn_role set ASSOCIATION_TYPE = 'CONTAINTMENT'
where identifier in
(
	select source_role_id from dyextn_association where identifier in
	(
		select de_association_id from intra_model_association where association_id =
		(
			select intermediate_path from path where first_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArrayContent'
			)
			and last_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArray'
			)
		)
	)
);
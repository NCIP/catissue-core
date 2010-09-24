 /*Added for IDP in catissue V1.2*/


DROP TABLE IF EXISTS `csm_migrate_user`;

CREATE TABLE `csm_migrate_user` (
  `LOGIN_NAME` varchar(100) NOT NULL,
  `TARGET_IDP_NAME` varchar(100) default NULL,
  `MIGRATED_LOGIN_NAME` varchar(100) default NULL,
  `MIGRATION_STATUS` varchar(100) default NULL,
  PRIMARY KEY  (`LOGIN_NAME`)
);


 /*Added for removing the unhooked record added in catissue V1.1.2 deployment.*/
delete from dyextn_abstract_record_entry where identifier not in
(
select identifier from catissue_participant_rec_ntry
union
select identifier from catissue_scg_rec_ntry
union
select identifier from catissue_specimen_rec_ntry sre
);
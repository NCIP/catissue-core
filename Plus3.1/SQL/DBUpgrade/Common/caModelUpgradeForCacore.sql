--clinical_annotation updates
update dyextn_role set name = 'radiationTherapy' where identifier=998;
update dyextn_role set name = 'deprecatedAnnotation2' where identifier=905;
update dyextn_role set name = 'deprecatedAnnotation2' where identifier=907;

--pathology_specimen updates
update dyextn_role set name = 'additionalFinding' where name='Additional Finding';
update dyextn_role set name = 'breastSpecimenPathologyAnnotation' where identifier=1221;
update dyextn_role set name = 'breastSpecimenNottinghamHistologicScore' where identifier=1222;
update dyextn_role set name = 'prostateSpecimenPathologyAnnotation' where identifier=1219;
update dyextn_role set name = 'prostateSpecimenGleasonScore' where identifier=1220;

--check whether to delete ------------ 1322, 1318 -- need to delete hierarchy
update dyextn_abstract_metadata set name = 'SpecimenDetails1' where identifier=1322;

--   delete association between 1365 and 1369 and delete 1369 from dyextn_abstract_metadata?
delete from dyextn_column_properties where cnstr_key_prop_id=(select identifier from dyextn_constraintkey_prop where src_constraint_key_id=1371 or tgt_constraint_key_id=1371);
delete from dyextn_constraintkey_prop where src_constraint_key_id=1371 or tgt_constraint_key_id=1371;
delete from dyextn_constraint_properties where association_id=1388;
delete from dyextn_association where source_role_id in (959,960) or target_role_id in (959,960);
delete from dyextn_attribute where identifier = 1388;
delete from DYEXTN_BASE_ABSTRACT_ATTRIBUTE where identifier = 1388;
delete from dyextn_role  where identifier in (959,960);
delete  from path where intermediate_path='704';
delete  from intra_model_association where de_association_id=1388;
--delete from dyextn_abstract_metadata where identifier=1369

-----delete association between SpecimenGleasonScore -- 1381 and ProstateSpecimenPathologyAnnotation --1377
---and delete 1381 ---
delete from dyextn_column_properties where cnstr_key_prop_id=(select identifier from dyextn_constraintkey_prop where src_constraint_key_id=1381 or tgt_constraint_key_id=1381);
delete from dyextn_constraintkey_prop where src_constraint_key_id=1381 or tgt_constraint_key_id=1381;
delete from dyextn_constraint_properties where association_id=1389;
delete from dyextn_association where source_role_id in (962,961) or target_role_id in (962,961);
delete from dyextn_attribute where identifier = 1389;
delete from DYEXTN_BASE_ABSTRACT_ATTRIBUTE where identifier = 1389;
delete from dyextn_role where identifier in (962,961);
delete from path where intermediate_path='699';
delete from intra_model_association where de_association_id=1389;
--delete from dyextn_abstract_metadata where identifier=1389

--pathology_scg updates -- these role names were null
update dyextn_role set name = 'baseSolidTissuePathologyAnnotation' where identifier=1327;
update dyextn_role set name = 'pathologicalStaging' where identifier=1329;
update dyextn_role set name = 'distantMetastasis' where identifier=1331;
update dyextn_role set name = 'radicalProstatectomyPathologyAnnotation' where identifier=1333;
update dyextn_role set name = 'radicalProstatectomyMargin' where identifier=1335;
update dyextn_role set name = 'localExcisionBasedColorectalPathologyAnnotation' where identifier=1337;
update dyextn_role set name = 'localExcisionColorectalLateralMargin' where identifier=1339;
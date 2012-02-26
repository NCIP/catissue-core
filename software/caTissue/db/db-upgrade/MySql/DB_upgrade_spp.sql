update catissue_spp set spp_template_xml = LOAD_FILE('@@template@@') where identifier in
(select spp_id from temp_spp_events);
//
drop table temp_spp_events;
//
commit;
//
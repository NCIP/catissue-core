delete from DYEXTN_TAGGED_VALUE where identifier=1466 and t_key='PackageName';
--NACC_11thDec updates --- Oracle production dump
update dyextn_role set name = 'geneticInformation' where identifier=1411;

--NACC_7thJan updates --- Oracle production dump
update dyextn_role set name = 'geneticInformation' where identifier=1463;
update DYEXTN_ABSTRACT_METADATA set name = 'PatientInformation' where identifier=3609;

--BrainMicroscopy_3rdMarch updates ---- Oracle production dump
update DYEXTN_TAGGED_VALUE set t_value='NPmodel3March' where abstract_metadata_id=4367;

--BrainMicroscopy updates
update DYEXTN_ABSTRACT_METADATA set name='abaa' where name='aBAA';
update DYEXTN_ABSTRACT_METADATA set name='abcp' where name='aBCP';
update DYEXTN_ABSTRACT_METADATA set name='abdp' where name='aBDP';
update DYEXTN_ABSTRACT_METADATA set name='agdStage' where name='aGDStage';
update DYEXTN_ABSTRACT_METADATA set name='ceradCriteria' where name='cERADCriteria';
update DYEXTN_ABSTRACT_METADATA set name='dlbCriteria' where name='dLBCriteria';
update DYEXTN_ABSTRACT_METADATA set name='niaReaganCriteria' where name='nIAReaganCriteria';
update DYEXTN_ABSTRACT_METADATA set name='pmi' where name='pMI';
update DYEXTN_ABSTRACT_METADATA set name='tdpDystrophicNeuritis' where name='tDPDystrophicNeuritis';
update DYEXTN_ABSTRACT_METADATA set name='tdpGlialCytoplasmicInclusions' where name='tDPGlialCytoplasmicInclusions';
update DYEXTN_ABSTRACT_METADATA set name='tdpNeuronalCytoplasmicInclusions' where name='tDPNeuronalCytoplasmicInclusions';
update DYEXTN_ABSTRACT_METADATA set name='tdpNeuronalIntranuclearInclusions' where name='tDPNeuronalIntranuclearInclusions';
update DYEXTN_ABSTRACT_METADATA set name='ubqGrains' where name='uBQGrains';
update DYEXTN_ABSTRACT_METADATA set name='ubqNI' where name='uBQNI';

--NACC updates------------
update DYEXTN_ABSTRACT_METADATA set name='ceradNeuropathologicalCriteriaUsed' where name='CERADNeuropathologicalCriteriaUsed';
update DYEXTN_ABSTRACT_METADATA set name='ifOtherIsI1Specify' where name='IfOtherIsI1Specify';
update DYEXTN_ABSTRACT_METADATA set name='ifOtherIsJ1Specify' where name='IfOtherIsJ1Specify';
update DYEXTN_ABSTRACT_METADATA set name='ifOtherIsK1Specify' where name='IfOtherIsK1Specify';

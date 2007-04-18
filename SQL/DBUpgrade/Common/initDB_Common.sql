/*Vijay Pande: Bug:3834: For Specimen class = 'Molecular' following two permissible values are added*/
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(2651,'Total Nucleic Acid',1,NULL);
INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES(2652,'Whole Genome Amplified DNA',1,NULL);
/* Virender Mehta Non Required valuess 'Not specified' for race and ethnicity for CatissueV1.2 requirement*/
DELETE FROM CATISSUE_PERMISSIBLE_VALUE where IDENTIFIER=2649;
DELETE FROM CATISSUE_PERMISSIBLE_VALUE where IDENTIFIER=2650;
/* Virender Mehta update Male and Female values for CatissueV1.2 requirement*/
UPDATE CATISSUE_PERMISSIBLE_VALUE SET VALUE = "Male Gender" WHERE identifier=59;
UPDATE CATISSUE_PERMISSIBLE_VALUE SET VALUE = "Female Gender" WHERE identifier=61;
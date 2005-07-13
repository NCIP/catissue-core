create table CATISSUE_QUERY_RESULTS (
   IDENTIFIER NUMBER(19,0) not null,
   PARTICIPANT_ID NUMBER(20,0),
   ACCESSION_ID NUMBER(20,0),
   SPECIMEN_ID NUMBER(20,0),
   SEGMENT_ID NUMBER(20,0),
   SAMPLE_ID NUMBER(20,0),
   TISSUE_SITE VARCHAR2(50),
   SPECIMEN_TYPE VARCHAR2(50),
   SAMPLE_TYPE VARCHAR2(50),
   primary key (IDENTIFIER)
)

insert into CATISSUE_QUERY_RESULTS VALUES (1,2,104,1,1,1,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (2,2,104,1,1,9,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (3,2,104,1,1,11,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (4,2,104,1,9,10,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (5,2,104,1,9,12,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (6,2,104,2,2,2,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (7,2,104,2,2,13,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (8,2,104,2,8,8,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (9,2,113,3,3,3,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (10,46,47,4,4,4,'ddd','eee','fff');
insert into CATISSUE_QUERY_RESULTS VALUES (11,46,47,4,5,5,'ddd','eee','fff');
insert into CATISSUE_QUERY_RESULTS VALUES (12,46,48,5,6,6,'aaa','bbb','ccc');
insert into CATISSUE_QUERY_RESULTS VALUES (13,46,48,6,7,7,'aaa','bbb','ccc');

insert into CATISSUE_PARTICIPANT VALUES (2,'Shetty','Gautam','V','06-Jun-2005','Male',123456,123456,'Asian',4);
insert into CATISSUE_PARTICIPANT VALUES (46,'SSS','GGG','VVV','06-Jun-2005','Male',78910,111213,'Asian',4);

insert into CATISSUE_ACCESSION VALUES (104,123,456,'www','diagnosis','status',123,'asd','24-Jun-2005',64,'by hand',1234,'24-Jun-2005',64,4,2,45);
insert into CATISSUE_ACCESSION VALUES (113,123,456,'aaa','dddd','ssss',123,'asdasd','24-Jun-2005',64,'by hand',1234,'24-Jun-2005',64,4,2,45);
insert into CATISSUE_ACCESSION VALUES (47,123,456,'qqqqqqq','ddddd','status',123,'asd','24-Jun-2005',64,'by hand',1234,'24-Jun-2005',64,4,46,45);
insert into CATISSUE_ACCESSION VALUES (48,123,456,'ooo','pppppppp','status',123,'asd','24-Jun-2005',64,'by hand',1234,'24-Jun-2005',64,4,46,45);

COMMIT
alter table QUERY_PARAMETERIZED_QUERY add (GENERATE_LABEL number(1,0) default 1);
alter table QUERY_PARAMETERIZED_QUERY add (LABEL_FORMAT varchar(255) default null);
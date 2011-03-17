CREATE TABLE `comptablesdatarows` (
  `schema1` varchar(100) default NULL,
  `schema1_table` varchar(100) default NULL,
  `schema1_Rowscnt` int(11) default NULL,
  `schema2` varchar(100) default NULL,
  `schema2_table` varchar(100) default NULL,
  `schema2_Rowscnt` int(11) default NULL,
  `SameTAB_struct` varchar(20) default NULL,
  `Identical_row` varchar(4000) default NULL,
  `DifStruc_schema1` varchar(4000) default NULL,
  `DifStruc_schema2` varchar(4000) default NULL,
  `Data_Comparison_Comment` varchar(4000) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

create table mysql.common_row_count (count_val int, tab_name varchar(300));

/* create database test and create table asd into the database used for debugging */
CREATE TABLE test.asd (temp_1 varchar(1000) default NULL);

-- comapre Db cmd
call mysql.Sp_compTables ('catissuev12', 'catissuev112' )




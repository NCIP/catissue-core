DELIMITER $$;

DROP PROCEDURE IF EXISTS `mysql`.`Sp_compTables`$$

CREATE DEFINER=`root`@`%` PROCEDURE `Sp_compTables`(
pvchSchema1 varchar(100),
pvchSchema2 varchar(100)
)
BEGIN
declare col1 varchar(200) default '';
declare optmize varchar(250) default '';
DECLARE done INT DEFAULT 0;
Declare pvchSchema1_row int DEFAULT 0;
Declare pvchSchema2_row int DEFAULT 0;
Declare compTableStruct varchar(20) default '';
Declare noOfIdenRows varchar(20);
Declare dataComparisonResult int DEFAULT 0;
Declare CommCols varchar(4000) default '';
Declare _lsql varchar(4000) default '';
Declare schcnt1 varchar(20);
declare colDataComparison varchar(200) default '';
declare DiffCOlsSchema1 varchar(4000);
declare DiffCOlsSchema2 varchar(4000);
declare Data_Comparison_Comment varchar(4000) default '';
declare  c1 cursor for  select table_name  from information_schema.tables where table_Schema in (pvchSchema1 , pvchSchema2)
			group by TABLE_NAME having count(*) > 1;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	set col1 = '';
	open c1;
	repeat
		fetch c1 into col1 ;
	if not done then
		set @optmize = concat('ANALYZE table ', pvchSchema1 , '.' , col1);
		/*insert into test.asd values (@optmize);  */
		PREPARE s1 FROM @optmize;
		execute s1 ;
		deallocate prepare s1 ;
/* CALL NEXT SCHWEMA */
		set @optmize = concat('ANALYZE table ', pvchSchema2 , '.' , col1);
		PREPARE s1 FROM @optmize;
		execute s1 ;
		deallocate prepare s1 ;
		/*insert into test.asd values ('here..' );*/
	/* not get the row counts*/
		select
			table_rows
		from
			information_schema.tables
		where table_Schema in (pvchSchema1) and  TABLE_NAME = col1
		into pvchSchema1_row;

		/*insert into test.asd values (pvchSchema1_row );*/
	/* not get the row counts for the same table in the next Db*/
		select
			table_rows
		from
			information_schema.tables
		where table_Schema in (pvchSchema2) and  TABLE_NAME = col1
		into pvchSchema2_row ;
		set @_lsql=concat('', ' select  Count(*) into @schcnt1 from ', pvchSchema1 ,'.',col1);
		PREPARE s1 FROM @_lsql;
		execute s1 ;
		deallocate prepare s1 ;
		set pvchSchema1_row =  @schcnt1;
		set @_lsql=concat('', ' select  Count(*) into @schcnt1 from ', pvchSchema2 ,'.',col1);
		PREPARE s1 FROM @_lsql;
		execute s1 ;
		deallocate prepare s1 ;
		set pvchSchema2_row =  @schcnt1;
	/* now check if tables have smae struct */

		set compTableStruct = `mysql`.`sf_compTablesInschemas` (col1, pvchSchema1 , pvchSchema2);
	/* now call the sf_compTablesDataRows to get the idnetical rows */
		if (compTableStruct = 'true' ) then
			call mysql.sf_compTablesDataRows( col1, pvchSchema1 , pvchSchema2);
			SELECT count_val INTO noOfIdenRows FROM mysql.common_row_count where tab_name=col1;
			set CommCols = noOfIdenRows;
			insert into mysql.compTablesDataRows
			select  pvchSchema1, col1, pvchSchema1_row,
			pvchSchema2, col1, pvchSchema2_row,
			compTableStruct,
			CommCols,
			null,
			null,null ;
		else
		      set CommCols  =  `mysql`.`sf_getColsDiffStruct`( col1, pvchSchema1 , pvchSchema2);
			/*insert into test.asd values (concat('CommCols   ' ,CommCols   ));*/
		      call `mysql`.`sp_CompTabData_diffStruc`( col1, pvchSchema1 , pvchSchema2);
			SELECT count_val INTO noOfIdenRows FROM mysql.common_row_count where tab_name=col1;
			/*insert into test.asd values (concat('# of rows diif ' ,noOfIdenRows ));*/

			/*--- */
			set DiffCOlsSchema1  = `mysql`.`sf_compDBTab_W_Diffcols`(col1, pvchSchema1 , pvchSchema2);
			set DiffCOlsSchema2  = `mysql`.`sf_compDBTab_W_Diffcols`(col1, pvchSchema2 , pvchSchema1);
           /* insert into test.asd values (concat('col1 before ' ,col1 ));*/

            if (col1 = 'catissue_container' ) then
			/*insert into test.asd values (concat('in container before function  ' ,col1 ));*/
			call `mysql`.`sp_compareDataWithDiffStruc`(col1, pvchSchema1 ,'IDENTIFIER','FULL', pvchSchema2,'IDENTIFIER','CONT_FULL','catissue_container_update');
			/*insert into test.asd values (concat('in container ' ,colDataComparison ));*/

			SELECT count_val INTO dataComparisonResult FROM mysql.common_row_count where tab_name='catissue_container_update';
            /*insert into test.asd values (concat('# of rows diif ' ,dataComparisonResult ));*/
            if (dataComparisonResult > 0  and col1 = 'catissue_container') then
			  set Data_Comparison_Comment = concat('' ,'Columns does not contain identical rows' );
			else
			  set Data_Comparison_Comment = concat('' ,'Columns contains identical rows' );

			/*insert into test.asd values (concat('Comment ' ,Data_Comparison_Comment ));*/
			end if;
			else
			  set Data_Comparison_Comment = '';
			end if;



			insert into mysql.compTablesDataRows
			select  pvchSchema1, col1, pvchSchema1_row,
			pvchSchema2, col1, pvchSchema2_row,
			compTableStruct,
			/* concat('' ,noOfIdenRows , ';' ' COLS COmp-->',CommCols ), */
			 concat('' ,noOfIdenRows ),
			DiffCOlsSchema1,
			DiffCOlsSchema2,Data_Comparison_Comment
			;

		end if ;


	end if ;
		UNTIL done END REPEAT;
	close c1;
	select * From mysql.compTablesDataRows;
END$$

DELIMITER ;$$
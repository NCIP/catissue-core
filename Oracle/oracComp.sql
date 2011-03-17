WITH ASD AS  (
      select /*+ materialize */  TABLE_NAME  from DBA_TABLES WHERE owner in('db_before_upgrade', 'db_after_upgrade')
      group by TABLE_NAME  having count(*) > 1) ,


    qwe as (
      Select /*+ materialize */
               asd.TABLE_NAME as tab,
               max(decode(OWNER, 'db_before_upgrade',NUM_ROWS, 0  )) as CATISSUEV12ROWS,
               max(decode(OWNER, 'db_after_upgrade',NUM_ROWS , 0 ) ) as CATISSUEV112RROWS
        from DBA_TABLES  tabs, ASD
      WHERE tabs.table_name = asd.table_name
      group by asd.table_name),



      zxc as (select qwe.* , sf_compTablesInschemas(tab, 'db_before_upgrade', 'db_after_upgrade') as samestruct
            from qwe)


      select a.* ,
            case when ((CATISSUEV12ROWS* CATISSUEV112RROWS > 0 ) and (SAMESTRUCT= 'true')) then
                           sf_compTablesDataRows(tab, 'db_before_upgrade', 'db_after_upgrade')
                   else       sf_compTabDataRowsDiffStruct( tab, 'db_before_upgrade', 'db_after_upgrade')    end
            as identicalrows,
            case when (SAMESTRUCT= 'false') then  sf_compTabInschemasWDiffStruct(tab, 'db_before_upgrade', 'db_after_upgrade')  end as ColsCompred,
            case when (SAMESTRUCT= 'false') then   sf_compSchemaTab_Diffcols(tab, 'db_before_upgrade', 'db_after_upgrade')  end as DifStruct_CATISSUEV12,
            case when (SAMESTRUCT= 'false') then   sf_compSchemaTab_Diffcols(tab, 'db_before_upgrade', 'db_after_upgrade')  end as DifStruct_CATISSUEV112,
            case when ((tab = 'CATISSUE_CONTAINER') and (SAMESTRUCT= 'false')) then sf_compDataInTables('CATISSUE_CONTAINER', 'db_after_upgrade','FULL', 'db_after_upgrade','CONT_FULL') end as DiffColName_Data_Comparison
      from ZXC a order by TAB 
      
   
      
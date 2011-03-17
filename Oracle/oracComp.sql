exec DBMS_UTILITY.ANALYZE_SCHEMA('CATISSUE_P5_1_UPGRADED','COMPUTE', null,100, 'FOR TABLE');







WITH ASD AS  (
      select /*+ materialize */  TABLE_NAME  from DBA_TABLES WHERE owner in('CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')
      group by TABLE_NAME  having count(*) > 1) ,


    qwe as (
      Select /*+ materialize */
               asd.TABLE_NAME as tab,
               max(decode(OWNER, 'CATISSUE_P5_1',NUM_ROWS, 0  )) as CATISSUEV12ROWS,
               max(decode(OWNER, 'CATISSUE_P5_1_UPGRADED',NUM_ROWS , 0 ) ) as CATISSUEV112RROWS
        from DBA_TABLES  tabs, ASD
      WHERE tabs.table_name = asd.table_name
      group by asd.table_name),



      zxc as (select qwe.* , sf_compTablesInschemas(tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED') as samestruct
            from qwe)


      select a.* ,
            case when ((CATISSUEV12ROWS* CATISSUEV112RROWS > 0 ) and (SAMESTRUCT= 'true')) then
                           sf_compTablesDataRows(tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')
                   else       sf_compTabDataRowsDiffStruct( tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')    end
            as identicalrows,
            case when (SAMESTRUCT= 'false') then  sf_compTabInschemasWDiffStruct(tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')  end as ColsCompred,
            case when (SAMESTRUCT= 'false') then   sf_compSchemaTab_Diffcols(tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')  end as DifStruct_CATISSUEV12,
            case when (SAMESTRUCT= 'false') then   sf_compSchemaTab_Diffcols(tab, 'CATISSUE_P5_1', 'CATISSUE_P5_1_UPGRADED')  end as DifStruct_CATISSUEV112,
            case when ((tab = 'CATISSUE_CONTAINER') and (SAMESTRUCT= 'false')) then sf_compDataInTables('CATISSUE_CONTAINER', 'CATISSUE_P5_1_UPGRADED','FULL', 'CATISSUE_P5_1_UPGRADED','CONT_FULL') end as DiffColName_Data_Comparison
      from ZXC a order by TAB 
      
   
      
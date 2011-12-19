create or replace
procedure Mol_migrate(event_name in varchar2) IS
     
     counter INTEGER;
     form_context_id INTEGER; 
     seqval Number(15); 
     specimen_event_identifier INTEGER(19) ;
     specimen_id INTEGER ;
     specimen_event_user_id INTEGER ; 
     specimen_event_param_id INTEGER ;
     specimen_comments varchar2(1000);
     specimen_timestamp timestamp;
		parent_specimen_id Integer;
		flag Integer;
     dispo_Imgurl Varchar2(1000);
     dispo_QIx Varchar2(1000);
     dispo_Lno Varchar2(1000);
     dispo_Gel_no Number(20);
     dispo_Abs_260 Number(20);
     dispo_Abs_280 NUMBER (20);
     dispo_Ratio_28s NUMBER (20);
     query_text varchar2(1000);
     query_text_form varchar2(1000);

      v_code  NUMBER;
       v_errm  VARCHAR2(1000);
      
    cursor mig_cursor  IS
    select spec.identifier,
           spec.specimen_id,
           spec.event_timestamp,
           spec.user_id,
           spec.comments,
           TRAN.GEL_IMAGE_URL,
           TRAN.QUALITY_INDEX,
           TRAN.LANE_NUMBER,
           TRAN.GEL_NUMBER,
           TRAN.ABSORBANCE_AT_260,
           TRAN.ABSORBANCE_AT_280,
           TRAN.RATIO_28S_TO_18S,
		    absspec.parent_specimen_id
      from CATISSUE_MOL_SPE_REVIEW_PARAM TRAN,
            catissue_specimen_event_param spec,
        catissue_specimen se,
        catissue_abstract_specimen absspec
	where
      TRAN.identifier = spec.identifier 
      and spec.specimen_id=se.identifier 
      and absspec.IDENTIFIER = se.identifier ;


Begin
  
  -----------------------------query for form contex id--------------------------------------
  SELECT formContext.identifier into form_context_id FROM dyextn_abstract_form_context formContext 
              join dyextn_container dcontainer
              on formcontext.container_id = dcontainer.identifier
              join dyextn_entity ent
              on ent.identifier = dcontainer.abstract_entity_id
              join dyextn_abstract_metadata  meta
              on meta.name=event_name and meta.identifier = ent.identifier
              join dyextn_abstract_metadata  meta2
              on meta2.name = 'SpecimenEvents'
              join dyextn_entity_group eg
              on eg.identifier=meta2.identifier
              and eg.identifier  =  ent.ENTITY_GROUP_ID and formContext.Activity_Status='Active';
              
 -----------------------------------calling function--------------------------------------------------------------- */       
              
              select query_formation_excol_mol(event_name) into query_text from dual;
              -- DBMS_OUTPUT.PUT_LINE(query_text);
              --DBMS_OUTPUT.PUT_LINE(form_context_id);
             
         counter :=0;     
  ------------------------------------------------------------------------------------------------------------------       
      open mig_cursor;

     
          
      LOOP
      
      
      fetch mig_cursor into specimen_event_identifier,
                            specimen_id,
                            specimen_timestamp,
                            specimen_event_user_id,
                            specimen_comments,
                            dispo_Imgurl,
                            dispo_QIx, 
                            dispo_Lno,
                            dispo_Gel_no, 
                            dispo_Abs_260,
                            dispo_Abs_280,
                            dispo_Ratio_28s,
							parent_specimen_id;
    EXIT WHEN mig_cursor%NOTFOUND;
		 select count(*) into flag  from 
        catissue_received_event_param rec,
        catissue_specimen_event_param spec
        where 
        spec.specimen_id =parent_specimen_id
        and spec.event_timestamp = specimen_timestamp
        and spec.identifier=rec.identifier ;
       IF flag=0 THEN 
      Begin
      -- DBMS_OUTPUT.PUT_LINE(specimen_event_identifier||'  '||specimen_id||'  '||specimen_timestamp||' '||specimen_event_user_id||' '||specimen_comments||' '||dispo_reason);                 
      -------------------------------------------------------------------
      insert into dyextn_abstract_record_entry
      (IDENTIFIER,modified_date,activity_status,abstract_form_context_id)
      values (DYEXTN_ABSTRACT_RE_SEQ.NEXTVAL,sysdate,'Active',form_context_id);  
      -------------------------------------------------------------------      
      
      insert into catissue_action_app_rcd_entry(identifier)values(DYEXTN_ABSTRACT_RE_SEQ.CURRVAL);
      
      -----------------------------
   query_text_form :='insert  into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments)
     values(:1, :2, :3, :4)';
     execute immediate query_text_form using specimen_event_identifier,specimen_timestamp,specimen_event_user_id,specimen_comments;
       
      -------------------------------------------------------------------
       
      insert into  catissue_action_application
      (identifier,specimen_id,action_app_record_entry_id)
      values(specimen_event_identifier,specimen_id,DYEXTN_ABSTRACT_RE_SEQ.CURRVAL);
      
      -------------------------------------------------------------------
      select DYEXTN_ABSTRACT_RE_SEQ.CURRVAL into seqval from dual;
  
      
    
    
     --DBMS_OUTPUT.PUT_LINE(specimen_id||'  '||specimen_event_identifier||'  '||seqval);
      EXECUTE IMMEDIATE query_text using dispo_Abs_260,dispo_Abs_280,dispo_Imgurl,dispo_Gel_no,specimen_event_identifier,dispo_Lno,dispo_QIx,dispo_Ratio_28s,seqval; 
     -- DBMS_OUTPUT.PUT_LINE(query_text_form);
     
     counter :=counter+1;
    
       NULL;
     EXCEPTION WHEN OTHERS THEN
      v_code := SQLCODE;
      v_errm := SUBSTR(SQLERRM, 1, 1000);
      DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm||' '||counter );
      end;
	  end IF;
     
    end loop;   
    DBMS_OUTPUT.PUT_LINE(counter);
   close mig_cursor; 
    ------------------------------------------------------------------
   EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
      rollback;
      DBMS_OUTPUT.PUT_LINE('Duplicate value on an index'||counter);
    WHEN OTHERS THEN
      rollback;
      v_code := SQLCODE;
      v_errm := SUBSTR(SQLERRM, 1, 1000);
      DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||','||v_errm||','||counter );
end Mol_migrate;
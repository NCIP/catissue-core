--Script for migrating data in CATIS to caTissue
DECLARE

BEGIN
    --Patients-->catissue_participants
    DBMS_OUTPUT.PUT('Migrating Patients Data...');
    INSERT ALL
    INTO catissue.catissue_participant (identifier, last_name, first_name, gender, birth_date)
    VALUES (catissue.catissue_participant_seq.NEXTVAL, lname, fname, gender, birth)
    INTO catissue.catissue_race (participant_id, race_name)
    VALUES (catissue.catissue_participant_seq.CURRVAL, race)
    SELECT race, lname, fname, gender, birth
    FROM emptycatis.patients;
    --The missing fields in catissue_participant are middle_name, genotype, ethnicity, social_security_number, 
    --activity status, death_date and vital status.
    DBMS_OUTPUT.PUT_LINE('Done');

    DBMS_OUTPUT.PUT('Migrating Institutions Data...');
    INSERT ALL
    INTO catissue_address (identifier, city, state, zipcode)
    VALUES(catissue.catissue_address_seq.NEXTVAL, instcity, inststate, instzip)
    INTO catissue.catissue_site (identifier, name, address_id) 
    VALUES (catissue.catissue_site_seq.NEXTVAL, instname, catissue.catissue_address_seq.CURRVAL) 
    SELECT instname, coordfname, coordlname, instcity, inststate, instzip, coordphone, coordfax 
    FROM emptycatis.institutions;
    --The missing fields in address for site are: street, country, phone and fax

    INSERT ALL
    INTO catissue_address (identifier, phone_number, fax_number)
    VALUES (catissue.catissue_address_seq.NEXTVAL, coordphone, coordfax)
    INTO catissue.catissue_user(identifier, first_name, last_name, address_id) --login is compulsory
    VALUES (catissue.catissue_user_seq.NEXTVAL, coordfname, coordlname, catissue.catissue_address_seq.CURRVAL)
    SELECT instname, coordfname, coordlname, instcity, inststate, instzip, coordphone, coordfax 
    FROM emptycatis.institutions;
    --The missing fields in address for coordinator/user are: street, city, state, country, zipcode 

    UPDATE catissue_site 
    SET user_id = (SELECT identifier FROM catissue_user JOIN
        emptycatis.institutions ON (last_name=coordlname AND first_name=coordfname) WHERE name=instname);
    --The missing fields in site are: type, email_address, activity_status
    DBMS_OUTPUT.PUT_LINE('Done');

    DBMS_OUTPUT.PUT('Migrating Studies Data...');
    INSERT INTO catissue_specimen_protocol (identifier, principal_investigator_id, short_title, title, irb_identifier)
    SELECT catissue.catissue_specimen_protocol_seq.NEXTVAL, catissue_user.identifier, studyname, studytitle, hscnumber 
    FROM emptycatis.studies join catissue.catissue_user ON (studypi = first_name || ' ' || last_name); --confirm this
    --The missing fields in catissue_specimen_protocol are start_date, end_date, enrollment
    --description_url, activity_status
    DBMS_OUTPUT.PUT_LINE('Done');
END;
/

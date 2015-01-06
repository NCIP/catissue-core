{
  "common": {
    "count" : "Count",
    "date": "Date",
    "na": "N/A",
    "yes": "Yes",
    "no": "No",
    "none": "None",

    "buttons": {
      "add": "Add",
      "save": "Save",
      "create": "Create",
      "cancel": "Cancel",
      "discard": "Discard"
    }
  },

  "cp": {
    "list": "Collection Protocols",
    "create_cp_title": "Create Collection Protocol",
    
    "title": "Title",
    "short_title": "Short Title",
    "pi": "Principal Investigator",
    "coordinators": "Protocol Coordinators",
    "start_date": "Start Date",
    "consents_waived": "Consents Waived",
    "ethics_approval_id": "Ethics Approval ID",
    "ppid_fmt": "Protocol Identifier Format",
    "ppid_prefix": "Prefix",
    "ppid_digits": "No. of digits",
    "ppid_suffix": "Suffix",
    "clinical_diagnoses": "Clinical Diagnoses",
    "anticipated_participants_cnt": "Anticipated Participants Count",
    "desc_url": "Description URL",
    "specimen_label_fmt": "Specimen Label Format",
    "derivative_label_fmt": "Derivative Label Format",
    "aliquot_label_fmt": "Aliquot Label Format",
    "store_all_aliquots_in_same_container": "Store all Aliquots in same Container",

    "unsigned_consent_url": "Unsigned Consent Form URL",
    "consent_tiers": "Consent Tiers", 
    "add_consent_tier": "Add Statement...",

    "default_site": "Default Site",
    "clinical_diagnosis": "Clinical Diagnosis",
    "clinical_status": "Clinical Status",
    
    "no_cpes": "There are no collection protocol events to show. Please create an event by clicking on Add Event...",
    "add_cpe": "Add Event...",
    "cpe_list": "Events",
    "cpe_point": "Event Point",
    "cpe_point_label": "Event Point Label",

    "menu": {
      "overview": "Overview",
      "consents": "Consents",
      "events": "Events",
      "users": "Users",
      "dashboard": "Dashboard"
    },

    "tooltip": {
      "view_details": "Click to view/edit Collection Protocol details",
      "reg_participants_count": "Count of Registered Participants",
      "collected_specimens_count": "Count of Collected Specimens"
    }
  },

  "participant": {
    "list": "Participants",
   
    "register_participant": "Register Participant",
    "reg_date": "Registration Date",
    "ppid": "Participant Protocol ID",
    "name": "Name",
    "first_name": "First Name",
    "last_name": "Last Name",
    "middle_name": "Middle Name",
    "birth_date": "Birth Date",
    "ssn": "Social Security Number", 
    "ssn_short": "SSN",
    "mpi": "Master Patient Index",
    "gender": "Gender",
    "vital_status": "Vital Status",
    "ethnicity": "Ethnicity",
    "death_date": "Death Date",
    "race": "Race",
    "select_race": "Select Race",
    "more_demographics": "More Demographic Information",
    "less_demographics": "Less Demographic Information",
    "mrn": "Medical Record Number",
    "mrn_short": "MRN",
    "site": "Site",
    "select_site": "Select Site",
    "pmis": "Medical Identifiers",
    "protocol_id": "Protocol Identifier",

    "matched_on_mpi": "Following Participant matched based on EMPI",
    "matched_on_ssn": "Following Participant matched based on SSN",
    "matched_on_pmi": "Following Participant matched based on MRN",
    "matched_on_lname_and_dob": "Following possible Participants matched based on Last Name and Birth Date",
    
    "buttons": {
      "register": "Register",
      "register_selected_participant": "Register Selected participant",
      "ignore_matches": "Ignore Matches and Register Participant",
      "lookup_again": "Lookup Again"
    },

    "menu": {
      "overview": "Overview",
      "consents": "Consents",
      "visits": "Visits",
      "extensions": "Extensions"
    },

    "tooltip": {
      "register": "Click to register new Participant",
      "view_details": "Click to view/edit Participant details",
      "completed_visits_count": "Count of Completed Visits",
      "collected_specimens_count": "Count of Collected Specimens"
    }
  },

  "visits": {
    "title": "Visit",
    "list": "Visits",
    "occurred_visits": "Occurred Visits",
    "collection_status": "Collection Status",
    "anticipated_visits": "Anticipated Visits",
    "anticipated_specimens": "Anticipated Specimens",

    "status": {
      "pending": "Pending",
      "complete": "Complete"
    },

    "ctx_menu": {
      "view_visit": "View Visit",
      "edit_visit": "Edit Visit",
      "collect_planned_specimens": "Collect Planned Specimens",
      "collect_unplanned_specimens": "Collect Unplanned Specimens",
      "print_specimen_labels": "Print Specimen Labels"
    }
  },

  "specimens": {
    "title": "Specimen",
    "list": "Specimens",
    "status": {
      "collected": "Collected",
      "not_collected": "Not Collected",
      "pending": "Pending"
    },

    "unplanned": "Unplanned",

    "label": "Label",
    "type": "Type",
    "pathology": "Pathology",
    "container": "Container",

    "ctx_menu": {
      "view_specimen": "View Specimen",
      "edit_specimen": "Edit Specimen",
      "create_aliquots": "Create Aliquots",
      "create_derivatives": "Create Derivatives",
      "add_event": "Add Event",
      "dispose": "Dispose"
    }
  },

  "srs": {
    "title": "Specimen Requirement",
    "list": "Specimen Requirements",
    "lineage": {
      "new": "New",
      "aliquot": "Aliquot",
      "derivative": "Derivative"
    },

    "no_srs": "There are no specimen requirements to show. Create a new requirement by clicking Add Specimen Requirement ...",
    "new_sr": "New Specimen Requirement",

    "name": "Name",
    "type": "Type",
    "pathology": "Pathology",
    "storage_type": "Storage Type",
    "label_fmt": "Label Format",
    "available_qty": "Available Quantity",
    "aliquot_cnt": "Count of Aliquots",
    "qty_per_aliquot": "Quantity per Aliquot",
    "specimen_class": "Specimen Class",
    "specimen_type": "Specimen Type",
    "qty": "Quantity",
    "concentration": "Concentration",
    "anatomic_site": "Anatomic Site",
    "laterality": "Laterality",
    "initial_qty": "Initial Quantity",
    "collector": "Collector",
    "receiver": "Receiver",
    "collection_container": "Collection Container",
    "collection_proc": "Collection Procedure",
    
    "ctx_menu": {
      "view_sr": "View Requirement",
      "edit_sr": "Edit Requirement",
      "create_aliquots": "Create Aliquots",
      "create_derivatives": "Create Derivatives"
    },
 
    "buttons": {
      "create_aliquots": "Create Aliquots",
      "create_derived": "Create Derived",
      "add_sr": "Add Specimen Requirement..."
    },

    "errors": {
      "insufficient_qty": "Insufficient parent requirement quantity to create aliquots"
    }
  },

  "institutes": {
    "list": "Institutes",
    "create_institute": "Create Institute",
    "name": "Name",
    "department_name": "Department Name",
    "departments": "Departments",

    "tooltip": {
      "view_details": "Click to view/edit Institute details",
      "add": "Click to add new Institute",
      "department_count": "Number of Departments",
      "user_count": "Number of Users in Institute"
    },

    "menu": {
      "overview": "Overview"
    }
  },
  
  "dp": {
    "list": "Distribution Protocols",
    "create_dp_title": "Create Distribution Protocol",
    "title": "Title",
    "short_title": "Short Title",
    "pi": "Principal Investigator",
    "start_date": "Start Date",
    "irb_id": "IRB ID",
    "description_url": "Description URL",
        
    "tooltip": {
      "view_details": "Click to view/edit Distribution Protocol details",
      "new_dp": "Click to add new Distribution Protocol",
      "pending_count":"Pending to distribute specimens count",
      "distributed_count":"Distributed specimens count"
    },
    
    "menu": {
      "overview": "Overview"
    }
  }

}

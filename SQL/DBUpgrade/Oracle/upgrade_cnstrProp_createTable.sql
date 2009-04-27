create table DYEXTN_ENTIY_COMPOSITE_KEY_REL (ENTITY_ID number(19,0) not null, ATTRIBUTE_ID number(19,0) not null, INSERTION_ORDER number(10,0) not null, primary key (ENTITY_ID, INSERTION_ORDER));

create table DYEXTN_CONSTRAINTKEY_PROP (IDENTIFIER number(19,0) not null, PRIMARY_ATTRIBUTE_ID number(19,0), SRC_CONSTRAINT_KEY_ID number(19,0), TGT_CONSTRAINT_KEY_ID number(19,0), primary key (IDENTIFIER));

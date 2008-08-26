/* remove old indexes */
/* this may be app specific */

/* catissue */
alter table QUERY_PARAMETERIZED_QUERY drop foreign key FKA272176BBC7298A9, drop index FKA272176BBC7298A9;
alter table QUERY_INTRA_MODEL_ASSOCIATION drop foreign key FKF1EDBDD3BC7298A9, drop index FKF1EDBDD3BC7298A9;
alter table QUERY_CONSTRAINTS drop foreign key FKE364FCFFD3C625EA, drop index FKE364FCFFD3C625EA;
alter table QUERY_PARAMETERIZED_CONDITION drop foreign key FK9BE75A3EBC7298A9, drop index FK9BE75A3EBC7298A9;
alter table QUERY_CONDITION drop foreign key FKACCE6246104CA7, drop index FKACCE6246104CA7;
alter table QUERY_RULE drop foreign key FK14A6503365F8F4CB, drop index FK14A6503365F8F4CB;
alter table QUERY_RULE drop foreign key FK14A65033BC7298A9, drop index FK14A65033BC7298A9;
alter table QUERY drop foreign key FK49D20A8251EDC5B, drop index FK49D20A8251EDC5B;
alter table QUERY_LOGICAL_CONNECTOR drop foreign key FKCF30478065F8F4CB, drop index FKCF30478065F8F4CB;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8FCA571190, drop index FK1B473A8FCA571190;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F9E19EF66, drop index FK1B473A8F9E19EF66;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F1CF7F689, drop index FK1B473A8F1CF7F689;
alter table QUERY_CONDITION_VALUES drop foreign key FK9997379DDA532516, drop index FK9997379DDA532516;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75BF5EEB27, drop index FK22C9DB75BF5EEB27;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75C4E818F8, drop index FK22C9DB75C4E818F8;
alter table QUERY_INTER_MODEL_ASSOCIATION drop foreign key FKD70658D1BC7298A9, drop index FKD70658D1BC7298A9;
alter table QUERY_EXPRESSION_OPERAND drop foreign key FKA3B976F965F8F4CB, drop index FKA3B976F965F8F4CB;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA955C60E6, drop index FKF055E4EA955C60E6;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAD3C625EA, drop index FKF055E4EAD3C625EA;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA7A5E6479, drop index FKF055E4EA7A5E6479;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAEE560703, drop index FKF055E4EAEE560703;
alter table QUERY_EXPRESSIONID drop foreign key FK6662DBEABC7298A9, drop index FK6662DBEABC7298A9;
/**********************************/
/* cab2b
alter table QUERY drop foreign key FK49D20A84B0F861E, drop index FK49D20A84B0F861E;
alter table QUERY_CONDITION drop foreign key FKACCE6242DCE1896, drop index FKACCE6242DCE1896;
alter table QUERY_CONDITION_VALUES drop foreign key FK9997379D4D1598FE, drop index FK9997379D4D1598FE;
alter table QUERY_CONSTRAINTS drop foreign key FKE364FCFF1C7EBF3B, drop index FKE364FCFF1C7EBF3B;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F526D4561, drop index FK1B473A8F526D4561;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8FCF83E189, drop index FK1B473A8FCF83E189;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F3FB1E956, drop index FK1B473A8F3FB1E956;
alter table QUERY_EXPRESSIONID drop foreign key FK6662DBEA62E3EDC7, drop index FK6662DBEA62E3EDC7;
alter table QUERY_EXPRESSION_OPERAND drop foreign key FKA3B976F9180A6E16, drop index FKA3B976F9180A6E16;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAB2B42E5B, drop index FKF055E4EAB2B42E5B;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA346832A9, drop index FKF055E4EA346832A9;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA1C7EBF3B, drop index FKF055E4EA1C7EBF3B;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAC070901F, drop index FKF055E4EAC070901F;
alter table QUERY_INTER_MODEL_ASSOCIATION drop foreign key FKD70658D15F5AB67E, drop index FKD70658D15F5AB67E;
alter table QUERY_INTRA_MODEL_ASSOCIATION drop foreign key FKF1EDBDD35F5AB67E, drop index FKF1EDBDD35F5AB67E;
alter table QUERY_LOGICAL_CONNECTOR drop foreign key FKCF304780180A6E16, drop index FKCF304780180A6E16;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75509C0ACD, drop index FK22C9DB75509C0ACD;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75604D4BDA, drop index FK22C9DB75604D4BDA;
alter table QUERY_PARAMETERIZED_CONDITION drop foreign key FK9BE75A3E4B9044D1, drop index FK9BE75A3E4B9044D1;
alter table QUERY_PARAMETERIZED_QUERY drop foreign key FKA272176B76177EFE, drop index FKA272176B76177EFE;
alter table QUERY_RULE drop foreign key FK14A65033180A6E16, drop index FK14A65033180A6E16;
alter table QUERY_RULE drop foreign key FK14A6503362E3EDC7, drop index FK14A6503362E3EDC7;
*/

/* rename of columns */
alter table query_condition_values change value_list VALUE varchar(255);
alter table query_condition_values change query_condition_id CONDITION_ID bigint;
alter table query change query_constraints_id CONSTRAINTS_ID bigint;
alter table QUERY_LOGICAL_CONNECTOR change logical_operator OPERATOR varchar(255);
alter table QUERY_EXPRESSION change QUERY_QUERY_ENTITY_ID QUERY_ENTITY_ID varchar(255);
/* expression */
alter table QUERY_EXPRESSION rename QUERY_BASE_EXPRESSION;
alter table QUERY_BASE_EXPRESSION add column EXPR_TYPE varchar(255);
update QUERY_BASE_EXPRESSION set EXPR_TYPE = 'expr';

/* operand */
alter table QUERY_EXPRESSION_OPERAND rename QUERY_OPERAND;
alter table QUERY_OPERAND add column OPND_TYPE varchar(255);

update query_operand set OPND_TYPE='rule' where identifier in (select identifier from query_rule);
update query_operand set OPND_TYPE='expr' where identifier in (select identifier from query_expressionid);

/* rule */
create table QUERY_RULE_COND (RULE_ID bigint not null, CONDITION_ID bigint not null, POSITION integer not null, primary key (RULE_ID, POSITION));

insert into QUERY_RULE_COND(RULE_ID, CONDITION_ID, POSITION) 
(select rule.identifier, cond.identifier, position from  QUERY_RULE rule join QUERY_CONDITION cond on rule.identifier = cond.query_rule_id);
alter table QUERY_CONDITION drop column query_rule_id;
alter table QUERY_CONDITION drop column position;
drop table QUERY_RULE;

/* sub expr opnd*/
alter table QUERY_EXPRESSIONID rename QUERY_SUBEXPR_OPERAND;
alter table QUERY_SUBEXPR_OPERAND add column EXPRESSION_ID bigint;

update QUERY_SUBEXPR_OPERAND sub set EXPRESSION_ID =
(select expr.identifier from QUERY_BASE_EXPRESSION expr 
where expr.QUERY_EXPRESSIONID_ID = sub.identifier);

create table tmp_expressionId (select * from QUERY_SUBEXPR_OPERAND);
/* incorrect rows */
delete from QUERY_SUBEXPR_OPERAND where identifier in (select identifier from QUERY_OPERAND where position is null);
delete from QUERY_OPERAND where position is null;

/* expression */
create table QUERY_EXPRESSION (IDENTIFIER bigint not null, IS_IN_VIEW BOOLEAN, IS_VISIBLE BOOLEAN, UI_EXPR_ID integer, QUERY_ENTITY_ID bigint, primary key (IDENTIFIER));
insert into QUERY_EXPRESSION(IDENTIFIER, IS_IN_VIEW, IS_VISIBLE, QUERY_ENTITY_ID) 
(select IDENTIFIER, IS_IN_VIEW, IS_VISIBLE, QUERY_ENTITY_ID from QUERY_BASE_EXPRESSION);
update QUERY_EXPRESSION expr set UI_EXPR_ID =
(select SUB_EXPRESSION_ID from tmp_expressionId sub where sub.expression_id = expr.identifier);
alter table QUERY_SUBEXPR_OPERAND drop column SUB_EXPRESSION_ID;
alter table QUERY_BASE_EXPRESSION drop column QUERY_EXPRESSIONID_ID;
alter table QUERY_BASE_EXPRESSION drop column IS_IN_VIEW;
alter table QUERY_BASE_EXPRESSION drop column IS_VISIBLE;
alter table QUERY_BASE_EXPRESSION drop column QUERY_ENTITY_ID;

create table QUERY_BASE_EXPR_OPND (BASE_EXPRESSION_ID bigint not null, OPERAND_ID bigint not null, POSITION integer not null, primary key (BASE_EXPRESSION_ID, POSITION));
insert into QUERY_BASE_EXPR_OPND(BASE_EXPRESSION_ID, OPERAND_ID, POSITION)
(select base.identifier, opnd.identifier, position 
from QUERY_BASE_EXPRESSION base join QUERY_OPERAND opnd on base.identifier = opnd.QUERY_EXPRESSION_ID);

alter table QUERY_OPERAND drop column QUERY_EXPRESSION_ID;
alter table QUERY_OPERAND drop column POSITION;

create table QUERY_CONSTRAINT_TO_EXPR (CONSTRAINT_ID bigint not null, EXPRESSION_ID bigint not null unique, primary key (CONSTRAINT_ID, EXPRESSION_ID));
insert into QUERY_CONSTRAINT_TO_EXPR (CONSTRAINT_ID, EXPRESSION_ID)
(select c.identifier, expr.identifier 
from QUERY_BASE_EXPRESSION expr join QUERY_CONSTRAINTS c on expr.QUERY_CONSTRAINT_ID = c.identifier);

alter table QUERY_BASE_EXPRESSION drop column QUERY_CONSTRAINT_ID;

/* graph */
create table COMMONS_GRAPH (IDENTIFIER bigint not null auto_increment, primary key (IDENTIFIER));
create table COMMONS_GRAPH_EDGE (IDENTIFIER bigint not null auto_increment, SOURCE_VERTEX_CLASS varchar(255), SOURCE_VERTEX_ID bigint, TARGET_VERTEX_CLASS varchar(255), TARGET_VERTEX_ID bigint, EDGE_CLASS varchar(255), EDGE_ID bigint, primary key (IDENTIFIER));
create table COMMONS_GRAPH_TO_EDGES (GRAPH_ID bigint not null, EDGE_ID bigint not null unique, primary key (GRAPH_ID, EDGE_ID));
create table COMMONS_GRAPH_TO_VERTICES (GRAPH_ID bigint not null, VERTEX_CLASS varchar(255), VERTEX_ID bigint);

alter table COMMONS_GRAPH add column JOIN_GRAPH_ID bigint;
insert into COMMONS_GRAPH(JOIN_GRAPH_ID) (select identifier from QUERY_JOIN_GRAPH);
alter table QUERY_JOIN_GRAPH add column COMMONS_GRAPH_ID bigint;

update QUERY_JOIN_GRAPH joinGraph set COMMONS_GRAPH_ID = 
(select cg.identifier from COMMONS_GRAPH cg where cg.JOIN_GRAPH_ID = joinGraph.identifier);
alter table COMMONS_GRAPH drop column JOIN_GRAPH_ID;

insert into commons_graph_to_vertices (graph_id, vertex_class, vertex_id)
(select distinct cg.identifier, 'edu.wustl.common.querysuite.queryobject.impl.Expression', sub.expression_id
from COMMONS_GRAPH cg 
join QUERY_JOIN_GRAPH joinGraph on cg.identifier = joinGraph.COMMONS_GRAPH_ID
join QUERY_GRAPH_ENTRY entry on entry.QUERY_JOIN_GRAPH_ID = joinGraph.identifier
join tmp_expressionId sub on (sub.identifier = entry.SOURCE_EXPRESSIONID_ID or sub.identifier = entry.TARGET_EXPRESSIONID_ID));

alter table commons_graph_edge add column OLD_ENTRY_ID bigint;
insert into commons_graph_edge (SOURCE_VERTEX_CLASS, SOURCE_VERTEX_ID, TARGET_VERTEX_CLASS, TARGET_VERTEX_ID, EDGE_CLASS, EDGE_ID, OLD_ENTRY_ID)
(select 'edu.wustl.common.querysuite.queryobject.impl.Expression', srcExpr.expression_id,
'edu.wustl.common.querysuite.queryobject.impl.Expression', targetExpr.expression_id,
'edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation', assoc.identifier,
entry.identifier
from  COMMONS_GRAPH cg 
join QUERY_JOIN_GRAPH joinGraph on cg.identifier = joinGraph.COMMONS_GRAPH_ID
join QUERY_GRAPH_ENTRY entry on entry.QUERY_JOIN_GRAPH_ID = joinGraph.identifier
join tmp_expressionId srcExpr on (srcExpr.identifier = entry.SOURCE_EXPRESSIONID_ID)
join tmp_expressionId targetExpr on (targetExpr.identifier = entry.TARGET_EXPRESSIONID_ID)
join QUERY_INTRA_MODEL_ASSOCIATION assoc on (assoc.identifier = entry.QUERY_MODEL_ASSOCIATION_ID));

insert into commons_graph_edge (SOURCE_VERTEX_CLASS, SOURCE_VERTEX_ID, TARGET_VERTEX_CLASS, TARGET_VERTEX_ID, EDGE_CLASS, EDGE_ID, OLD_ENTRY_ID)
(select 'edu.wustl.common.querysuite.queryobject.impl.Expression', srcExpr.expression_id,
'edu.wustl.common.querysuite.queryobject.impl.Expression', targetExpr.expression_id,
'edu.wustl.common.querysuite.metadata.associations.impl.InterModelAssociation', assoc.identifier,
entry.identifier
from  COMMONS_GRAPH cg 
join QUERY_JOIN_GRAPH joinGraph on cg.identifier = joinGraph.COMMONS_GRAPH_ID
join QUERY_GRAPH_ENTRY entry on entry.QUERY_JOIN_GRAPH_ID = joinGraph.identifier
join tmp_expressionId srcExpr on (srcExpr.identifier = entry.SOURCE_EXPRESSIONID_ID)
join tmp_expressionId targetExpr on (targetExpr.identifier = entry.TARGET_EXPRESSIONID_ID)
join QUERY_INTER_MODEL_ASSOCIATION assoc on (assoc.identifier = entry.QUERY_MODEL_ASSOCIATION_ID));

insert into commons_graph_to_edges (graph_id, edge_id)
(select cg.identifier, edge.identifier
from COMMONS_GRAPH cg 
join QUERY_JOIN_GRAPH joinGraph on cg.identifier = joinGraph.COMMONS_GRAPH_ID
join QUERY_GRAPH_ENTRY entry on entry.QUERY_JOIN_GRAPH_ID = joinGraph.identifier
join commons_graph_edge edge on edge.old_entry_id = entry.identifier);

alter table commons_graph_edge drop column OLD_ENTRY_ID;
drop table query_graph_entry;
/* connectors */
create table QUERY_BASEEXPR_TO_CONNECTORS (BASE_EXPRESSION_ID bigint not null, CONNECTOR_ID bigint not null, POSITION integer not null, primary key (BASE_EXPRESSION_ID, POSITION));
alter table QUERY_LOGICAL_CONNECTOR rename QUERY_CONNECTOR;
insert into QUERY_BASEEXPR_TO_CONNECTORS(BASE_EXPRESSION_ID, CONNECTOR_ID, POSITION)
(select expr.identifier, conn.identifier, position 
from QUERY_BASE_EXPRESSION expr join QUERY_CONNECTOR conn on conn.QUERY_EXPRESSION_ID = expr.identifier);
alter table QUERY_CONNECTOR drop column QUERY_EXPRESSION_ID;
alter table QUERY_CONNECTOR drop column POSITION;

/* parameterized conditions */
create table QUERY_PARAMETER (IDENTIFIER bigint not null auto_increment, NAME varchar(255), OBJECT_CLASS varchar(255), OBJECT_ID bigint, primary key (IDENTIFIER));
create table QUERY_TO_PARAMETERS (QUERY_ID bigint not null, PARAMETER_ID bigint not null unique, POSITION integer not null, primary key (QUERY_ID, POSITION));

insert into QUERY_PARAMETER(NAME, OBJECT_CLASS, OBJECT_ID)
(select CONDITION_NAME, 'edu.wustl.common.querysuite.queryobject.impl.Condition', identifier
from query_parameterized_condition);

insert into QUERY_TO_PARAMETERS(query_id, parameter_id, position)
(select query.identifier, param.identifier, condition_index
from query_parameterized_query query 
join query q on query.identifier = q.identifier
join query_constraints c on q.constraints_id = c.identifier
join QUERY_CONSTRAINT_TO_EXPR cToEx on cToEx.CONSTRAINT_ID = c.identifier
join QUERY_EXPRESSION ex on ex.identifier = cToEx.EXPRESSION_ID
join QUERY_BASE_EXPR_OPND baseOpnd on baseOpnd.BASE_EXPRESSION_ID = ex.identifier
join query_operand opnd on opnd.identifier = baseOpnd.operand_id
join query_rule_cond ruleCond on opnd.identifier = ruleCond.rule_id
join query_parameterized_condition cond on cond.identifier = ruleCond.condition_id
join QUERY_PARAMETER param on param.object_id = cond.identifier);

drop table query_parameterized_condition;

/* output attributes */
alter table query_output_attribute add column EXPRESSION_ID bigint;
update query_output_attribute outAttr set EXPRESSION_ID = 
(select sub.expression_id 
from tmp_expressionId sub where sub.identifier = outAttr.expressionid_id);
alter table query_output_attribute drop column EXPRESSIONID_ID;

drop table tmp_expressionId;
/* new tables */
create table QUERY_TO_OUTPUT_TERMS (QUERY_ID bigint not null, OUTPUT_TERM_ID bigint not null unique, POSITION integer not null, primary key (QUERY_ID, POSITION));
create table QUERY_OUTPUT_TERM (IDENTIFIER bigint not null auto_increment, NAME varchar(255), TIME_INTERVAL varchar(255), TERM_ID bigint, primary key (IDENTIFIER));
create table QUERY_FORMULA_RHS (CUSTOM_FORMULA_ID bigint not null, RHS_TERM_ID bigint not null, POSITION integer not null, primary key (CUSTOM_FORMULA_ID, POSITION));
create table QUERY_CUSTOM_FORMULA (IDENTIFIER bigint not null, OPERATOR varchar(255), LHS_TERM_ID bigint, primary key (IDENTIFIER));
create table QUERY_ARITHMETIC_OPERAND (IDENTIFIER bigint not null, LITERAL varchar(255), TERM_TYPE varchar(255), DATE_LITERAL date, TIME_INTERVAL varchar(255), DE_ATTRIBUTE_ID bigint, EXPRESSION_ID bigint, primary key (IDENTIFIER));

/* new indexes */
alter table COMMONS_GRAPH_TO_EDGES add index FKA6B0D8BAA0494B1D (GRAPH_ID), add constraint FKA6B0D8BAA0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table COMMONS_GRAPH_TO_EDGES add index FKA6B0D8BAFAEF80D (EDGE_ID), add constraint FKA6B0D8BAFAEF80D foreign key (EDGE_ID) references COMMONS_GRAPH_EDGE (IDENTIFIER);
alter table COMMONS_GRAPH_TO_VERTICES add index FK2C4412F5A0494B1D (GRAPH_ID), add constraint FK2C4412F5A0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table QUERY add index FK49D20A89E2FD9C7 (CONSTRAINTS_ID), add constraint FK49D20A89E2FD9C7 foreign key (CONSTRAINTS_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0BD635BD31 (IDENTIFIER), add constraint FK262AEB0BD635BD31 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0BE92C814D (EXPRESSION_ID), add constraint FK262AEB0BE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_BASEEXPR_TO_CONNECTORS add index FK3F0043482FCE1DA7 (CONNECTOR_ID), add constraint FK3F0043482FCE1DA7 foreign key (CONNECTOR_ID) references QUERY_CONNECTOR (IDENTIFIER);
alter table QUERY_BASEEXPR_TO_CONNECTORS add index FK3F00434848BA6890 (BASE_EXPRESSION_ID), add constraint FK3F00434848BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_BASE_EXPR_OPND add index FKAE67EAF0712A4C (OPERAND_ID), add constraint FKAE67EAF0712A4C foreign key (OPERAND_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_BASE_EXPR_OPND add index FKAE67EA48BA6890 (BASE_EXPRESSION_ID), add constraint FKAE67EA48BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_CONDITION_VALUES add index FK9997379D6458C2E7 (CONDITION_ID), add constraint FK9997379D6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_CONSTRAINTS add index FKE364FCFF1C7EBF3B (QUERY_JOIN_GRAPH_ID), add constraint FKE364FCFF1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_CONSTRAINT_TO_EXPR add index FK2BD705CEA0A5F4C0 (CONSTRAINT_ID), add constraint FK2BD705CEA0A5F4C0 foreign key (CONSTRAINT_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_CONSTRAINT_TO_EXPR add index FK2BD705CEE92C814D (EXPRESSION_ID), add constraint FK2BD705CEE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_CUSTOM_FORMULA add index FK5C0EEAEFBE674D45 (LHS_TERM_ID), add constraint FK5C0EEAEFBE674D45 foreign key (LHS_TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_CUSTOM_FORMULA add index FK5C0EEAEF12D455EB (IDENTIFIER), add constraint FK5C0EEAEF12D455EB foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F40EB75D4 (IDENTIFIER), add constraint FK1B473A8F40EB75D4 foreign key (IDENTIFIER) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F635766D8 (QUERY_ENTITY_ID), add constraint FK1B473A8F635766D8 foreign key (QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY (IDENTIFIER);
alter table QUERY_FORMULA_RHS add index FKAE90F94D3BC37DCB (RHS_TERM_ID), add constraint FKAE90F94D3BC37DCB foreign key (RHS_TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_FORMULA_RHS add index FKAE90F94D9A0B7164 (CUSTOM_FORMULA_ID), add constraint FKAE90F94D9A0B7164 foreign key (CUSTOM_FORMULA_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_INTER_MODEL_ASSOCIATION add index FKD70658D15F5AB67E (IDENTIFIER), add constraint FKD70658D15F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_INTRA_MODEL_ASSOCIATION add index FKF1EDBDD35F5AB67E (IDENTIFIER), add constraint FKF1EDBDD35F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_JOIN_GRAPH add index FK2B41B5D09DBC4D94 (COMMONS_GRAPH_ID), add constraint FK2B41B5D09DBC4D94 foreign key (COMMONS_GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75604D4BDA (PARAMETERIZED_QUERY_ID), add constraint FK22C9DB75604D4BDA foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75E92C814D (EXPRESSION_ID), add constraint FK22C9DB75E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_OUTPUT_TERM add index FK13C8A3D388C86B0D (TERM_ID), add constraint FK13C8A3D388C86B0D foreign key (TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_PARAMETERIZED_QUERY add index FKA272176B76177EFE (IDENTIFIER), add constraint FKA272176B76177EFE foreign key (IDENTIFIER) references QUERY (IDENTIFIER);
alter table QUERY_RULE_COND add index FKC32D37AE6458C2E7 (CONDITION_ID), add constraint FKC32D37AE6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_RULE_COND add index FKC32D37AE39F0A10D (RULE_ID), add constraint FKC32D37AE39F0A10D foreign key (RULE_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_SUBEXPR_OPERAND add index FK2BF760E832E875C8 (IDENTIFIER), add constraint FK2BF760E832E875C8 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_SUBEXPR_OPERAND add index FK2BF760E8E92C814D (EXPRESSION_ID), add constraint FK2BF760E8E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_TO_OUTPUT_TERMS add index FK8A70E2565E5B9430 (OUTPUT_TERM_ID), add constraint FK8A70E2565E5B9430 foreign key (OUTPUT_TERM_ID) references QUERY_OUTPUT_TERM (IDENTIFIER);
alter table QUERY_TO_OUTPUT_TERMS add index FK8A70E25691051647 (QUERY_ID), add constraint FK8A70E25691051647 foreign key (QUERY_ID) references QUERY (IDENTIFIER);
alter table QUERY_TO_PARAMETERS add index FK8060DAD7F84B9027 (PARAMETER_ID), add constraint FK8060DAD7F84B9027 foreign key (PARAMETER_ID) references QUERY_PARAMETER (IDENTIFIER);
alter table QUERY_TO_PARAMETERS add index FK8060DAD739F0A314 (QUERY_ID), add constraint FK8060DAD739F0A314 foreign key (QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);

/* enum values modified */
/* rel oper */
update query_condition set RELATIONAL_OPERATOR = 'NotEquals' where RELATIONAL_OPERATOR = 'Not Equals';
update query_condition set RELATIONAL_OPERATOR = 'IsNull' where RELATIONAL_OPERATOR = 'Is Null';
update query_condition set RELATIONAL_OPERATOR = 'IsNotNull' where RELATIONAL_OPERATOR = 'Is Not Null';
update query_condition set RELATIONAL_OPERATOR = 'LessThan' where RELATIONAL_OPERATOR = 'Less than';
update query_condition set RELATIONAL_OPERATOR = 'LessThanOrEquals' where RELATIONAL_OPERATOR = 'Less than or Equal to';
update query_condition set RELATIONAL_OPERATOR = 'GreaterThan' where RELATIONAL_OPERATOR = 'Greater than';
update query_condition set RELATIONAL_OPERATOR = 'GreaterThanOrEquals' where RELATIONAL_OPERATOR = 'Greater than or Equal to';
update query_condition set RELATIONAL_OPERATOR = 'StartsWith' where RELATIONAL_OPERATOR = 'Starts With';
update query_condition set RELATIONAL_OPERATOR = 'EndsWith' where RELATIONAL_OPERATOR = 'Ends With';
update query_condition set RELATIONAL_OPERATOR = 'NotIn' where RELATIONAL_OPERATOR = 'Not In';
/* logical oper */
update query_connector set OPERATOR = 'And' where OPERATOR = 'AND';
update query_connector set OPERATOR = 'Or' where OPERATOR = 'OR';
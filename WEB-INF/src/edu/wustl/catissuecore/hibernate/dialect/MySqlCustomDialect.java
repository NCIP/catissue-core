package edu.wustl.catissuecore.hibernate.dialect;

import java.sql.Types;
import org.hibernate.Hibernate;

import org.hibernate.dialect.MySQLDialect;

public class MySqlCustomDialect extends MySQLDialect {

	public MySqlCustomDialect()
	{
	   super();
      registerHibernateType(Types.LONGVARCHAR, Hibernate.CLOB.getName());
     }
}
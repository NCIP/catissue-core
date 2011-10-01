/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import edu.wustl.catissuecore.util.IStringIdentifiable;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public abstract class BaseComparator {

	private static final Logger logger = Logger
			.getCommonLogger(BaseComparator.class);

	/**
	 * @param oldObj
	 * @param newObj
	 * @param results
	 * @throws RuntimeException
	 */
	protected void compare(AbstractDomainObject oldObj,
			AbstractDomainObject newObj,
			List<IDomainObjectComparisonResult> results)
			throws RuntimeException {
		for (final Field field : getFields()) {
			try {
				Object oldValue = getProperty(oldObj,
						field);
				Object newValue = getProperty(newObj,
						field);
				final boolean equals = equals(oldValue, newValue);
				final String oldValueStr = asString(oldValue);
				final String newValueStr = asString(newValue);
				results.add(new IDomainObjectComparisonResult() {
					@Override
					public boolean isDifferent() {
						return !equals;
					}

					@Override
					public Object getOldValue() {
						return oldValueStr;
					}

					@Override
					public Object getNewValue() {
						return newValueStr;
					}

					@Override
					public String getFieldName() {
						return field.getName();
					}
				});
			} catch (Exception e) {
				logger.error(e.toString(), e);
				throw new RuntimeException(e);
			}
		}
	}

	private Object getProperty(Object obj, Field field)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		try {
			return PropertyUtils.getProperty(obj, field.getProperty());
		} catch (IllegalArgumentException e) {
			// using exception for control flow. guilty as charged. had to do it because of nested properties.
			return null;
		}
	}

	public abstract List<Field> getFields();

	protected boolean equals(Object o, Object n) {
		if (o instanceof Collection || n instanceof Collection) {
			return equals((Collection) o, (Collection) n);
		}
		return ObjectUtils.equals(o, n);
	}

	protected boolean equals(Collection c1, Collection c2) {
		// this is naive, but enough for our current needs.
		return ObjectUtils.equals(asString(c1), asString(c2));

	}

	protected String asString(Object obj) {
		if (obj instanceof Collection) {
			return asString((Collection) obj);
		}
		if (obj instanceof Date) {
			return DateFormatUtils.format((Date) obj,
					DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		}
		return obj != null ? obj.toString() : "";
	}

	protected String asString(Collection coll) {
		Collection c = new TreeSet();
		if (coll != null)
			c.addAll(coll);
		final String tail = ", ";
		StringBuilder sb = new StringBuilder();
		if (c != null) {
			for (Object o : c) {
				if (o != null && !(o instanceof IStringIdentifiable)) {
					throw new RuntimeException(
							"Currently, only instances of edu.wustl.catissuecore.util.IStringIdentifiable are supported in edu.wustl.catissuecore.bizlogic.ccts.BaseComparator.asString(Collection)");
				}
				IStringIdentifiable identifiable = (IStringIdentifiable) o;
				sb.append(identifiable != null ? identifiable.getAsString()
						: "null");

				sb.append(tail);
			}
		}
		return sb.toString().replaceFirst(tail + "$", "");
	}

	protected static final class Field {
		private String name;
		private String property;

		public Field(String name, String property) {
			super();
			this.name = name;
			this.property = property;
		}

		/**
		 * @return the name
		 */
		public final String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public final void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the property
		 */
		public final String getProperty() {
			return property;
		}

		/**
		 * @param property
		 *            the property to set
		 */
		public final void setProperty(String property) {
			this.property = property;
		}

	}

}

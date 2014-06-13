
package com.krishagni.catissueplus.core.labelgenerator;

public interface LabelGenerator<T> {

	public String generateLabel(String labelFormat, T object);

}

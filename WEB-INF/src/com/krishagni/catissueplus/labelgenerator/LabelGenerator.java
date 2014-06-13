
package com.krishagni.catissueplus.labelgenerator;

public interface LabelGenerator<T> {

	public String generateLabel(String labelFormat, T object);

}

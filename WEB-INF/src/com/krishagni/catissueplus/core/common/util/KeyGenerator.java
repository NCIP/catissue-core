
package com.krishagni.catissueplus.core.common.util;

public class KeyGenerator {

	private Long id;

	private String key;

	private Long value = 0L;

	private String keyType;

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Long increment() {

		return ++value;
	}

}

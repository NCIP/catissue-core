package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.proxy.HibernateProxyHelper;

public class BaseEntity {
	protected Long id;
	
	protected transient List<Runnable> onSaveProcs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Runnable> getOnSaveProcs() {
		return onSaveProcs;
	}

	public void setOnSaveProcs(List<Runnable> onSaveProcs) {
		this.onSaveProcs = onSaveProcs;
	}

	public void addOnSaveProc(Runnable onSaveProc) {
		if (onSaveProcs == null) {
			onSaveProcs = new ArrayList<>();
		}

		onSaveProcs.add(onSaveProc);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((getId() == null) ? 0 : getId().hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null	|| 
			getClass() != HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) {
			return false;
		}

		BaseEntity other = (BaseEntity) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	public boolean sameAs(Object obj) {
		return equals(obj);
	}
}
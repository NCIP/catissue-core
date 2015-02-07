package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.proxy.HibernateProxyHelper;

public class BaseEntity {	
    protected Long id;

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    @Override
    public int hashCode() {
            final int prime = 31;
            return prime + ((id == null) ? 0 : id.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
            if (this == obj) {
                    return true;
            }

            if (obj == null ||
                    getClass() != HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) {
                    return false;
            }

            BaseEntity other = (BaseEntity) obj;
            return id != null ? id.equals(other.getId()) : false;
    }
}

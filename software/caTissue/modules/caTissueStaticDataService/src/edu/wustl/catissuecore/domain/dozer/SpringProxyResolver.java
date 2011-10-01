package edu.wustl.catissuecore.domain.dozer;

import org.dozer.util.DefaultProxyResolver;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;

/**
 * @author Ion C. Olaru
 *         Date: 7/22/11 - 11:49 AM
 */
public class SpringProxyResolver extends DefaultProxyResolver {

    private static SpringProxyResolver instance;
    /**
     * Sort of a singleton
     * Multiple instances are allowed since this is accessed by Dozer as well.
     * */
    public static SpringProxyResolver getInstance() {
        if (instance == null) instance = new SpringProxyResolver();
        return instance;
    }

    @Override
    public <T> T unenhanceObject(T object) {
        T t = object;
        if (object instanceof Advised) {
            Advised sp = (Advised)object;
            Class tc = sp.getTargetClass();
            try {
                t = (T)sp.getTargetSource().getTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.unenhanceObject(t);
    }

    @Override
    public Class<?> getRealClass(Class<?> clazz) {
        Class klass = super.getRealClass(clazz);
        return klass;
    }

}

package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.factory.AbstractTransformerFactory;
import edu.wustl.common.factory.ITransformerFactory;
import edu.wustl.common.util.ClassesInPackages;

public class TransformerFactory extends AbstractTransformerFactory {

    private static ITransformerFactory factory = new TransformerFactory();

    private TransformerFactory() {
        super(getClasses());
    }

    /**
     * TODO BETTER THIS.
     */
    public static ITransformerFactory getInstance() {
        return factory;
    }

    /**
     * Thou mockest me; but laugh not, for it is I that allows thee to mock
     * me...
     *
     * @param factory typically a mock for testing.
     */
    static synchronized void setInstance(ITransformerFactory factory) {
        TransformerFactory.factory = factory;
    }

    @SuppressWarnings("unchecked")
    private static List<Class<? extends UIDomainTransformer>> getClasses() {
        List<Class<? extends UIDomainTransformer>> res = new ArrayList<Class<? extends UIDomainTransformer>>();
        // TODO is this OK?
        List<Class<?>> classes = ClassesInPackages.classesInPackages(TransformerFactory.class.getPackage().getName());

        for (Class<?> c : classes) {
            if (isTransformer(c))
                res.add((Class<? extends UIDomainTransformer>) c);
        }
        return res;
    }

    // public static void main(String[] args) {
    // for (Map.Entry e : new
    // TransformerFactory().getAllTransformers().entrySet()) {
    // System.out.println(e);
    // }
    // }
}

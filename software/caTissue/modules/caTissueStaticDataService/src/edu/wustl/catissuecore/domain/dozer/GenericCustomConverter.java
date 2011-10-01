package edu.wustl.catissuecore.domain.dozer;

import edu.wustl.catissuecore.domain.converter.GenericConverter;
import edu.wustl.catissuecore.domain.service.WAPIUtility;
import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.dozer.DozerBeanMapperSingletonWrapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Ion C. Olaru
 *         Date: 7/29/11 - 2:42 PM
 */
public class GenericCustomConverter implements ConfigurableCustomConverter {

    private String parameters;
    private Map<String, String> params = new HashMap<String, String>();

    private static final String CLASS_NAME = "CLASS_NAME";
    private static final String CLASS_NAME2 = "CLASS_NAME2";

    private static Logger log = Logger.getLogger(GenericCustomConverter.class);

    public void setParameter(String parameters) {
        this.parameters = parameters;
        StringTokenizer st = new StringTokenizer(this.parameters, ",");
        byte i = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            switch (i) {
                case 0: params.put(CLASS_NAME, token); break;
                case 1: params.put(CLASS_NAME2, token); break;
            }
            i++;
            log.debug(">>> Found token: " + token);
        }
    }

    public Object convert(Object destination, Object source, Class<?> destClass, Class<?> sourceClass) {
        // log.debug(String.format(">>> Custom converting [%s] to [%s]", sourceClass, destClass));
        System.out.println(String.format(">>> Custom converting [%s] to [%s]", sourceClass, destClass));

        if (sourceClass.getCanonicalName().indexOf(".ws") >= 0) {

            // 1 Step - Call Dozer post mapper
            String postMapper = params.get(CLASS_NAME) + "PostMapper";
            log.debug(">>> Using postMapper: " + postMapper);

            DozerBeanMapperSingletonWrapper.getInstance().map(source, destination, postMapper);

            // 2 Step - Nullify Ids
            WAPIUtility.nullifyFieldValue(destination, "setId", "getId", Long.class, null);

            // 3 Step - Convert certain Collections to HashSet
            GenericCollectionConverter.convertCollectionTypes(destination);

            // 4 Step - add Root object ref to certain children (Ex: add Participant to Participant's race object)
            GenericCollectionConverter.adjustReference(destination);

        }
        return destination;
    }
}

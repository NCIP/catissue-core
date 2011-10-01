package edu.wustl.catissuecore.actionForm;

import java.util.Map;

public interface IAddOrderDetailsForm {
    /**
     * @return name of distribution protocol
     */
    public abstract String getDistrbutionProtocol();

    /**
     * @return object of OrderForm
     */
    public abstract OrderForm getOrderForm();

    /**
     * @return map values
     */
    public abstract Map getValues();

}
package edu.wustl.catissuecore.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import antlr.collections.List;

import edu.wustl.catissuecore.domain.Specimen;

/**
 * @author abhijit_naik
 *
 */
public interface MultipleSpecimenList {

	public LinkedHashMap<String, SpecimenDTO> getSpecimenList(String eventKey);
}

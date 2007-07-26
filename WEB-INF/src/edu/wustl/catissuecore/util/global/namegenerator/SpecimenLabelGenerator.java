package edu.wustl.catissuecore.util.global.namegenerator;

import java.util.List;

/**
 * Interface for Specimen label generation.
 * During JBOSS Startup CatissueCoreServeletContextlistener class get instance of SpecimenGenerator
 * which inturn retrive currentlable from database and set value of current lable in a variable. 
 * @author virender_mehta
 *
 */
public interface SpecimenLabelGenerator
{
	/**
	 * Returns next available specimen label
	 * @param  obj
	 * @return Specimen label
	 */
	public String getNextAvailableSpecimenlabel(Object obj);
	
	/**
	 * Returns next available derived specimen label
	 * @param  obj
	 * @return Derive Specimen label
	 */
	public String getNextAvailableDeriveSpecimenlabel(Object obj);
	
	/**
	 * Returns next available aliquot specimen labels 
	 * @param  obj
	 * @return Aliquot Specimen label collection
	 */
	public List<String> getNextAvailableAliquotSpecimenlabel(Object specimenId, int count);
	
	/**
	 * Returns the next 'count' number of specimen labels 
	 * @param  obj input values
	 * @return Collection of Specimen label 
	 */
	public List<String> getNextAvailableSpecimenlabel(Object obj, int count);
}
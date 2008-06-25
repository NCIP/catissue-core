package edu.wustl.catissuecore.bean;

import java.util.List;


public interface SpecimenDetailsInfo {

	public abstract String getSelectedSpecimenId();

	public abstract List<GenericSpecimen> getSpecimenList();

	public abstract List<GenericSpecimen> getAliquotList();

	public abstract List<GenericSpecimen> getDerivedList();

	public abstract boolean getShowCheckBoxes();

	public abstract boolean getShowbarCode();

	public abstract boolean getShowLabel();

}
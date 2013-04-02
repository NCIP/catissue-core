/**
 */

package edu.wustl.catissuecore.dto;

import java.util.HashSet;
import java.util.Set;


public class OrderGrid {
	
	private Set<RowDTO> rowDTOs;
	
	public OrderGrid()
	{
		rowDTOs = new HashSet<RowDTO>();
	}
	public void addRow(RowDTO rowDTO)
	{
		rowDTOs.add(rowDTO);
	}
	/**
	 * @return the attributes
	 */
	public Set<RowDTO> getRowDTOs() {
		return rowDTOs;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Set<RowDTO> rowDTOs) {
		this.rowDTOs = rowDTOs;
	}
	
}

/**
 */

package edu.wustl.catissuecore.dto;

import java.util.ArrayList;
import java.util.List;


public class RowDTO  implements Comparable<RowDTO> {
	
	private List<String> cells;
	private String id;
	
	public RowDTO()
	{
		cells = new ArrayList<String>();
	}
	public void addCell(String cell)
	{
		cells.add(cell);
	}
	/**
	 * @return the attributes
	 */
	public List<String> getCells() {
		return cells;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int compareTo(RowDTO other) {
        return id.compareTo(other.getId());
    }
}

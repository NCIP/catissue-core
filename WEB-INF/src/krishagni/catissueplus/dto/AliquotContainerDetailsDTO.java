
package krishagni.catissueplus.dto;

import java.util.ArrayList;
import java.util.List;

public class AliquotContainerDetailsDTO
{

	public String containerName;
	public List<String> position1 = new ArrayList<String>();
	public List<String> position2 = new ArrayList<String>();
	public int dimension1;
	public int dimension2;
	public Long emptyPositionCount;
	public Long containerId;

	@Override
	public boolean equals(Object aliquotContainerDetailsDTO)
	{
		// TODO Auto-generated method stub
		return containerName
				.equals(((AliquotContainerDetailsDTO) aliquotContainerDetailsDTO).containerName);
	}

}

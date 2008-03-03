package edu.wustl.catissuecore.util;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.CollectionProtocol;

public class CollectionProtocolSeqComprator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		if(arg0 instanceof CollectionProtocol && arg1 instanceof CollectionProtocol)
		{
			CollectionProtocol cp1 = (CollectionProtocol)arg0;
			CollectionProtocol cp2 = (CollectionProtocol)arg1;
			Integer seq1 = cp1.getSequenceNumber();
			Integer seq2 = cp2.getSequenceNumber();
			
			if(seq1 != null && seq2 != null)
			{
				return seq1.compareTo(seq2);
			}
			if(seq1 == null && seq2 == null)
				return 0;
			
			if(seq1 ==null)
				return 1;
			if(seq2 == null)
				return -1;
						
		}
		// TODO Auto-generated method stub
		return 0;
	}

}



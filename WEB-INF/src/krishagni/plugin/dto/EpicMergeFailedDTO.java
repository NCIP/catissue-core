package krishagni.plugin.dto;

import krishagni.catissueplus.dto.ParticipantDetailsDTO;


public class EpicMergeFailedDTO
{
    private String oldPartSourceID;
    private String partSourceID;
    private ParticipantDetailsDTO oldParticipantDetailsDTO;
    private ParticipantDetailsDTO participantDetailsDTO;
    private String partSource;
    private String changeType;
 
        
    public String getOldPartSourceID()
    {
        return oldPartSourceID;
    }
    
    public void setOldPartSourceID(String oldPartSourceID)
    {
        this.oldPartSourceID = oldPartSourceID;
    }
    
    public String getPartSourceID()
    {
        return partSourceID;
    }
    
    public void setPartSourceID(String partSourceID)
    {
        this.partSourceID = partSourceID;
    }


    public ParticipantDetailsDTO getParticipantDetailsDTO()
    {
        return participantDetailsDTO;
    }

    
    public void setParticipantDetailsDTO(ParticipantDetailsDTO participantDetailsDTO)
    {
        this.participantDetailsDTO = participantDetailsDTO;
    }

    public String getChangeType()
    {
        return changeType;
    }

    
    public void setChangeType(String changeType)
    {
        this.changeType = changeType;
    }

    public ParticipantDetailsDTO getOldParticipantDetailsDTO()
    {
        return oldParticipantDetailsDTO;
    }

    
    public void setOldParticipantDetailsDTO(ParticipantDetailsDTO oldParticipantDetailsDTO)
    {
        this.oldParticipantDetailsDTO = oldParticipantDetailsDTO;
    }

    
    public String getPartSource()
    {
        return partSource;
    }

    
    public void setPartSource(String partSource)
    {
        this.partSource = partSource;
    }

        
    
   
    
    
}

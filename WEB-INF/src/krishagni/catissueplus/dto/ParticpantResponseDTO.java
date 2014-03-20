package krishagni.catissueplus.dto;

import krishagni.catissueplus.dto.ParticipantDetailsDTO;


public class ParticpantResponseDTO
{
    private ParticipantDetailsDTO  participantDetailsDTO;
    private ParticipantDetailsDTO  mergeFromParticipantDetailsDTO;
    private ParticipantResponseStatusEnum participantResponseStatusEnum;
    private String message;
    
    
    public ParticipantDetailsDTO getMergeFromParticipantDetailsDTO()
    {
        return mergeFromParticipantDetailsDTO;
    }

    
    public void setMergeFromParticipantDetailsDTO(ParticipantDetailsDTO mergeFromParticipantDetailsDTO)
    {
        this.mergeFromParticipantDetailsDTO = mergeFromParticipantDetailsDTO;
    }

    public ParticipantDetailsDTO getParticipantDetailsDTO()
    {
        return participantDetailsDTO;
    }
    
    public void setParticipantDetailsDTO(ParticipantDetailsDTO participantDetailsDTO)
    {
        this.participantDetailsDTO = participantDetailsDTO;
    }
    
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }

    
    public ParticipantResponseStatusEnum getParticipantResponseStatusEnum()
    {
        return participantResponseStatusEnum;
    }


    
    public void setParticipantResponseStatusEnum(ParticipantResponseStatusEnum participantResponseStatusEnum)
    {
        this.participantResponseStatusEnum = participantResponseStatusEnum;
    }
    
}

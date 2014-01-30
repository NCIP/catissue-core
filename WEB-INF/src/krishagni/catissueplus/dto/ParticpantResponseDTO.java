package krishagni.catissueplus.dto;

import krishagni.catissueplus.dto.ParticipantDetailsDTO;


public class ParticpantResponseDTO
{
    public enum StatusEnum {
        ADDED,MODIFIED,ERROR 
    }
    private ParticipantDetailsDTO  participantDetailsDTO;
    private StatusEnum statusEnum;
    private String message;
    
    public ParticipantDetailsDTO getParticipantDetailsDTO()
    {
        return participantDetailsDTO;
    }
    
    public void setParticipantDetailsDTO(ParticipantDetailsDTO participantDetailsDTO)
    {
        this.participantDetailsDTO = participantDetailsDTO;
    }
    
    public StatusEnum getStatusEnum()
    {
        return statusEnum;
    }
    
    public void setStatusEnum(StatusEnum statusEnum)
    {
        this.statusEnum = statusEnum;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}

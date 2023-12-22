package mch.ms.sms.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SendSmsRequestDTO {

    private String message;
    private List<SendSmsNumberRequestDTO> recipient = new ArrayList<>();
    private String test;

}

package mch.ms.sms.service;

import mch.ms.sms.controller.dto.SmsRequestDTO;
import mch.ms.sms.service.dto.SendSmsRequestDTO;
import mch.ms.sms.utilities.Response;

public interface ISmsService {

    Response sendSMS(SmsRequestDTO smsRequestDTO);

    void saveLog(SmsRequestDTO smsRequestDTO, String response);

}

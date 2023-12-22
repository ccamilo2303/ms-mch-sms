package mch.ms.sms.service.impl;

import lombok.extern.slf4j.Slf4j;
import mch.ms.sms.controller.dto.SmsRequestDTO;
import mch.ms.sms.entities.SmsLog;
import mch.ms.sms.repository.SmsLogRepository;
import mch.ms.sms.service.ISmsService;
import mch.ms.sms.service.dto.SendSmsNumberRequestDTO;
import mch.ms.sms.service.dto.SendSmsResponseDTO;
import mch.ms.sms.service.dto.SendSmsRequestDTO;
import mch.ms.sms.utilities.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    private final SmsLogRepository smsLogRepository;
    private final RestTemplate restTemplate;
    private String headerName;
    private String headerValue;

    @Value("${labsmobile.user}")
    private String user;

    @Value("${labsmobile.password}")
    private String password;

    @Value("${labsmobile.url}")
    private String urlLabsMobile;

    @Value("${labsmobile.test}")
    private boolean labsMobileTest;

    public SmsServiceImpl(SmsLogRepository smsLogRepository, RestTemplate restTemplate){
        this.smsLogRepository = smsLogRepository;
        this.restTemplate = restTemplate;
        this.headerName = "Authorization";
        this.headerValue = "Basic";
    }

    private String hash() {
        String credentials = user.concat(":").concat(password);
        return new String(Base64.getEncoder().encode(credentials.getBytes()));
    }

    @Override
    public Response sendSMS(SmsRequestDTO smsRequestDTO) {
        Response responseSendSMS = new Response();
        log.info("[SmsServiceImpl][sendSMS][smsRequestDTO]: {}", smsRequestDTO);
        try{
            if(smsRequestDTO.getCartera().isBlank() || smsRequestDTO.getTelefono().isEmpty() || smsRequestDTO.getMensaje().isBlank()){
                responseSendSMS.setCode(400);
                responseSendSMS.setMessage("Error en el request.");
                return responseSendSMS;
            }

            SendSmsRequestDTO sendSmsRequestDTO = new SendSmsRequestDTO();
            sendSmsRequestDTO.setMessage(smsRequestDTO.getMensaje());
            if(labsMobileTest) sendSmsRequestDTO.setTest("1");


            smsRequestDTO.getTelefono().forEach((telefono) -> {
                SendSmsNumberRequestDTO sendSmsNumberRequestDTO = new SendSmsNumberRequestDTO();
                sendSmsNumberRequestDTO.setMsisdn(telefono);
                sendSmsRequestDTO.getRecipient().add(sendSmsNumberRequestDTO);
            });


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(headerName,headerValue.concat(" ").concat(hash()));

            HttpEntity<SendSmsRequestDTO> httpEntity = new HttpEntity<>(sendSmsRequestDTO, headers);
            ResponseEntity<SendSmsResponseDTO> response = restTemplate.postForEntity(urlLabsMobile, httpEntity, SendSmsResponseDTO.class);

            saveLog(smsRequestDTO, response.getBody().getMessage());

            if(response.getBody().getCode().equals("0")){
                responseSendSMS.setCode(200);
            }else{
                responseSendSMS.setCode(400);
            }
            responseSendSMS.setMessage(response.getBody().getMessage());
            return responseSendSMS;

        }catch (Exception e){
            responseSendSMS.setCode(500);
            responseSendSMS.setMessage("Ha ocurrido un error: ".concat(e.getMessage()));
            return responseSendSMS;
        }
    }

    @Override
    @Transactional
    public void saveLog(SmsRequestDTO smsRequestDTO, String response) {

        smsRequestDTO.getTelefono().forEach((telefono) -> {
            SmsLog smsLog = new SmsLog();
            smsLog.setCartera(smsRequestDTO.getCartera());
            smsLog.setTelefono(telefono);
            smsLog.setMensaje(smsRequestDTO.getMensaje());
            smsLog.setResultado(response);
            smsLog.setFechaEnvio(java.time.LocalDateTime.now());
            smsLogRepository.save(smsLog);
        });

    }
}

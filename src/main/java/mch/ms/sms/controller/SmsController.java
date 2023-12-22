package mch.ms.sms.controller;


import mch.ms.sms.controller.dto.SmsRequestDTO;
import mch.ms.sms.service.ISmsService;
import mch.ms.sms.utilities.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;


@RestController
@RequestMapping("/ms-sms/sms")
public class SmsController {

    @Autowired
    private ISmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<Response> sendSMS(@RequestBody SmsRequestDTO smsRequestDTO) throws URISyntaxException {
        Response response = smsService.sendSMS(smsRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/prueba")
    public ResponseEntity<String> prueba(){
        return new ResponseEntity<>("Prueba", HttpStatus.OK);
    }

}

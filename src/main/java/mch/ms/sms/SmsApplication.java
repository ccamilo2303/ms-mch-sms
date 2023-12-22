package mch.ms.sms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SmsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}

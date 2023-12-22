package mch.ms.sms.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sms_log")
public class SmsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cartera")
    private String cartera;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

}

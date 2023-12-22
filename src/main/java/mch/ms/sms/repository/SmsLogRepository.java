package mch.ms.sms.repository;

import mch.ms.sms.entities.SmsLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsLogRepository extends CrudRepository<SmsLog, Long> {
}

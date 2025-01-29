package com.prx.mercury.scheduler;

import com.prx.mercury.api.v1.service.EmailService;
import com.prx.mercury.constant.DeliveryStatusType;
import com.prx.mercury.jpa.nosql.repository.EmailMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SendEmailScheduler {

    private final Logger logger = LoggerFactory.getLogger(SendEmailScheduler.class);
    private final EmailMessageRepository emailMessageRepository;
    private final EmailService emailService;

    public SendEmailScheduler(EmailMessageRepository emailMessageRepository, EmailService emailService) {
        this.emailMessageRepository = emailMessageRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void sendEmail() {
        logger.debug("Sending email...");
        emailMessageRepository.findByDeliveryStatus(DeliveryStatusType.OPENED)
                .forEach(emailMessageDocument -> {
                    logger.debug("Sending email: {}", emailMessageDocument);
                    emailMessageRepository.save(emailService.sendEmail(emailMessageDocument));
                });
    }

    @Scheduled(fixedRate = 600000)
    public void saveMessageProcessed() {
        logger.debug("Saving message processed...");
        emailMessageRepository.findByDeliveryStatus(DeliveryStatusType.SENT).forEach(emailMessageDocument -> {
            logger.debug("Saving message processed: {}", emailMessageDocument);
            emailService.saveMessageRecord(emailMessageDocument);
            emailMessageRepository.deleteById(emailMessageDocument.id());
            logger.debug("Message processed saved and deleted: {}", emailMessageDocument);
        });
    }


}

package com.prx.mercury.scheduler;

import com.prx.mercury.processor.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SendEmailScheduler {

    private final Logger logger = LoggerFactory.getLogger(SendEmailScheduler.class);
    private final MessageProcessor messageProcessor;

    public SendEmailScheduler(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Scheduled(fixedRate = 60000)
    public void sendEmail() {
        logger.debug("Initiating email sending task...");
        messageProcessor.processMessage();
        logger.debug("Email sending task completed.");
    }

    @Scheduled(fixedRate = 300000)
    public void saveMessageProcessed() {
        logger.debug("Initiating message processed saving task...");
        messageProcessor.updateMessageStatus();
        logger.debug("Message processed saving task completed.");
    }

}

package com.example.demo.listener;

import com.example.demo.dto.EnrollmentCreatedEvent;
import com.example.demo.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EnrollmentNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentNotificationListener.class);
    private final EmailService emailService;

    public EnrollmentNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEnrollment(EnrollmentCreatedEvent e) {
        log.info("Listener triggered for {}", e.studentEmail());
        try {
            emailService.send(
                    e.studentEmail(),
                    "Enrollment confirmed: " + e.courseCode(),
                    "Hello " + e.studentName() + ",\n\n"
                            + "You have been enrolled in " + e.courseTitle()
                            + " (" + e.courseCode() + ").\n\nGood luck!");
        } catch (MailException ex) {
            log.error("Could not send enrollment email to {}", e.studentEmail(), ex);
        }
    }
}

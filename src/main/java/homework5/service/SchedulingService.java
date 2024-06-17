package homework5.service;

import homework5.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@Slf4j
public class SchedulingService {
    private SimpMessagingTemplate simpMessagingTemplate;

    public SchedulingService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(fixedDelay = 2000)
    private void bgColor(){
        Random random = new Random();
        String color = String.format("#%02x%02x%02x", random.nextInt(255), random.nextInt(255), random.nextInt(255));
        simpMessagingTemplate.convertAndSend("/topic/color", new Greeting(color));
        log.info("Send color: " + color);
    }
}

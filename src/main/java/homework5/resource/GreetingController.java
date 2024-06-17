package homework5.resource;

import homework5.domain.Greeting;
import homework5.domain.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Random;

@Controller
@Slf4j
public class GreetingController {
    private SimpMessagingTemplate simpMessagingTemplate;

    public GreetingController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greetingAll(HelloMessage message) throws Exception {
        log.info("Incoming message: " + message);
        Thread.sleep(1000);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @GetMapping("/color")
    @ResponseBody
    public Greeting sendColor(HelloMessage message) {
        log.info("Incoming message: " + message);
        Random random = new Random();
        String color = String.format("#%02x%02x%02x", random.nextInt(255), random.nextInt(255), random.nextInt(255));
        simpMessagingTemplate.convertAndSend("/topic/color", new Greeting(color));
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(color));
    }

    @MessageMapping("/hello.user")
    public void greetingToUser(HelloMessage message) throws Exception {
        log.info("Incoming message: " + message);
        Thread.sleep(100);
        simpMessagingTemplate.convertAndSend("/topic/hello.user", new Greeting("hello hello" ));
        Random random = new Random();
        String color = String.format("#%02x%02x%02x", random.nextInt(255), random.nextInt(255), random.nextInt(255));
        simpMessagingTemplate.convertAndSend("/topic/color", new Greeting(color));
    }
}

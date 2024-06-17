package homework5.resource;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class DefaultErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Logging error
        log.error("An error occurred, redirecting to index.html");

        // Returning index.html page
        return "forward:/index.html";
    }
}

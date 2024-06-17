package homework5.resource;

import homework5.security.JwtAuthentication;
import homework5.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class HelloController {

    private final AuthService authService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        log.info("Hello user endpoint accessed by user: {}", authInfo.getPrincipal().getUserName());
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal().getUserName() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        log.info("Hello admin endpoint accessed by admin: {}", authInfo.getPrincipal().getUserName());
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal().getUserName() + "!");
    }
}
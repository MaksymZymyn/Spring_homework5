package homework5.resource;

import homework5.security.JwtRequest;
import homework5.security.JwtResponse;
import homework5.security.RefreshJwtRequest;
import homework5.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        log.info("Login request received for username: {}", authRequest.getLogin());
        final JwtResponse token = authService.login(authRequest);
        log.info("Login successful for user: {}", authRequest.getLogin());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        log.info("Request received to generate new access token for refresh token: {}", request.getRefreshToken());
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        log.info("New access token generated successfully for refresh token: {}", request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        log.info("Request received to refresh access token for refresh token: {}", request.getRefreshToken());
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        log.info("Access token refreshed successfully for refresh token: {}", request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/revoke")
    public ResponseEntity<?> revokeToken(@RequestParam String accessToken) {
        log.info("Request received to revoke token: {}", accessToken);
        boolean isRevokeSuccess = authService.revokeToken(accessToken);
        if (isRevokeSuccess) {
            log.info("Token {} revoked successfully", accessToken);
            return ResponseEntity.ok("Token was revoked successfully");
        }
        log.warn("Failed to revoke token: {}", accessToken);
        return ResponseEntity.badRequest().body("Token was not revoked");
    }
}

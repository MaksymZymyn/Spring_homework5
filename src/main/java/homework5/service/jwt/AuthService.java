package homework5.service.jwt;

import homework5.domain.SysUser;
import homework5.exceptions.AuthException;
import homework5.security.JwtAuthentication;
import homework5.security.JwtResponse;
import homework5.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import homework5.security.JwtRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final Map<String, List<String>> accessStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // Інші методи

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    // Метод, який встановлює об'єкт користувача в JwtAuthentication
    public void setAuthInfo(SysUser user) {
        JwtAuthentication jwtAuth = new JwtAuthentication();
        jwtAuth.setPrincipal(user); // Встановлює користувача
        jwtAuth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(jwtAuth);
    }

    // Метод, який використовує setAuthInfo
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final SysUser user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getEncryptedPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUserName(), refreshToken);

            List<String> accessTokens = accessStorage.computeIfAbsent(user.getUserName(), k -> new ArrayList<>());
            accessTokens.add(accessToken);
            accessStorage.put(user.getUserName(), accessTokens);

            setAuthInfo(user); // Виклик методу setAuthInfo

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    @Transactional
    public List<String> getTokensByUser(String username) {
        Optional<SysUser> user = userService.findUsersByUserName(username);
        if (user.isPresent()) {
            List<String> accessTokens = accessStorage.get(username);
            return accessTokens;
        }
        return Collections.emptyList();
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            System.out.println("Validate refresh token");
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                System.out.println("Validate refresh token inside if");
                final SysUser user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);

                List<String> accessTokens = accessStorage.computeIfAbsent(login, k -> new ArrayList<>());
                accessTokens.add(accessToken);
                accessStorage.put(login, accessTokens);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final SysUser user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUserName(), newRefreshToken);

                List<String> accessTokens = accessStorage.computeIfAbsent(login, k -> new ArrayList<>());
                accessTokens.add(accessToken);
                accessStorage.put(login, accessTokens);

                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("JWT token is invalid");
    }

    public boolean revokeToken(@NonNull String accessToken) {
        if (jwtProvider.validateAccessToken(accessToken)) {
            final Claims claims = jwtProvider.getAccessClaims(accessToken);
            final String login = claims.getSubject();
            List<String> tokens = accessStorage.get(login);
            if (tokens != null) {
                tokens.remove(accessToken);
                if (tokens.isEmpty()) {
                    accessStorage.remove(login);
                } else {
                    accessStorage.put(login, tokens);
                }
                return true;
            }
        }
        return false;
    }
}

package homework5.filter;

import homework5.domain.SysUser;
import homework5.security.JwtAuthentication;
import homework5.security.JwtUtils;
import homework5.service.UserService;
import homework5.service.jwt.AuthService;
import homework5.service.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token == null) {
            fc.doFilter(request, response);
            return;
        }
        try {
            if (jwtProvider.validateAccessToken(token)) {
                final Claims claims = jwtProvider.getAccessClaims(token);
                final String login = claims.getSubject();
                List<String> accessTokens = authService.getTokensByUser(login);

                if (accessTokens != null && accessTokens.contains(token)) {
                    SysUser user = userService.getByLogin(login).orElse(null);
                    if (user != null) {
                        final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims, user);
                        jwtInfoToken.setAuthenticated(true);
                        SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT", e);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
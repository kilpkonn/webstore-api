package ee.taltech.iti0203.webstore.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter doing the authentication
 */
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String jwtToken = getToken(request);
        if (jwtToken == null) {
            //passing without authentication to another steps
            chain.doFilter(request, response);
            return;
        }
        String username = getUsername(jwtToken);
        if (username == null) {
            //passing without authentication to another steps
            chain.doFilter(request, response);
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private String getUsername(String jwtToken) {
        try {
            return jwtTokenProvider.getIdCodeFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error("Unable to get JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT Token has expired", e);
        }
        return null;
    }

    private String getToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        if (requestTokenHeader == null || !requestTokenHeader.startsWith(BEARER_)) {
            return null;
        }
        return requestTokenHeader.substring(7);
    }
}

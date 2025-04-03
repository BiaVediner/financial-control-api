package br.com.api.application.config;

import br.com.api.application.handler.ErrorHandler;
import br.com.api.domain.exceptions.UnauthorizedException;
import br.com.api.domain.exceptions.enums.ErrorMessageEnum;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    @Autowired
    private ErrorHandler handler;

    @Value("${application.security.api-key}")
    private String apiKey;

    private static final String[] WHITELIST = {
            "/v1/swagger-ui",
            "/v1/v3/api-docs",
            "/v1/swagger-resources",
            "/v1/actuator",
            "/v1/metrics"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String providedApiKey = request.getHeader("X-API-KEY");

        if (!isUriWhitelisted(request) && !apiKey.equals(providedApiKey)) {
            customErrorResponse(response);
            return;
        }

        PreAuthenticatedAuthenticationToken authentication =
                new PreAuthenticatedAuthenticationToken(apiKey, null, Collections.emptyList());
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isUriWhitelisted(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        for (String pattern : WHITELIST) {
            if (requestURI.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    private void customErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(new Gson().toJson(handler.handleException(new UnauthorizedException(ErrorMessageEnum.UNAUTHORIZED))));
        out.flush();
    }
}
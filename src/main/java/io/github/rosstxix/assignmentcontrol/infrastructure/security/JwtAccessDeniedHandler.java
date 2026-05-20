package io.github.rosstxix.assignmentcontrol.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.ErrorResponseFactory;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ErrorResponseFactory errorResponseFactory;
    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ErrorResponseFactory errorResponseFactory, ObjectMapper objectMapper) {
        this.errorResponseFactory = errorResponseFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) throws IOException {
        ErrorResponse body = errorResponseFactory.create(
                HttpStatus.FORBIDDEN,
                ApiErrorCode.ACCESS_DENIED,
                "Access denied",
                request.getRequestURI()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        log.warn("Access denied: {}", ex.getMessage());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

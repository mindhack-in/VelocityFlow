package com.velocity_flow.api.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

/**
 * Handles access denied errors (e.g., when an authenticated user lacks the
 * required permission).
 */
@Component
public class CustomAccessDeniedHandler
	implements AccessDeniedHandler {


	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_FORBIDDEN);
		body.put("error", "Forbidden");
		body.put("message", "You do not have permission to access this resource");
		body.put("path", request.getServletPath());
		body.put("timestamp", LocalDateTime.now().toString());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}
}

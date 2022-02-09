package com.upgrad.bookmyconsultation.servlet;

import com.upgrad.bookmyconsultation.controller.AppointmentController;
import com.upgrad.bookmyconsultation.entity.UserAuthToken;
import com.upgrad.bookmyconsultation.exception.AuthorizationFailedException;
import com.upgrad.bookmyconsultation.exception.RestErrorCode;
import com.upgrad.bookmyconsultation.exception.UnauthorizedException;
import com.upgrad.bookmyconsultation.provider.BearerAuthDecoder;
import com.upgrad.bookmyconsultation.service.AuthTokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.upgrad.bookmyconsultation.constants.ResourceConstants.BASIC_AUTH_PREFIX;

@Component
public class AuthFilter extends ApiFilter {

	private static final Logger logger = LogManager.getLogger(AuthFilter.class);


	@Autowired
	private AuthTokenService authTokenService;

	@Override
	public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		if (servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		final String pathInfo = servletRequest.getRequestURI();
		logger.info("Path Info"+pathInfo);
		if (!pathInfo.contains("register") && !pathInfo.contains("actuator") &&  !pathInfo.contains("favicon.ico") && !pathInfo.contains("doctors")){
			final String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			logger.info("Authorization"+authorization);
			if (StringUtils.isEmpty(authorization)) {
				logger.info("Exception ");
				throw new UnauthorizedException(RestErrorCode.ATH_001);
			}

			if (pathInfo.contains("login") && !authorization.startsWith(BASIC_AUTH_PREFIX)) {
				throw new UnauthorizedException(RestErrorCode.ATH_002);
			}

			if (!pathInfo.contains("login")) {
				final String accessToken = new BearerAuthDecoder(authorization).getAccessToken();
				try {
					final UserAuthToken userAuthTokenEntity = authTokenService.validateToken(accessToken);
					servletRequest.setAttribute(HttpHeaders.AUTHORIZATION, userAuthTokenEntity.getUser().getEmailId());
				} catch (AuthorizationFailedException e) {
					servletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
					return;
				}
			}
		}
		System.out.println("Else");
		filterChain.doFilter(servletRequest, servletResponse);
	}

}

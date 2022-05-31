package com.mindhub.homebanking.Utils;

import org.jboss.logging.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOG = Logger.getLogger(String.valueOf(CustomAccessDeniedHandler.class));

    @Override
    public void handle(HttpServletRequest request,HttpServletResponse response,AccessDeniedException exc) throws IOException{

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            LOG.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + "/web/403.html");

    }
}

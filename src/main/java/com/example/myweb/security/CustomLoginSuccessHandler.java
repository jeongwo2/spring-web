package com.example.myweb.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.log4j.Log4j;

/** Part7 사용자가 성공적으로 인증할 때
 * CustomLoginSuccessHandler is a class that implements the AuthenticationSuccessHandler interface.
 * This class is responsible for handling the successful authentication event in Spring Security.
 * It logs a warning message upon successful login and redirects the user to the appropriate page based on their roles.
 * 로그인 성공 후 특정 URI 로 이동하거나 쿠키 처리
 * @author YourName
 * @version 1.0
 * @since 2023-01-01
 */
@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    /** Part7
     * This method is called when a user successfully authenticates.
     * It logs a warning message, retrieves the user's roles, and redirects the user to the appropriate page.
     * 로그인 성공 후 특정 URI 로 이동하거나 쿠키 처리 등의 추가적인 작업
     * @param request The HttpServletRequest object representing the incoming request.
     * @param response The HttpServletResponse object representing the outgoing response.
     * @param auth The Authentication object representing the authenticated user.
     * @throws IOException If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {

        log.info("Login Success");

        List<String> roleNames = new ArrayList<>();

       // 사용자의 역할을 검색하고, roleNames 목록에 저장합니다.
        auth.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        log.info("ROLE NAMES[roleNames]:{} " , roleNames);

		// 사용자의 역할에 따라 적절한 페이지로 리디렉션합니다.
        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/sample/admin");
            return;
        }

        if (roleNames.contains("ROLE_MEMBER")) {
            response.sendRedirect("/sample/member");
            return;
        }

        response.sendRedirect("/");
    }
}



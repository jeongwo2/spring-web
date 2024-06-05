package com.example.myweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

/** ex06
 * CustomAccessDeniedHandler is a class that implements the AccessDeniedHandler interface
 * provided by Spring Security. This class is responsible for handling access denied exceptions
 * and redirecting the user to a specific error page.
 *
 * @author YourName
 * @since 2023-01-01
 */
@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * This method is called when an access is denied. 오류 메시지를 기록하고,
   * "/accessError" 페이지로 사용자를 리디렉션하며, 발생할 수 있는 예외를 처리합니다.
   *
   * @param request The HttpServletRequest object representing the incoming request.
   * @param response The HttpServletResponse object representing the response to be sent.
   * @param accessException The AccessDeniedException that was thrown when access was denied.
   * @throws IOException If an input or output error occurs.
   * @throws ServletException If a servlet-specific error occurs.
   */
  @Override
  public void handle(HttpServletRequest request, 
      HttpServletResponse response, AccessDeniedException accessException)
      throws IOException, ServletException {

    log.error("Access Denied Handler");
    log.info("/accessError 페이지로 사용자를 리디렉션....");

    response.sendRedirect("/accessError");

  }
}

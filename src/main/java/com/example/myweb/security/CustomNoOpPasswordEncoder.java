package com.example.myweb.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j;

/** ex06 security 애플리케이션에서 비밀번호를 인코딩하고 검증
 * CustomNoOpPasswordEncoder is a custom implementation of Spring Security's PasswordEncoder interface.
 * This class is used to encode and verify passwords in a Spring Boot application.
 * In this example, the encoder does not perform any actual encoding, but simply returns the raw password.
 * This is useful for testing or demonstration purposes.
 *
 * @author YourName
 * @version 1.0
 * @since 2022-01-01
 */
@Log4j2
public class CustomNoOpPasswordEncoder implements PasswordEncoder {

    /** 원 비밀번호를 인코딩
     * Encodes the raw password.
     * In this implementation, the raw password is returned as is.
     *
     * @param rawPassword the raw password to be encoded
     * @return the encoded password
     */
    public String encode(CharSequence rawPassword) {
        log.info("before encode :" + rawPassword);
        return rawPassword.toString();
    }

    /** 원 비밀번호가 인코딩된 비밀번호와 일치하는지 확인
     * Verifies if the raw password matches the encoded password.
     * In this implementation, the raw password is compared directly with the encoded password.
     *
     * @param rawPassword the raw password to be verified
     * @param encodedPassword the encoded password to be verified against
     * @return true if the raw password matches the encoded password, false otherwise
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info("matches: " + rawPassword + ":" + encodedPassword);
        return rawPassword.toString().equals(encodedPassword);
    }
}

package com.example.myweb.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import com.example.myweb.domain.MemberVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * CustomUser class extends Spring's User class to add additional member fields and methods.
 * This class is used in Spring Security for authentication and authorization.
 *
 * @author YourName
 * @since 2023-01-01
 */
@Getter
public class CustomUser extends User {
    private static final Logger log = LogManager.getLogger(CustomUser.class);
    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * MemberVO object that holds user information.
     */
    private MemberVO member;

    /**
     * Constructor for creating a CustomUser object from username, password, and authorities.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param authorities The collection of granted authorities for the user.
     */
    public CustomUser(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        // User 클래스의 생성자를 호출하여 username, password, 그리고 authorities 를 전달합니다.
        super(username, password, authorities);
        log.info("User 클래스의 생성자를 호출하여 username, password, 그리고 authorities 를 전달");
    }

    /**
     * Constructor for creating a CustomUser object from a MemberVO object.
     * This constructor extracts the user's username, password, and authorities from the MemberVO object.
     *
     * @param vo The MemberVO object containing user information.
     */
    public CustomUser(MemberVO vo) {
		// MemberVO 객체에서 사용자 이름, 비밀번호, 권한을 추출합니다.
		// 권한은 SimpleGrantedAuthority 객체로 변환됩니다.
        super(vo.getUserid(), vo.getUserpw(), vo.getAuthList().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
		// CustomUser 클래스의 멤버 변수 'member' 에 MemberVO 객체를 할당합니다.
        log.info("CustomUser 클래스의 멤버 변수 'member' 에 MemberVO 객체를 할당 {}", vo );
        this.member = vo;
    }
}

package com.example.myweb.security;

import com.example.myweb.domain.MemberVO;
import com.example.myweb.mapper.MemberMapper;
import com.example.myweb.security.domain.CustomUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**ex06 데이터베이스에서 사용자 관련 데이터를 로드하고, 인증 및 권한 부여
 * CustomUserDetailsService is a class that implements the UserDetailsService interface
 * provided by Spring Security. It is responsible for loading user-specific data from the
 * database and creating a CustomUser object for authentication and authorization purposes.
 *
 * @author YourName
 * @version 1.0
 * @since 2023-01-01
 */
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * MemberMapper is an interface that provides methods for interacting with the database
     * to retrieve member-related data. It is autowired to facilitate database operations.
     */
    @Setter(onMethod_ = { @Autowired })
    private MemberMapper memberMapper;

    /**
     * loadUserByUsername is a method that retrieves user-specific data from the database
     * based on the provided username and creates a CustomUser object.
     *
     * @param userName the username of the user to be loaded
     * @return a CustomUser object containing the user's data
     * @throws UsernameNotFoundException if the user with the given username is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        log.info("Load User By UserName : " + userName);

        // userName은 사용자 ID를 의미합니다.
        MemberVO vo = memberMapper.read(userName);

        log.info("queried by member mapper: " + vo);

        // 데이터베이스에서 사용자를 찾지 못한 경우 null을 반환합니다.
        // 그렇지 않으면 CustomUser 객체를 생성하여 반환합니다.
        return vo == null? null : new CustomUser(vo);
    }

}

## Part 7
• **Spring Web Security의 이해**    
• **인증(Authentication) VS 인가(Authorization)**    
• **로그인 처리와 자동 로그인**  
• **CSRF 공격과 CSRF토큰**

▣ 스프링 시큐리트를 위한 설정  
• 스프링 시큐리티 관련 라이브러리

```
  <spring-security-version>5.2.11.RELEASE</spring-security-version> <!-- 취약점 개선 -->
  
  <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-config</artifactId>
      <version>${spring-security-version}</version>
  </dependency>

  <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-core</artifactId>
      <version>${spring-security-version}</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-taglibs -->
  <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-taglibs</artifactId>
      <version>${spring-security-version}</version>
  </dependency>

```

▣ web.xml 변경

• [web.xml] 변경
~~~
  <!-- Part7-2 add security-context.xml -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/root-context.xml,
            /WEB-INF/spring/security-context.xml</param-value>
    </context-param>
    
  <!--Part7-1 스프링 시큐리트를 위한 설정 -->
    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
~~~

▣ security-config.xml의 추가
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:security="http://www.springframework.org/schema/security"
  xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
    http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

<security:http>

  <security:form-login />

  </security:http>

  <security:authentication-manager>

  </security:authentication-manager>

</beans>
```

▣ 인증(Authentication)과 권한 부여(Authorization - 인가)
** 출입증 검사 -> 허가증 패용(권한)**

▣ 스프링 시큐리티의 핵심 구조

[AuthenticationManager]  [AuthenticationProvider] ↔ UserDetailsService
↑                     ↑
[ProviderManager] ←─────────┘

▣ 로그인과 로그아웃처리
• [security-context.xml] **접근제한 설정**
~~~
<security:http>
		<!-- 접근제한 설정: URL patterns and their access control -->
		<security:intercept-url pattern="/sample/all" access="permitAll" />
		<security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')" />
		<security:intercept-url pattern="/sample/admin"  access="hasRole('ROLE_ADMIN')" />
    <security:form-login />

</security:http>
~~~    

▣ 단순 로그인 처리  
**5버전이상에서는 반드시 PasswordEncoder작업이 필요하므로 주의**

~~~
 <security:authentication-manager>
    <security:authentication-provider>
      <security:user-service>        
        <security:user name="member" password="{noop}member" authorities="ROLE_MEMBER"/>      
      </security:user-service>    
    </security:authentication-provider>
  </security:authentication-manager>
~~~

▣ 로그인 후 이동 확인

http://localhost:8081/sample/all
http://localhost:8081/login

▣ 여러 권한을 가지는 사용자 설정

<security:authentication-manager>

    <security:authentication-provider>
<security:user-service>        
<security:user name="member" password="{noop}member" authorities="ROLE_MEMBER"/>        
<security:user name="admin" password="{noop}admin" authorities="ROLE_MEMBER, ROLE_ADMIN"/>
</security:user-service>    
</security:authentication-provider>

  </security:authentication-manager>

▣ 접근 제한 메시지의 처리

• AccessDeniedHandler를 구현하거나 URI를 지정해서 처리

<security:access-denied-handler error-page="/accessError"/>


▣ 커스텀 로그인 페이지

<security:form-login login-page="/customLogin" />



// Part7-5 커스텀 로그인 페이지
@GetMapping("/customLogin")
public void loginInput(String error, String logout, Model model) {

		log.info("/customLogin  error {}, logout {}: ", error, logout);

		if (error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}

		if (logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}

[customLogin.jsp]
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

  <h1>Custom Login Page</h1>
  <h2><c:out value="${error}"/></h2>
  <h2><c:out value="${logout}"/></h2>

  <form method='post' action="/login">  
    <div>
      <input type='text' name='username' value='admin'>
    </div>
    <div>
      <input type='password' name='password' value='admin'>
    </div>
    <div>
      <input type='submit'>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />  
  </form>

</body>
</html>


▣ CSRF 공격

• 사이트간 요청 위조(Cross-site request forgery) 공격

▣ CSRF토큰
• 스프링 시큐리티는 기본적으로 GET방식을 제외하고 모든 요청에 CRSF토큰 사용  
• <form>등의 데이터 전송시에 CSRF토큰을 같이 전송하도록 처리

▣ 로그인 성공과 AuthenticationSuccessHandler  
• 로그인 성공 후 특정 URI로 이동하거나 쿠키 처리 등의 추가적인 작업

```

@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, 
    HttpServletResponse response, 
    Authentication auth)   throws IOException, ServletException {

    log.warn("Login Success");

    List<String> roleNames = new ArrayList<>();

    auth.getAuthorities().forEach(authority -> {
      roleNames.add(authority.getAuthority());
    });

    log.warn("ROLE NAMES: " + roleNames);

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
```


<bean id="customAccessDenied" class="org.zerock.security.CustomAccessDeniedHandler"></bean>
<bean id="customLoginSuccess" class="org.zerock.security.CustomLoginSuccessHandler"></bean>

  <security:http>
<security:intercept-url pattern="/sample/all" access="permitAll"/>
<security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
<security:intercept-url pattern="/sample/admin" access="hasRole('ROLE_ADMIN')"/>  
<security:access-denied-handler ref="customAccessDenied"/>    
<security:form-login login-page="/customLogin" authentication-success-handler-ref="customLoginSuccess" />
<!-- <security:csrf disabled="true"/> -->
</security:http>

▣ 로그아웃의 처리와 LogoutSuccessHandler
 <!--  Part7-6 Logout configuration -->
<security:logout logout-url="/customLogout" invalidate-session="true" />

[customLogout.jsp]
<form action="/customLogout" method='post'>
  <input type="hidden"name="${_csrf.parameterName}"value="${_csrf.token}"/>
  <button>로그아웃</button>
</form>


▣ JDBC를 이용하는 간편 인증/권한 처리
• 패스워드는 PasswordEncoder를 지정해서 처리

▣ 회원 테이블의 설계

create table users(
username varchar2(50) not null ,
password varchar2(50) not null,
enabled  char(1) default '1'
);

alter table users
add constraint pk_users primary key (username);

 create table authorities (
username varchar2(50)  not null,
authority varchar2(50) not null
);

alter table authorities
add constraint fk_authorities_users foreign key (username) references users(username);

create unique index idx_auth_username on authorities (username,authority);

insert into users (username, password) values ('member00','pw00');
insert into users (username, password) values ('admin00','pw00');
insert into users (username, password) values ('user00','pw00');

insert into authorities (username, authority) values ('member00','ROLE_MANAGER');
insert into authorities (username, authority) values ('admin00','ROLE_MANAGER');
insert into authorities (username, authority) values ('admin00','ROLE_ADMIN');
insert into authorities (username, authority) values ('user00','ROLE_USER');

commit;

SELECT * FROM users ;

SELECT * FROM authorities ;


▣ Security-context 설정

  <!-- Part7 회원 정보를 이용해서 로그인 처리:AuthenticationManager configuration -->
	<security:authentication-manager>
		<!-- AuthenticationProvider using CustomUserDetailsService and BCryptPasswordEncoder -->
		<security:authentication-provider
			user-service-ref="customUserDetailsService">
			<security:password-encoder
				ref="bcryptPasswordEncoder" />
		</security:authentication-provider>
	</security:authentication-manager>


▣ PasswordEncoder의 설정  
• 암호화를 피하고 싶다면 직접 PasswordEncoder를 구현

```

  <security:authentication-manager>
    <security:authentication-provider>
      <security:jdbc-user-service
        data-source-ref="dataSource" />
      <security:password-encoder
        ref="customPasswordEncoder" />
    </security:authentication-provider>
  </security:authentication-manager>-

```

▣ 기존의 테이블을 이용하는 경우 BCryptPasswordEncoder

```
  <bean id="bcryptPasswordEncoder"
    class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

  <security:authentication-provider>
    <security:jdbc-user-service
        data-source-ref="dataSource" />        
        <security:password-encoder
        ref="bcryptPasswordEncoder" />
    </security:authentication-provider>
  </security:authentication-manager>
```

▣ 인코딩된 패스워드를 가지는 사용자 생성

@Setter(onMethod_ = @Autowired)
private PasswordEncoder pwencoder;

@Setter(onMethod_ = @Autowired)
private DataSource ds;

…
pstmt.setString(2, pwencoder.encode("pw" + i));

▣ 쿼리를 이용하는 인증

```
 <security:jdbc-user-service
      data-source-ref="dataSource"
      users-by-username-query="select userid , userpw , enabled from tbl_member where userid = ? "
      authorities-by-username-query="select userid, auth from tbl_member_auth where userid = ? " />

  <!-- <security:password-encoder ref="customPasswordEncoder" /> -->

  <security:password-encoder ref="bcryptPasswordEncoder" />

```

▣ 커스텀 UserDetailsService 활용

• 사용자가 원하는 방식으로 인가/인증 처리를 하기 위해서는
**직접 UserDetailsService인터페이스를 구현해서 처리**


▣ 회원 도메인

```
@Data
public class MemberVO {

  private String userid;
  private String userpw;
  private String userName;
  private boolean enabled;

  private Date regDate;
  private Date updateDate;
  private List<AuthVO> authList;

}

@Data
public class AuthVO {

  private String userid;
  private String auth;
  
}

```

▣ 회원 Mapper 설계
• MyBatis의 <resultMap>을 활용해 Join처리시에 발생하는 1:N문제를 해결

```
[MemberMapper.xml]
<mapper namespace="com.example.myweb.mapper.MemberMapper">
<!-- Part7 MyBatis 의 <resultMap>을 활용해 Join 처리시에 발생하는 1:N 문제를 해결 -->
  <resultMap type="com.example.myweb.domain.MemberVO" id="memberMap">
    <id property="userid" column="userid"/>
    <result property="userid" column="userid"/>
    <result property="userpw" column="userpw"/>
    <result property="userName" column="username"/>
    <result property="regDate" column="regdate"/>
    <result property="updateDate" column="updatedate"/>
    <collection property="authList" resultMap="authMap">
    </collection> 
  </resultMap>
  
  <resultMap type="com.example.myweb.domain.AuthVO" id="authMap">
    <result property="userid" column="userid"/>
    <result property="auth" column="auth"/>
  </resultMap>
  
  <select id="read" resultMap="memberMap">
    SELECT
          mem.userid,  userpw, username, enabled, mem.regdate, mem.updatedate, auth.auth
    FROM  tbl_member mem
    LEFT OUTER JOIN tbl_member_auth  auth
    on   mem.userid = auth.userid
    WHERE mem.userid = #{userid}
  </select>

</mapper>

```

▣ CustomUserDetailsService 구성
• 스프링 시큐리티의 UserDetailsService를 구현하고, MemberMapper 타입의 인스턴스를 주입 받아서 실제 기능을 구현

• UserDetailsService
↑   
CustomUserDetailsService  ⬅  MemberMapper

```

@Log4j
public class CustomUserDetailsService implements UserDetailsService {

  @Setter(onMethod_ = { @Autowired })
  private MemberMapper memberMapper;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    log.warn("Load User By UserName : " + userName);
    return null;
  }
}

```  

▣ security-context.xml의 수정- customUserDetailsService

```

  <security:authentication-manager>
    <security:authentication-provider
      user-service-ref="customUserDetailsService">

      <security:password-encoder ref="bcryptPasswordEncoder" />

    </security:authentication-provider>

  </security:authentication-manager>

```

▣ MemberVO를 UsersDetails 타입으로 변환하기

CustomUser          →  User  → UserDetail    
MemberVO    
List<AuthVO>


```
public class CustomUser extends User {

  private static final long serialVersionUID = 1L;

  private MemberVO member;

  public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public CustomUser(MemberVO vo) {

    super(vo.getUserid(), vo.getUserpw(), vo.getAuthList().stream()
        .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));

    this.member = vo;
  }
}


@Log4j
public class CustomUserDetailsService implements UserDetailsService {

  @Setter(onMethod_ = { @Autowired })
  private MemberMapper memberMapper;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    log.warn("Load User By UserName : " + userName);

    // userName means userid
    MemberVO vo = memberMapper.read(userName);

    log.warn("queried by member mapper: " + vo);

    return vo == null ? null : new CustomUser(vo);
  }

}


```


▣ 스프링 시큐리티를 JSP에서 활용하기

• 의존성 추가: pom.xml이나 build.gradle에 스프링 시큐리티 관련 의존성을 추가.  
• 보안 설정: WebSecurityConfigurerAdapter를 상속받는 클래스를 생성하여 보안 설정을 구성.  
• 로그인 페이지: 사용자 정의 로그인 페이지   
• 권한에 따른 접근 제어: 특정 URL에 대해 권한별로 접근을 제어할 수 있도록 설정.

• JSP에서는 시큐리티 태그들을 이용해서 처리
1. 태그 라이브러리 추가:

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

2. 인증 정보 출력: <sec:authentication> 태그와 principal 이라는 이름의 속성을 사용

<p>principal : <sec:authentication property="principal"/></p>
<p>MemberVO : <sec:authentication property="principal.member"/></p>
<p>사용자이름 : <sec:authentication property="principal.member.userName"/></p>
<p>사용자아이디 : <sec:authentication property="principal.username"/></p>
<p>사용자 권한 리스트  : <sec:authentication property="principal.member.authList"/></p>

3. 권한에 따른 내용 표시: <sec:authorize> 태그를 사용

<sec:authorize access="hasRole('ROLE_USER')">
  <!-- 사용자에게만 보여질 내용 -->
</sec:authorize>

4. 로그인, 로그아웃 링크:

<a href="<c:url value='/login'/>">Login</a>
<a href="<c:url value='/logout'/>">Logout</a>


▣ 표현식을 이용하는 동적 화면 구성

hasRole( [role] )
hasAuthority( [authority] ) : 해당 권한이 있으면 true

hasAnyRole( [role,role2])
hasAnyAuthority([authority]): 여러 권한들 중에서 하나라도 해당하는 권한이 있으면 true

principal: 현재 사용자 정보를 의미
permitAll: 모든 사용자에게 허용
denyAll  : 모든 사용자에게 거부
isAnomymous( ): 익명의 사용자의 경우(로그인을 하지 않은 경우도 해당)
isAuthenticated( ): 인증된 사용자면 true
isFullyAuthenticated( ): Remember-me로 인증된 것이 아닌 인증된 사용자인 경우 true


▣ 자동 로그인(remember-me)

• security-context.xml에는 <security:remember-me> 태그를 이용해서 기능을 구현
◦ key: 쿠키에 사용되는 값을 암호화하기 위한 키(key)값  
◦ data-source-ref: DataSource를 지정하고 테이블을 이용해서 기존 로그인 정보를 기록(옵션)  
◦ remember-me-cookie: 브라우저에 보관되는 쿠키의 이름을 지정합니다.  
  기본값은 ‘remember-me’입니다.  
◦ remember-me-parameter: 웹 화면에서 로그인할 때 ‘remember-me’는 대부분 체크박스를  
이용해서 처리합니다. 이 때 체크박스 태그의 name속성을 의미합니다.  
◦ token-validity-seconds: 쿠키의 유효시간을 지정합니다.  





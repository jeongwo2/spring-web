# spring-web
#### [참고] 코드로 배우는 스프링 웹프로젝트(개정판)
* * *
### 📑 개발환경
- **Language**: Java 11, JSP, javascript, html5, css3
- **IDE**: IntelliJ
- **Framework**: Spring 5.2.25.RELEASE
- **DB**: Oracle 11g, Mybatis 3.5.9
- **UI**: JSP, javascript, html5, css3, jquery, bootstrap4

### 📑 프로젝트 Library 의존성
```
┌───────────────────────────────────────┐
│   라이브러리             │ 버전        │
├─────────────────────────┼─────────────│
│ spring-security         │ 5.7.5       │
│ aspectj                 │ 1.9.0       │
│ log4j2                  │ 2.17.1      │
│ mybatis                 │ 3.5.9       │
│ mybatis-spring          │ 3.0.0       │
│ hikaricp                │ 4.0.3       │
│ jackson                 │ 2.15.1      │
│ gson                    │ 2.9.0       │
│ javax.servlet-api       │ 3.1.0       │
│ javax.servlet.jsp-api   │ 2.3.3       │
│ jstl                    │ 1.2         │
│ junit                   │ 4.12        │
│ lombok                  │ 1.18.20     │
│ log4jdbc-log4j2-jdbc4.1 │ 1.16        │
│ hibernate-validator     │ 7.0.1.Final │
│ validation-api          │ 2.0.1.Final │
│ commons-fileupload      │ 1.5         │
│ commons-io              │ 2.15.1      │
└───────────────────────────────────────┘
 
```
※ **mybatis-spring 3.0.0 보다 크면 NamespaceHandler 에러 발생**

### 📑 프로젝트 구조
~~~
spring-web
├── src  
│   ├── main  
│   │   ├── java  
│   │   │   └── com  
│   │   │       └── example  
│   │   │           └── myweb  
│   │   │               ├── controller  
│   │   │               │   └── BoardController.java  
│   │   │               ├── domain  
│   │   │               │   ├── BoardVO.java  
│   │   │               │   └── PageMaker.java, PageDTO.java  
│   │   │               ├── service  
│   │   │               │   ├── BoardService.java  
│   │   │               │   └── BoardServiceImpl.java  
│   │   │               └── mapper  
│   │   │                   └── BoardMapper.java  
│   │   ├── resources  
│   │   │   ├── com\example\myweb\mapper  
│   │   │   │                     └── BoardMapper.xml  
│   │   │   ├── log4j2.xml, mybatis-config.xml  
│   │   │   └── log4jdbc.log4j2.properties  
│   │   │  
│   │   └── webapp
│   │       ├── resources    
│   │       │   ├── css  
│   │       │   ├── js  
│   │       │   └── images   
│   │       └── WEB-INF  
│   │           ├── spring  
│   │           │   ├── appServlet  
│   │           │   │   └── servlet-context.xml  
│   │           │   └── root-context.xml  
│   │           ├── views
│   │           │   ├── board
│   │           │   │   └── get.jsp, list.jsp, read.jsp, register.jsp, modify.jsp ...  
│   │           │   └── include  
│   │           │       └── header.jsp, footer.jsp  
│   │           └── web.xml
│   └── test  
│        └── java  
│           └── com\example\myweb  
│                           ├── controller  
│                           ├── service  
│                           └── mapper  
└── pom.xml  
~~~

### 📑 Tomcat 8080 port
1.1 포트 확인  
   netstat -ano | findstr :8080   

~~~
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       1234
~~~
2. 프로세스 종료 (Kill)  
   taskkill /F /PID 1234  
   taskkill /F /PID 15568
3. 

### JSP 주석 설명

- JSP 주석: <%-- JSP 주석은 결과에 포함되지 않으며, 브라우저에서도 주석 처리됩니다1. --%>  
- 자바 언어 주석: // 자바 주석은 스크립트릿, 표현식, 선언부 안에서만 사용 가능  
- HTML 주석: <!-- HTML 주석은 출력 결과에 포함되며, 브라우저에서도 주석 처리되지 않습니다. --   

### Spring FileUpload

**Apache Commons FileUpload 1.5 버전** 이전엔 multipart form 처리시 파일의 수 제한을  
두지 않았기 때문에 Dos 공격 가능성이 있는 취약점이다.  
- MultipartResolver


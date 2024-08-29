## Part1 

• 스프링의 개발 환경 (STS(혹은 Eclipse), Lombok 등)  
• 오라클 데이터베이스 설치 및 계정 설정  
• 스프링과 MyBatis 의 연동 설정  
• 스프링 MVC 의 구성 설정 및 테스트   

◼ 개발을 위한 준비 (JDK1.8)  
 • JDK 1.8 이상 사용  
 • 환경 변수  설정  
   - 변수  이름: JAVA_HOME  

◼ Eclipse or STS or Intellij

 • Eclipse 의 경우
   - STS 플러그인 추가 설치 후 사용  
 • STS 의 경우  
   - Eclipse 와 별도로 다운로드 및 압축 해제   

2. Tomcat 다운로드   
   • Tomcat 버전별 JDK 와 Servlet 정보

| Tomcat | Servlet | JSP | EL  | Websocket | JDK |
|--------|---------|-----|-----|-----------|-----|
| 11.0.x | 6.1     | 4.0 | 6.0 | 3.1       | 17~ |
| 10.1.x | 6.0     | 3.1 | 5.0 | 2.1       | 11~ |
| 9.0.x  | 4.0     | 2.3 | 3.0 | 1.1       | 8~  |
| 8.5.x  | 3.1     | 2.3 | 3.0 | 1.1       | 7~  |


◼ Spring MVC 프로젝트 생성

• Maven 디렉토리 구조
```
.      
├── pom.xml        
├── src
│   ├── main
│   │   ├── com      
│   │   │   └── example
│   │   │       └── myweb
│   │   ├── resources        
│   │   └── webapp         
│   │       ├── WEB-INF
│   │       │   ├── spring                      
│   │       │   │   ├── appServlet
│   │       │   │   │   └── servlet-context.xml
│   │       │   │   └── root-context.xml
│   │       │   └── views
│   │       ├── resources   
│   │       └── web.xml                                               
│   └── test          
│       ├── java
│       └── resources
└── target            
```

1) Eclipse 의 경우
  • Spring 프로젝트 중에서 ‘Spring Legacy Project’ 생성  
    ◦ 이클립스 상단 메뉴에서 File → New → Other→ Spring  
    ◦ Spring → Spring Legacy Project를 선택 후 Next    
    ◦ 프로젝트 이름을 설정하고 Spring MVC Project를 선택한 후  
       **프로젝트 이름**: spring-mvc  
           Next를 클릭합니다.      
    ◦ Spring MVC Project top-level package  
       **패키지명**: com.example.myweb    
       을 입력 후 Finish 를 클릭합니다.    

2) IntelliJ 에서   
   • File → New Project → Jakarta EE    
     ◦ Name: myweb  
     ◦ Location:  
     ◦ Templates: Web application  
     ◦ Application Server: Tomcat 9.0  
     ◦ Language: Java  
     ◦ Build System: Maven  
     ◦ Group: com.example  
     ◦ Artifact: myweb  
     ◦ JDK: 11.  
     ◦ Servlet: 4.0.1  
   • File → New Project → Maven  
     ◦ Name: myweb    
     ◦ Location:   
     ◦ JDK: 11.  
     ◦ Catalog: Interal    
     ◦ **Archetype**: org.apache.maven.archetypes:maven-archetype-webapp     
     ◦ Group: com.example  
     ◦ Artifact: myweb  

2. pom.xml의 수정

• 스프링 프로젝트의 버전 변경 => 5.0.7
• Java 버전 변경 및 ‘Maven -> Update Project’

```
<properties>
    <project.name>spring-web</project.name>
    <java-version>11</java-version>
    <maven.compiler-version>3.8.1</maven.compiler-version>
    <build.sourceEncoding>UTF-8</build.sourceEncoding>
    <!-- 리소스 파일 인코딩 설정 -->
    <project.resources.sourceEncoding>UTF-8</project.resources.sourceEncoding>
    <!-- 보고서 출력 인코딩 설정 -->
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>        

<build>
        <defaultGoal>package</defaultGoal>
        <finalName>${project.name}</finalName>

        <resources>
            <resource>
                <!-- src/main/java 폴더의 xml 파일들을 빌드에 포함 -->
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <!-- src/main/resources 폴더의 xml, properties, yml, setting 파일들을 빌드에 포함 -->
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                    <!-- <include>**/*.setting</include>-->
                </includes>
            </resource>
        </resources>
        <plugins>
            <plugin> <!-- 2.5.1 -->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven.compiler-version}</version>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                    <encoding>${build.sourceEncoding}</encoding>
                    <compilerArgument>-Xlint:unchecked</compilerArgument>
                    <showWarnings>true</showWarnings>
                    <showDeprecation>true</showDeprecation>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <encoding>${build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>

    </build>
```

2.2 Java 설정을 이용하는 경우  
• root-context.xml => RootConfig 클래스로 처리   
• web.xml => WebConfig 클래스로 처리     
• @Configuration 을 이용하는 설정

```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.2.0</version>
        <configuration>
          <failOnMissingWebXml>false</failOnMissingWebXml>
        </configuration>
    </plugin>


package com.example.myweb.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

}

package com.example.myweb.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer  {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    // TODO Auto-generated method stub
    return new Class[] {RootConfig.class};;
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String[] getServletMappings() {
    // TODO Auto-generated method stub
    return null;
  }

}

```

3.프로젝트의 실행 확인 및 경로 조정  
  • Context Path 를 '/'로 조정  

2장. 스프링 특징과 의존성 주입

3장. 스프링과 Oracle Database 연동

◼  Oracle 11g Express Edition

◼ DataSource 설정

```
[pom.xml]
<dependency>
   <groupId>com.zaxxer</groupId>
   <artifactId>HikariCP</artifactId>
   <version>2.7.4</version>
</dependency>

[root-context.xml]  
<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">  
  <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"></property>  
  <property name="jdbcUrl" value="jdbc:oracle:thin:@localhost:1521:XE"></property>    
  <property name="username" value="dev"></property>   
  <property name="password" value="dev$11g"></property>   
</bean>
<!-- HikariCP configuration -->
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource"  
      destroy-method="close">  
    <constructor-arg ref="hikariConfig" />  
</bean>   
```
Spring Context    Spring Context   Spring Context      
                   hikariConfig   → hikariConfig         
root-context.xml                    dataSource    

4장. MyBatis와 스프링 연동

• MyBatis 세팅
• spring-mybatis 라이브러리 설정
• XML Mapper 파일
• Mapper 인터페이스 설정 확인

◼ mybatis-spring의 설정

```
[pom.xml]
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.4.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>1.3.2</version>
</dependency>

[root-context.xml]
<!-- HikariCP configuration -->
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource"
  destroy-method="close">
  <constructor-arg ref="hikariConfig" />
</bean>

<bean id="sqlSessionFactory"
  class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource"></property>
</bean>

```

### ◼ sql 설정 | log4jdbc.log4j2

🏷️ Log4jdbc.log4j2 사용 이유
: 스프링에서 sql문을 실행한 로그를 직관적으로 볼 수 있도록 출력

📑 dependency 추가 [pom.xml]
```
<!-- SQL 로그 출력 -->
<!-- https://mvnrepository.com/artifact/org.bgee.log4jdbc-log4j2/log4jdbc-log4j2-jdbc4 -->
<dependency>
    <groupId>org.bgee.log4jdbc-log4j2</groupId>
    <artifactId>log4jdbc-log4j2-jdbc4</artifactId>
    <version>1.16</version>
</dependency>
```
📑 로그 설정 파일  
✔️ 경로 : 'src/main/resources' 패키지 경로에 log4jdbc.log4j2.properties 파일을 생성한다.  
```
log4jdbc.spylogdelegator.name=net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator
```

📑 log4j2.xml
STS Console 창에서 SQL 쿼리 Log 를 자세히 출력을 위한 설정  
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>

<!-- Logger 설정 -->
<Loggers>
<!-- Root Logger -->
<Root level="debug" additivity="false" >
    <!-- warn 에서 debug 로 수정시 sql 확인 -->
    <AppenderRef ref="console"/>
 </Logger>
  <!-- 스프링 프레임워크에서 찍는건 level을 info로 설정 -->
  <Logger name="org.springframework" level="info" additivity="false">
    <AppenderRef ref="console"/>
  </Logger>

</Configuration>
```
🔗 JDBC 의 Driver 변경 [root-context.xml]
```
<!-- <bean> hikariConfig <property> driverClassName, jdbcUrl value 수정 -->
<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
   <property name="driverClassName"
       value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy">
   </property>
   <property name="jdbcUrl"
       value="jdbc:log4jdbc:oracle:thin:@localhost:1521:XE">
   </property>
</bean>
```
📑 J@Log4j2 
```bazaar

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@Getter
@ToString
@Log4j2
public class HomeController { 
    final static Logger logger = LogManager.getLogger(HomeController.class);
    
    @RequestMapping(value = "/", method = RequestMethod.GET) 
    public String home(Locale locale, Model model) { 
       log.info("Log4j2.................."); 
	   log.info("Welcome home! The client locale is {}.", locale); 
	   Date date = new Date(); 
       DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, 
         locale); 
       String formattedDate = dateFormat.format(date); 
       model.addAttribute("serverTime", formattedDate ); 
       return "home"; 
   } 
 }
```

1. Mapper 인터페이스   

2. Mapper 인터페이스의 설정/인식
3. 
```
[root-context.xml]
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
<property name="dataSource" ref="dataSource"></property>        
</bean>

  <mybatis-spring:scan base-package="com.example.myweb.mapper" />

  <context:component-scan base-package="com.example.myweb.service"/>


public class TimeMapperTests {  
@Setter(onMethod_ = @Autowired)
private TimeMapper timeMapper;
}


```

3. XML Mapper 
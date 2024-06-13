## Part 5
• **JavaScript 의 모듈 패턴**    
• **AOP의 개념 이해와 적용**    
• **AOP의 용어와 기법**  
• **트랜잭션의 처리 적용**  

18. AOP 라는 paradigm
    • Aspect: abstract(추상)명사로 crossing(횡단) 관심사를 의미  
    ex> 로깅, 보안, 트랜잭션등  
    • Advice: 횡단 관심사를 구현한 객체  
    • Target: 핵심로직을 가지고 있는 객체   
    • Proxy 객체: Target 객체 + Advice

▣ AOP의 실습  
• pom.xml 수정

```
  <org.slf4j-version>1.7.25</org.slf4j-version>

<!-- AspectJ -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjrt</artifactId>
      <version>${org.aspectj-version}</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>${org.aspectj-version}</version>
    </dependency>
```
▣ 서비스 계층 설계/Advice 작성/Pointcut

```
@Aspect
@Log4j
@Component
public class LogAdvice {
  @Before( "execution(* com.example.myweb.service.SampleService*.*(..))")
  public void logBefore() {
    log.info("========================");
  }  
}
```
▣ AOP처리를 위한 설정  
**[root-context.xml 의 일부]**
```
    <context:annotation-config />

<!--part5 AOP: 패키지 내에서 어노테이션이 있는 클래스를 검색하고, 
    클래스를 Spring 빈으로 등록 -->
    <context:component-scan 
      base-package="com.example.myweb.aop"></context:component-scan>

    <!--part5 AOP: 공통적인 기능(예: 로깅, 트랜잭션 관리)이 코드를 재 사용 -->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```
@AfterThrowing: 예외 발생을 감지해서 AOP로 처리

### 19.스프링에서 트랜잭션 처리

▣ 트랜잭션 설정   
• pom.xml의 트랜잭션 관련 설정 추가
```
<!-- part5 트랜잭션 설정 추가 -->
    <bean id="transactionManager"
        class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <tx:annotation-driven />
```    

• 스프링의 경우 @Transactional 을 이용해서 설정 가능   
  ◦ 메서드의 @Transactional 설정이 가장 우선시 됩니다.   
  ◦ 클래스의 @Transactional 설정은 메서드보다 우선순위가 낮습니다.   
  ◦ 인터페이스의 @Transactional 설정이 가장 낮은 우선순위입니다.

▣ 댓글과 트랙잭션 설정

```
alter table tbl_board add (replycnt number default 0);

update tbl_board 
set replycnt = (select count(rno) from tbl_reply 
  where tbl_reply.bno = tbl_board.bno);  
```
```
  <update id="updateReplyCnt">
    update tbl_board 
      set replycnt = replycnt + #{amount} 
    where bno = #{bno}
  </update>
```
▣ ReplyServiceImpl 의 수정

```
[BoardMapper]
//part5 댓글과 트랙잭션 설정 추가
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
  
[ReplyServiceImpl]

  @Transactional // part5 트랙잭션 추가
  @Override
  public int register(ReplyVO vo) {
    log.info("register.....vo:{}" , vo);
    // part5 댓글과 트랙잭션 설정 추가
    boardMapper.updateReplyCnt(vo.getBno(), 1);

    return replyMapper.insert(vo);
  }
  ...
  @Transactional // part5
  @Override
  public int remove(Long rno) {
    log.info("remove...rno:{}" , rno);
    // part5 댓글과 트랙잭션 설정 추가
    ReplyVO vo = replyMapper.read(rno);
    log.info("remove...vo:{}" , vo);
    boardMapper.updateReplyCnt(vo.getBno(), -1);
    return replyMapper.delete(rno);
  }
```


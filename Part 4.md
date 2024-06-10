## Part 4 

• REST 방식의 데이터 교환 방식의 이해   
• Ajax 를 통한 JSON 데이터 통신  
• jQuery 를 이용하는 Ajax 처리   
• JavaScript 의 모듈 패턴   

#### 16. REST 방식으로 전환  

@RestController  

```bazaar
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.6</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.9.6</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.2</version>
</dependency>
```
- String 혹은 Integer 등의 타입들
- 사용자 정의 타입
- ResponseEntity<> 타입 

@PathVariable: URL 경로의 일부를 파라미터로 사용할 때 이용  

@RequestBody: JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우에 주로 사용  
일반 <form>방식으로 처리된 데이터  



### 17. Ajax 를 이용하는 댓글 처리 






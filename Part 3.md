## Part 3  

◼ 각 영역의 네이밍 규칙

• xxx**Controller**: 스프링 MVC에서 동작하는 Controller 클래스

• xxx**Serivce**, xxx**ServiceImpl**: 비즈니스 영역을 담당하는 인터페이스는   
 ‘xxxService’라는 방식을 사용하고, 인터페이스를 구현한 클래스는   
 ‘xxxServiceImpl’이라는 이름을 사용

• xxxDAO, xxxRepository: DAO(Data-Access-Object)나 Repository(저장소)라는   
  이름으로 영역을 따로 구성하는 것이 보편적. 예제에서는 별도의 DAO 를 구성하는   
  대신에 MyBatis 의 Mapper 인터페이스를 활용.

• VO, DTO: VO의 경우는 주로 Read Only의 목적이 강하고, 데이터 자체도   
  Immutable(불변)하게 설계. DTO 는 주로 데이터 수집의 용도  


◼ Project Module Directory Structure:

```
project-module/
│   pom.xml
├───src/
│   ├───main/
│   │   ├───java/
│   │   │   └───com/example/
│   │   │       └───yourproject/
│   │   │           ├───config/
│   │   │           ├───controller/
│   │   │           ├───domain/
│   │   │           ├───repository/
│   │   │           ├───service/
│   │   │           └───web/
│   │   │
│   │   └───resources/
│   │       ├───mapper/
│   │       └───application.properties
│   │
│   └───test/
│       └───java/
│           └───com/example/
│               └───yourproject/
└───docs/
```
◼ 소스 구현 순서
1. 데이터 흐름과 테이블 구조 파악
2. 테이블을 반영하는 VO 클래스 생성
3. MyBatis 의 Mapper 인터페이스 작성
4. SQL 쿼리 Mapper XML 작성 후 테스트
5. 서비스  
  ① 서비스 패키지 설정 (root-context.xml)  
  ② 서비스 구현  

◼ 뒤로가기와 history 객체  
등록페이지> 목록페이지> 조회페이지> 뒤로가기

➊            ➋                ➌                 ➍
/boards/register  
/boards/list       /boards/list
/boards/register  /boards/register   /boards/register
/boards/list  /boards/list      /boards/list       /boards/list

◼ 게시물의 수정/삭제

◼ 페이지 처리
1. 검색에 필요한 내용을 담는 Criteria 클래스
2. MyBatis  
   public List<BoardVO> getListWithPaging(Criteria cri);
3. 화면처리  
- BoardController/BoardService/BoardServiceImpl 처리
- list.jsp 처리     
  - 현재 page  
  - prev / next   
  - 시작번호와 끝 번호  
  - total 을 통한 endPage 의 재계산
```
  this.endPage   = (int)(Math.ceil(페이지번호 / 10.0)) * 10;
  this.startPage = this.endPage - 9;
  realEnd = (int) (Math.ceil((total * 1.0) / amount) );  
  if(realEnd  < this.endPage) {  
     this.endPage = realEnd;
  }
  this.prev = this.startPage > 1; //이전 페이지
  this.next = this.endPage < realEnd; // 다음페이지
```
http://localhost:8080/board/list?pageNum=5&amount=20  

◼ 검색처리 
- foreach 
```bazaar
Map<String, String> map = new HashMap<>();
  map.put("T", "TTTT");
  map.put("C", "CCCC");
```




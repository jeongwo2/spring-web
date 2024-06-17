## Part 6
• **스프링에서의 파일 업로드 처리**    
• **<form>과 Ajax 를 이용하는 파일 업로드**    
• **썸네일 처리**  
• **파일 다운로드의 처리**

### 21.파일 업로드 방식

▣ 일반적인 파일 업로드 처리 방식

• <form> 태그를 이용하는 방식: 브라우저의 제한이 없어야 하는 경우에 사용   
◦ 일반적으로 페이지 이동과 동시에 첨부파일을 업로드하는 방식   
◦ <iframe>을 이용해서 화면의 이동 없이 첨부파일을 처리하는 방식

• Ajax를 이용하는 방식: 첨부파일을 별도로 처리하는 방식   
◦ <input type=’file’>을 이용하고 Ajax로 처리하는 방식     
◦ Drag And Drop이나 jQuery 라이브러리들을 이용해서 처리하는 방식

▣ 파일 업로드 라이브러리들
• cos.jar: 2002년도 이후에 개발이 종료되었으므로, **더 이상 사용하는 것을 권장하지 않음**  
• commons-fileupload: 가장 일반적으로 많이 활용되고, 서블릿 스펙 3.0 이전에도 사용 가능  
• 서블릿3.0 이상 – 3.0 이상부터는 자체적인 파일 업로드 처리가 API 상에서 지원

▣ Servlet 3.0 이상의 경우 web.xml설정  
• 어노테이션 설정 혹은 web.xml 의 설정

``` 
    <!-- servlet-context.xml 에 설정 해 도 됨 -->
    <multipart-config>
      <location>C:\\upload\\temp</location>
      <max-file-size>20971520</max-file-size> <!--1MB * 20 -->
      <max-request-size>41943040</max-request-size><!-- 40MB -->
      <file-size-threshold>20971520</file-size-threshold> <!-- 20MB -->
    </multipart-config>
```

▣  servlet-context.xml의 설정  
• multipartResolver 설정
```
<!-- part6: 파일 업로드를 위한 multipartResolver -->
    <beans:bean id="multipartResolver"
                class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <beans:property name="defaultEncoding" value="utf-8"></beans:property>
        <!-- 1024 * 1024 * 10 bytes  10MB -->
        <beans:property name="maxUploadSize" value="104857560"></beans:property>
        <!-- 1024 * 1024 * 2 bytes  2MB -->
        <beans:property name="maxUploadSizePerFile" value="2097152"></beans:property>
        <beans:property name="uploadTempDir" value ="file:/C:/upload/tmp"></beans:property>
        <beans:property name="maxInMemorySize" value="10485756"></beans:property>
    </beans:bean>

```
▣  form 방식
``` 
<!-- part6 파일업로드 --> 
<form action="uploadFormAction" method="post" enctype="multipart/form-data">
    <input type='file' name='uploadFile' multiple>
    <button>Submit</button>
</form>
```
**multiple 은 브라우저의 버전의 제약이 있음**

▣  Multipart 타입

```
    // part 6 업로드 파일 Multipart 타입
    @PostMapping("/uploadFormAction")
    public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
        String uploadFolder = "C:\\upload";

        for (MultipartFile multipartFile : uploadFile) {
            log.info("-------------------------------------");
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            log.info("Upload File Size: " + multipartFile.getSize());

            File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

            try {
                multipartFile.transferTo(saveFile);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    } 
```

▣ Ajax를 이용한 파일 업로드

• Ajax 를 이용하는 경우에는 FormData 객체를  이용  
• FormData 역시 브라우저별로 지원 여부 확인

```  
<script>
$(document).ready(function(){ 
  // part6 업로드된 파일 목록을 HTML li 요소로 생성  
  $("#uploadBtn").on("click", function(e){
    var formData = new FormData();    
    var inputFile = $("input[name='uploadFile']");    
    var files = inputFile[0].files;    
    console.log(files);    
  });  
});
</script>  
```
▣ jQuery 를 이용한 파일 전송 

```
// Part6 jQuery 를 이용한 파일 전송
$.ajax({
url: '/uploadAjaxAction',
processData: false,
contentType: false,
data: formData,
type: 'POST',
success: function(result){
alert("Uploaded");
}
}); //$.ajax
```
### 22. 파일 업로드 상세 처리  

▣ 파일의 확장자나 크기 사전처리  

 • 정규식을 이용해서 파일 확장자 체크 

```
// Part6 파일의 확장자나 크기 사전처리 
var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
var maxSize = 5242880; //5MB

function checkExtension(fileName, fileSize){
if(fileSize >= maxSize){
alert("파일 사이즈 초과");
return false;
}
if(regex.test(fileName)){
alert("해당 종류의 파일은 업로드할 수 없습니다.");
return false;
}
return true;
}
```
▣ 중복된 이름의 파일 처리  

```
private String getFolder() {
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   Date date = new Date();
   String str = sdf.format(date);
   return str.replace("-", File.separator);
}

// make folder --------
File uploadPath = new File(uploadFolder, getFolder());
log.info("upload path: " + uploadPath);

UUID uuid = UUID.randomUUID();
 
  uploadFileName = uuid.toString() + "_" + uploadFileName;
 
  File saveFile = new File(uploadPath, uploadFileName);
 
    try {
 
      multipartFile.transferTo(saveFile);
} catch (Exception e) {
log.error(e.getMessage());
} // end catch


```

▣ 섬네일(thumbnail) 이미지 생성 

```
  <!-- Part6 섬네일(thumbnail) 이미지 생성 -->
  <dependency>
      <groupId>net.coobird</groupId>
      <artifactId>thumbnailator</artifactId>
      <version>0.4.8</version>
  </dependency>
```

▣ 섬네일을 처리하는 단계

```
try {
      String contentType = Files.probeContentType(file.toPath());
 
      return contentType.startsWith("image");
 
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
```

▣ 업로드 된 파일의 데이터 반환

```
// Part6 업로드 된 파일의 데이터 반환 
@Data
public class AttachFileDTO { 
  private String fileName;
  private String uploadPath;
  private String uuid;
  private boolean image; 
}
```

▣ 브라우저에서 Ajax의 처리

```
$.ajax({
  url: '/uploadAjaxAction',
  processData: false, 
  contentType: false,
  data: formData,
    type: 'POST',
    dataType:'json',
    success: function(result){
       console.log(result);
    }
}); //$.ajax 


$.ajax({
    url : '/uploadAjaxAction',
    processData : false,
    contentType : false,
    data : formData,
    type : 'POST',
    dataType : 'json',
    success : function(result) { 
      console.log(result); 
      showUploadedFile (result); 
      $(".uploadDiv").html(cloneObj.html());
 
    }
}); //$.ajax


function showUploadedFile(uploadResultArr) {
  var str = "";
  $(uploadResultArr).each(function(i, obj) {
    str += "<li>" + obj.fileName + "</li>";
  });
  uploadResult.append(str);
}

```

▣ 섬네일 이미지 보여주기
• 파일의 확장자에 따라 적당한 MIME타입 데이터를 지정

```
    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName) {
        log.info("fileName: " + fileName);

        File file = new File("c:\\upload\\" + fileName);
        log.info("file: " + file);

        ResponseEntity<byte[]> result = null;
        // Part6 파일의 확장자에 따라 적당한 MIME 타입 데이터를 지정
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

```
▣ 첨부파일의 다운로드

• 일반 파일의 경우 클릭 시에 다운로드 처리

```
  // Part6 첨부파일의 다운로드
  @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  @ResponseBody
  public ResponseEntity<Resource> downloadFile (String fileName) {
 
    log.info("download file: " + fileName); 
    Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
 
    log.info("resource: " + resource);
    String resourceName = resource.getFilename(); 
    HttpHeaders headers = new HttpHeaders();
    try {
      headers.add("Content-Disposition",
          "attachment; filename=" + new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
  }
```

• IE의 경우에 한글 파일 이름 처리 주의

```        
  boolean checkIE = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);
 
  String downloadName = null;
 
  if (checkIE) {
    downloadName = URLEncoder.encode(resourceName, "UTF8").replaceAll("\\+", " ");
  } else {
    downloadName = new String(resourceName.getBytes("UTF-8"), "ISO-8859-1");
  }
```

▣ 원본 이미지 보여주기
• <div>를 이용해서 화면 내에 원본 이미지를 보여주도록 처리
• jQuery의 animate( )를 이용해서 처리
```
  $(".bigPictureWrapper").css("display","flex").show();
  
  $(".bigPicture")
  .html("<img src='/display?fileName="+fileCallPath+"'>")
  .animate({width:'100%', height: '100%'}, 1000);
```

▣ 첨부파일의 삭제


▣ 등록을 위한 화면 처리

```
-- Oracle
create table tbl_attach (
  uuid       varchar2(100) not null,
  uploadPath varchar2(200) not null,
  fileName   varchar2(100) not null,
  filetype   char(1) default 'I',
  bno number(10,0)
);

alter table tbl_attach
add constraint pk_attach primary key (uuid);

alter table tbl_attach
add constraint fk_board_attach foreign key (bno) references tbl_board(bno);

[BoardVO.java]
@Data
public class BoardVO {

  private Long bno;   // oracle number
  private String title;
  private String content;
  private String writer;
  private Date regdate;
  private Date updateDate;

  // Part6 여러 개의 첨부 파일을 가지도록
  private List<BoardAttachVO> attachList;
}

[BoardAttachVO.java]
@Data
public class BoardAttachVO {
  private String uuid;
  private String uploadPath;
  private String fileName;
  private boolean fileType;
  // Part6 파일 정보들을 BoardAttachVO로 변환
  private Long bno;
}

[BoardServiceImpl.java] 

  @Transactional // Part6
	@Override
	public void register(BoardVO board) {

		log.info("register......{}", board);

		boardMapper.insertSelectKey(board);
		// Part4
		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		// Part6 트랜잭션하에 여러 개의 첨부 파일 정보도 DB에 저장
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}
```

▣ 게시물의 조회와 첨부파일

• 첨부파일은 사실상 수정이라는 개념이 존재하지 않음
• 삭제후 다시 추가하는 방식


```
  @GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {
 
    log.info("getAttachList " + bno);
 
    return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
 
  }

 
  var bno = '<c:out value="${board.bno}"/>';
    
  $.getJSON("/board/getAttachList", {bno: bno}, function(arr){
    console.log(arr);        
  });//end getjson

```

▣ Quartz라이브러리 설정

  <!-- https://mvnrepository.com/artifact/org.quartz-scheduler/quartz -->
  <dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.3.0</version>
  </dependency>
 
 
  <!-- https://mvnrepository.com/artifact/org.quartz-scheduler/quartz-jobs -->
  <dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz-jobs</artifactId>
    <version>2.3.0</version>
  </dependency>

▣ Task설정  
[root-context.xml]
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:task="http://www.springframework.org/schema/task"
       xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
		http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd
        ">
<task:annotation-driven/>
```

▣ cron 설정




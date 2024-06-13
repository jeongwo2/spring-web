<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- ex06 security -->
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- ex02 register.jsp [등록] 페이지-->
<%@include file="../includes/header.jsp"%>

<!--ex05 -->
<style>
    .uploadResult {
        width: 100%;
        background-color: gray;
    }

    .uploadResult ul {
        display: flex;
        flex-flow: row;
        justify-content: center;
        align-items: center;
    }

    .uploadResult ul li {
        list-style: none;
        padding: 10px;
    }

    .uploadResult ul li img {
        width: 100px;
    }
</style>

<style>
    .bigPictureWrapper {
      position: absolute;
      display: none;
      justify-content: center;
      align-items: center;
      top:0%;
      width:100%;
      height:100%;
      background-color: gray;
      z-index: 100;
    }

    .bigPicture {
      position: relative;
      display:flex;
      justify-content: center;
      align-items: center;
    }
</style>

<!-- ex02 -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Board Register[등록]</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<!-- ex02 div#게시글 등록 -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Register[등록]</div>
            <!-- /.panel-heading -->
            <div class="panel-body">

                <form role="form" action="/board/register" method="post">
                    <!-- ex06 -->
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <label>Title</label>
                        <input class="form-control" name='title'>
                    </div>

                    <div class="form-group">
                        <label>Text area</label>
                        <textarea class="form-control" rows="3" name='content'></textarea>
                    </div>

                    <div class="form-group">
                        <label>Writer</label>
<!--                        <input class="form-control" name='writer'>   ex06 -->
                        <input class="form-control" name='writer'
                               value='<sec:authentication property="principal.username"/>' readonly="readonly">
                    </div>

                    <button type="submit" class="btn btn-default">Submit Button</button>
                    <button type="reset" class="btn btn-default">Reset Button</button>
                </form>
            </div>
            <!--  end panel-body -->
        </div>
        <!--  end panel-body -->
    </div>
    <!-- end panel -->
</div>
<!-- /.row -->

<!-- ex05: 파일 업로드 -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">File Attach</div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div class="form-group uploadDiv">
                    <input type="file" name='uploadFile' multiple>
                </div>
                <div class='uploadResult'>
                    <ul>
                    </ul>
                </div>
            </div>
            <!--  end panel-body -->
        </div>
        <!--  end panel-body -->
    </div>
    <!-- end panel -->
</div>
<!-- /.row -->

<!--ex05: JQuery 파일 업로드, 삭제, 게시글 등록 기능 -->
<script>
    $(document).ready(function(e){
      var formObj = $("form[role='form']");

      // 게시글 등록 버튼을 클릭할 때
      $("button[type='submit']").on("click", function(e){
        e.preventDefault();  // 기본 이벤트 동작을 취소
        console.log("submit clicked");

        var str = "";
        // 업로드된 파일 목록을 순회
        $(".uploadResult ul li").each(function(i, obj){
          var jobj = $(obj); // jQuery 객체로 변환
          console.dir(jobj);
          console.log(jobj.data("filename"));

          // input 태그에 hidden 속성으로 파일 정보를 추가
          str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
          str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
          str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
          str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
        });
        console.log(str);

        formObj.append(str).submit(); // 폼에 hidden 속성으로 파일 정보를 추가하고 제출
      });

      // 파일 확장자, 크기 검사
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

       <!--ex06 -->
       var csrfHeaderName ="${_csrf.headerName}";
       var csrfTokenValue="${_csrf.token}";

      // 파일 업로드
      $("input[type='file']").change(function(e){
        var formData = new FormData();
        var inputFile = $("input[name='uploadFile']");
        var files = inputFile[0].files;
        // 선택된 파일 목록을 순회
        for(var i = 0; i < files.length; i++){
          if(!checkExtension(files[i].name, files[i].size) ){
            return false;
          }
          formData.append("uploadFile", files[i]);
        }

        // 파일 업로드 AJAX 요청- Part6 변경 브라우저에서 Ajax 의 처리
        $.ajax({
          url: '/uploadAjaxAction',
          processData: false,
          contentType: false, data:
          beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
          },
          data:formData,
          formData,type: 'POST',
          dataType:'json',
              success: function(result){
              console.log(result);
              showUploadResult(result); //업로드 결과 처리 함수
          }
        }); //$.ajax
      });

      // 업로드된 파일 목록을 화면에 표시
      function showUploadResult(uploadResultArr){
        if(!uploadResultArr || uploadResultArr.length == 0){ return; }

        var uploadUL = $(".uploadResult ul");
        var str ="";
        // 업로드된 파일 목록을 순회
        $(uploadResultArr).each(function(i, obj){
            // Part6 화면에서의 다운로드 처리
            if(obj.image){ // 이미지 파일인 경우
                var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);

                str += "<li data-path='"+obj.uploadPath+"'";
                str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'"
                str +" ><div>";
                str += "<span> "+ obj.fileName+"</span>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' "
                str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/display?fileName="+fileCallPath+"'>";
                str += "</div>";
                str +"</li>";
            }else{
                // 이미지 파일이 아닌 경우
                var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);
                var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");

                str += "<li "
                str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' ><div>";
                str += "<span> "+ obj.fileName+"</span>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' "
                str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/resources/img/attach.png'></a>";
                str += "</div>";
                str +"</li>";
            }
        });
        uploadUL.append(str);
      }

      // 업로드된 파일 삭제
      $(".uploadResult").on("click", "button", function(e){
        console.log("delete file");

        var targetFile = $(this).data("file");
        var type = $(this).data("type");
        var targetLi = $(this).closest("li");

        // 파일 삭제 AJAX 요청 - beforeSend 추가
        $.ajax({
          url: '/deleteFile',
          data: {fileName: targetFile, type:type},
          beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
          },
          dataType:'text',
          type: 'POST',
            success: function(result){
               alert(result);
               targetLi.remove(); // 화면에서 파일 목록 제거
             }
        }); //$.ajax
       });
    });
</script>

<!--ex05: JQuery 파일 업로드, 삭제, 게시글 등록 기능 -->
<%@include file="../includes/footer.jsp"%>

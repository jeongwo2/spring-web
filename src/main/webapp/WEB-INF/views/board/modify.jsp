<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- ex06 security -->
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!-- ex02 modify.jsp : [수정/삭제] 페이지 -->
<%@include file="../includes/header.jsp"%>

<!--ex05 : style-->
<style>
    .uploadResult {
      width:100%;
      background-color: gray;
    }
    .uploadResult ul{
      display:flex;
      flex-flow: row;
      justify-content: center;
      align-items: center;
    }
    .uploadResult ul li {
      list-style: none;
      padding: 10px;
      align-content: center;
      text-align: center;
    }
    .uploadResult ul li img{
      width: 100px;
    }
    .uploadResult ul li span {
      color:white;
    }
    /* Style for the big picture wrapper */
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
      background:rgba(255,255,255,0.5);
    }
    .bigPicture {
      position: relative;
      display:flex;
      justify-content: center;
      align-items: center;
    }
    .bigPicture img {
      width:600px;
    }
</style>

<!-- Row for the page header -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Board Modify[게시글 수정]</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<!-- div#게시글 수정 -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Modify[수정]</div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <!-- Form to modify the board -->
                <form role="form" action="/board/modify" method="post">
                    <!-- Hidden inputs for page number, amount, type, keyword, and board number
                    <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
                    <input type='hidden' name='amount' value='<c:out value="${cri.pagePerNum }"/>'>
                    <input type='hidden' name='type' value='<c:out value="${cri.type }"/>'>
                    <input type='hidden' name='keyword' value='<c:out value="${cri.keyword }"/>'>
                    ex06 -->
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
                    <input type='hidden' name='amount' 	value='<c:out value="${cri.pagePerNum }"/>'>
                    <input type='hidden' name='type'    value='<c:out value="${cri.type }"/>'>
                    <input type='hidden' name='keyword' value='<c:out value="${cri.keyword }"/>'>

                    <div class="form-group">
                        <label>Bno</label>
                        <input class="form-control" name='bno' value='<c:out value="${board.bno }"/>' readonly="readonly">
                    </div>

                    <div class="form-group">
                        <label>Title</label>
                        <input class="form-control" name='title' value='<c:out value="${board.title }"/>'>
                    </div>

                    <div class="form-group">
                        <label>Text area</label>
                        <textarea class="form-control" rows="3" name='content'><c:out value="${board.content}"/></textarea>
                    </div>

                    <div class="form-group">
                        <label>Writer</label>
                        <input class="form-control" name='writer' value='<c:out value="${board.writer}"/>' readonly="readonly">
                    </div>

                    <div class="form-group">
                        <label>RegDate</label>
                        <input class="form-control" name='regDate'
                               value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.regdate}" />'
                               readonly="readonly">
                    </div>

                    <div class="form-group">
                        <label>Update Date</label>
                        <input class="form-control" name='updateDate'
                               value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.updateDate}" />'
                               readonly="readonly">
                    </div>
                    <!-- Buttons for modifying, removing, and returning to the list
                    <button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
                    <button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
                    -->
                    <!-- ex06 -->
                    <sec:authentication property="principal" var="pinfo" />
                    <sec:authorize access="isAuthenticated()">
                        <c:if test="${pinfo.username eq board.writer}">

                            <button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
                            <button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
                        </c:if>
                    </sec:authorize>
                    <button type="submit" data-oper='list' class="btn btn-info">List</button>
                </form>
            </div>
            <!--  end panel-body -->
        </div>
        <!--  end panel-body -->
    </div>
    <!-- end panel -->
</div>
<!-- /.row -->

<!-- ex05 -->
<div class='bigPictureWrapper'>
    <div class='bigPicture'>
    </div>
</div>

<!-- ex05 uploadFile -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Files</div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div class="form-group uploadDiv">
                    <input type="file" name='uploadFile' multiple="multiple">
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


<!--ex03 jQuery: 게시물의 수정/삭제 -->
<script type="text/javascript">
    $(document).ready(function() {
        // 즉시 실행 함수를 이용해서 첨부파일 목록 가져오기
        var formObj = $("form");
        // Event handler for form buttons
        $('button').on("click", function(e){
            e.preventDefault();

            var operation = $(this).data("oper");
            console.log(operation);

            // Perform different actions based on the button clicked
            if(operation === 'remove'){
              formObj.attr("action", "/board/remove");
            }else if(operation === 'list'){
              // Move to list
                formObj.attr("action", "/board/list").attr("method","get");

              // Clone and append hidden inputs for page number, amount, type, and keyword
                var pageNumTag = $("input[name='pageNum']").clone();
                var amountTag = $("input[name='amount']").clone();
                var keywordTag = $("input[name='keyword']").clone();
                var typeTag = $("input[name='type']").clone();

                formObj.empty();

                formObj.append(pageNumTag);
                formObj.append(amountTag);
                formObj.append(keywordTag);
                formObj.append(typeTag);
                <!-- ex06 -->
            }else if(operation === 'modify'){
                console.log("submit clicked");
                var str = "";

                $(".uploadResult ul li").each(function(i, obj){
                    var jobj = $(obj);
                    console.dir(jobj);
                    str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
                    str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
                    str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
                    str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
	            });
	            formObj.append(str).submit();
            }
            formObj.submit();
        });
    });
</script>
<!--ex03 jQuery: 게시물의 수정/삭제 -->

<!-- ex05 jQuery: 첨부파일 원본 이미지 보기  -->
<script>
    $(document).ready(function() {
      (function(){
        // Get the board's bno value
        var bno = '<c:out value="${board.bno}"/>';  // Board number

        // Get the list of attached files
        $.getJSON("/board/getAttachList", {bno: bno}, function(arr){
          console.log(arr);

          var str = ""; // 초기화
          // Loop through each attachment in the received list
          $(arr).each(function(i, attach){
              // If the file is an image
              if(attach.fileType){
                 // Create a file call path for the image
                var fileCallPath =  encodeURIComponent( attach.uploadPath+ "/s_"+attach.uuid +"_"+attach.fileName);

                // Append an HTML list item with the image and buttons to the string
                str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' "
                str +=" data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
                str += "<span> "+ attach.fileName+"</span>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' "
                str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/display?fileName="+fileCallPath+"'>";
                str += "</div>";
                str +"</li>";
              }else{
                // If the file is not an image
                var fileCallPath =  encodeURIComponent( attach.uploadPath+"/"+ attach.uuid +"_"+attach.fileName);
                var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");

                // Append an HTML list item with the file icon and buttons to the string
                str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' "
                str += "data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
                str += "<span> "+ attach.fileName+"</span><br/>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' "
                str += " class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/resources/img/attach.png'></a>";
                str += "</div>";
                str +"</li>";
              }
           });
           // Append the generated HTML list items to the uploadResult ul
          $(".uploadResult ul").html(str);
        });//end getjson
      })();//end function

      // Event listener for the delete file button
      $(".uploadResult").on("click", "button", function(e){
        console.log("delete file");

        if(confirm("Remove this file? ")){
           // Get the parent list item of the clicked button
           var targetLi = $(this).closest("li");
           targetLi.remove();
        }
      });
      // Regular expression for checking file extensions
      var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
      var maxSize = 5242880; //5MB

      // Function to check file extension and size
      function checkExtension(fileName, fileSize){
        // Check if the file size exceeds the maximum allowed size
        if(fileSize >= maxSize){
          alert("파일 사이즈 초과");
          return false;
        }
        // Check if the file extension matches the regular expression
        if(regex.test(fileName)){
          alert("해당 종류의 파일은 업로드할 수 없습니다.");
          return false;
        }
        return true;
      }
      <!-- ex06 -->
      var csrfHeaderName ="${_csrf.headerName}";
      var csrfTokenValue="${_csrf.token}";

      // Event listener for the file input field
      $("input[type='file']").change(function(e){

        var formData = new FormData();  // Initialize
        var inputFile = $("input[name='uploadFile']");
        var files = inputFile[0].files;

        // Loop through each selected file
        for(var i = 0; i < files.length; i++){
          if(!checkExtension(files[i].name, files[i].size) ){
            return false;
          }
          formData.append("uploadFile", files[i]);
        }

        // Send an AJAX request to upload the files- Part6 jQuery 를 이용한 파일 전송
        $.ajax({
            url: '/uploadAjaxAction',
            processData: false,
            contentType: false,data:
            formData,type: 'POST',
            dataType:'json',
            success: function(result){
              console.log(result);
              showUploadResult(result); //업로드 결과 처리 함수
            }
        }); //$.ajax
      });

      // Function to display the upload result
      function showUploadResult(uploadResultArr){
        if(!uploadResultArr || uploadResultArr.length == 0){ return; }

        // Get the uploadResult ul element
        var uploadUL = $(".uploadResult ul");
        var str =""; // Initialize

        // Loop through each item in the upload result array
        $(uploadResultArr).each(function(i, obj){
            // Check if the item is an image
            if(obj.image){
                // Create a file call path for the image
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
                // Create a file call path for the file
                var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);
                var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");

                // Append an HTML list item with the file icon and buttons to the string
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
        // Append the generated HTML list items to the uploadResult ul
        uploadUL.append(str);
      }
    });
</script>

<%@include file="../includes/footer.jsp"%>

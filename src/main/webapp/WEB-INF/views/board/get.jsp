<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- ex06 security -->
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!-- ex02 get.jsp -->
<%@include file="../includes/header.jsp"%>

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

<%-- 주석 설명
   . <%  : 브라우저에서도 주석 처리
   . <!  : HTML 주석, 브라우저에 주석처리 되지 않음
   . //  : Java 주석, 스크립트, 표현식, 선언부 안에서만 주석처리
--%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Board Read</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">

            <div class="panel-heading">Board Read Page</div>
            <!-- /.panel-heading -->
            <div class="panel-body">

                <div class="form-group">
                    <label>Bno</label>
                    <input class="form-control" name='bno'
                           value='<c:out value="${board.bno }"/>' readonly="readonly">
                </div>

                <div class="form-group">
                    <label>Title</label>
                    <input class="form-control" name='title'
                           value='<c:out value="${board.title }"/>' readonly="readonly">
                </div>

                <div class="form-group">
                    <label>Text area</label>
                    <textarea class="form-control" rows="3" name='content'
                              readonly="readonly"><c:out value="${board.content}" />
                    </textarea>
                </div>

                <div class="form-group">
                    <label>Writer</label>
                    <input class="form-control" name='writer'
                           value='<c:out value="${board.writer }"/>' readonly="readonly">
                </div>
                <%-- <!--
                <button data-oper='modify' class="btn btn-default">
                    <a href="/board/modify?bno=<c:out value=" ${board.bno}"/>">Modify</a></button>
                <button data-oper='list' class="btn btn-info">
                    <a href="/board/list">List</a></button>
                --> --%>
                <!-- ex06 -->
                <sec:authentication property="principal" var="pinfo"/>
                <sec:authorize access="isAuthenticated()">
                    <c:if test="${pinfo.username eq board.writer}">
                        <button data-oper='modify' class="btn btn-default">Modify</button>
                    </c:if>
                </sec:authorize> <!-- ex06 -->

                <button data-oper='list' class="btn btn-info">List</button>
                <%--   <!--
                <form id='operForm' action="/boad/modify" method="get">
                    <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
                </form>
                amount rename to pagePerNum -->
                --%>
                <!-- part3: 전달받은 페이지 번호를 이용해서 원래 페이지로 이동 -->
                <form id='operForm' action="/board/modify" method="get">
                    <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
                    <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>
                    <input type='hidden' name='amount' value='<c:out value="${cri.pagePerNum}"/>'>
                    <input type='hidden' name='keyword' value='<c:out value="${cri.keyword}"/>'>
                    <input type='hidden' name='type' value='<c:out value="${cri.type}"/>'>
                </form>
            </div>
            <!--  end panel-body -->
        </div>
        <!--  end panel-body -->
    </div>
    <!-- end panel -->
</div>
<!-- /.row -->

<!--ex06: bigPicture -->
<div class='bigPictureWrapper'>
    <div class='bigPicture'>
    </div>
</div>
<!-- uploadResult -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">Files</div>
            <!-- /.panel-heading -->
            <div class="panel-body">
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
<!-- ex06: Spring Security 를 사용하여 인증된 사용자만 게시판 글에 대한 새로운 답변을 추가할 수 있는 버튼을 표시 -->
<div class='row'>
    <div class="col-lg-12">
        <!-- /.panel -->
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-comments fa-fw"></i> Reply
                <!--ex06: Spring Security 사용자가 인증된 경우에만 콘텐츠를 렌더링   -->
                <sec:authorize access="isAuthenticated()">
                    <button id='addReplyBtn' class='btn btn-primary btn-xs pull-right'>New Reply[새글]</button>
                </sec:authorize> <!--ex06 -->
            </div>
            <!-- part4 댓글의 페이지 번호 처리 -->
            <!-- /.panel-heading 기존에 존재하는 부분 -->
            <div class="panel-body">
                <ul class="chat">
                </ul>
                <!-- ./ end ul -->
            </div>
            <!-- /.panel .chat-panel 기존에 존재하는 부분 -->
            <div class="panel-footer"></div>
        </div>
    </div>
    <!-- ./ end row -->
</div>
<!-- ex06: 답변추가 버튼 -->

<!-- ex03 modal =========-->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <!-- modal-dialog -->
    <div class="modal-dialog">
        <!-- modal-content -->
        <div class="modal-content">
            <!-- modal-header -->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
            </div>
            <!-- modal-body -->
            <div class="modal-body">
                <div class="form-group">
                    <label>Reply</label> <input class="form-control" name='reply'
                                                value='New Reply!!!!'>
                </div>
                <div class="form-group">
                    <label>Replyer</label> <input class="form-control" name='replyer'
                                                  value='replyer'>
                </div>
                <div class="form-group">
                    <label>Reply Date</label> <input class="form-control"
                                                     name='replyDate' value='2018-01-01 13:13'>
                </div>
            </div>
            <!-- modal-footer -->
            <div class="modal-footer">
                <button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
                <button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
                <button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
                <button id='modalCloseBtn' type="button" class="btn btn-default">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- ./modal-dialog -->
</div>
<!-- /.modal ======================================-->

<!--ex02 jQuery : 게시글 수정과 목록으로 돌아가기 -->
<script type="text/javascript">
    $(document).ready(function() { // 페이지가 로딩-DOM 이 로드이 완료 되었을 때
      // 게시글을 수정-
      var operForm = $("#operForm"); // operForm 인 HTML 요소를 jQuery 객체로 찾아서 저장
      // 속성 값이 modify 인 모든 버튼에 대해 클릭 이벤트 리스너를 설정
      $("button[data-oper='modify']").on("click", function(e){
        // 클릭 이벤트가 발생하면, operForm 의 action 속성을 변경
        operForm.attr("action","/board/modify").submit();
      });

      // 속성 값이 list 인 모든 버튼에 대해 클릭 이벤트 리스너-[목록으로 돌아가기]
      $("button[data-oper='list']").on("click", function(e){
        operForm.find("#bno").remove(); // 폼 내에서 ID가 bno인 HTML 요소를 찾아, 특정 데이터를 제출 전에 폼에서 제거
        operForm.attr("action","/board/list") //폼 데이터가 서버의 /board/list 경로로 전송
        operForm.submit(); //  사용자가 버튼을 클릭하면 폼을 제출
      });
    });
</script>
<!--ex02 javascript -->

<!-- part4 ex03 조회 화면에서 reply 호출 ==============================================================-->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<!-- part4 ex03 ./reply ============================================================-->

<!-- ex03 jQuery 게시판 세부 페이지에서 댓글 기능 -->
<script>
    $(document).ready(function () { // 문서가 완전히 로드된 후에 JavaScript 코드를 실행
        var bnoValue = '<c:out value="${board.bno}"/>'; // EL 표현식으고 게시판 번호를 가져옴
        var replyUL = $(".chat"); // HTML 요소 .chat 을 선택하여 댓글 목록을 표시

        showList(1); // 첫 페이지의 댓글을 초기 표시

        // 서버에서 댓글 목록을 가져온후 <li>태그를 만들어서 화면에 보여준다.
        function showList(page){
            console.log("show list " + page);
            // 서버에서 댓글 목록을 가져오기
            replyService.getList({bno:bnoValue, page: page|| 1}, function(replyCnt, list) {
                console.log("replyCnt: " + replyCnt);

                if (page == -1) {
                    pageNum = Math.ceil(replyCnt / 10.0);
                    showList(pageNum);
                    return;
                }

                var str = ""; // Initialize an empty string
                if (list == null || list.length == 0) {
                    return;
                }
                // 게시글의 댓글을 <li>태그를 만들어서 화면에 보여준다.
                for (var i = 0, len = list.length || 0; i < len; i++) {
                    str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>"; // 수정이나 삭제 시에는 반드시 댓글의 번호(rno)가 필요
                    str += "  <div><div class='header'><strong class='primary-font'>["
                        +  list[i].rno + "] " + list[i].replyer + "</strong>";
                    str += "    <small class='pull-right text-muted'>"
                        +  replyService.displayTime(list[i].replyDate) + "</small></div>";
                    str += "    <p>" + list[i].reply + "</p></div></li>";
                }

                replyUL.html(str);
                // 총 댓글 수(replyCnt)를 기반으로 페이지네이션 버튼 HTML 을 생성
                showReplyPage(replyCnt);

            });//end function
        }
        //showList end

        var pageNum = 1;
        var replyPageFooter = $(".panel-footer"); // 페이지네이션 버튼 이벤트를 처리

    /**페이지네이션을 처리
    * This function is responsible for displaying the pagination buttons for the reply list.
    * It calculates the start and end page numbers, and generates the HTML for the pagination buttons.
    *
    * @param replyCnt The total number of replies.(총 댓글 수)
    */
    function showReplyPage(replyCnt) {
        var endNum = Math.ceil(pageNum / 10.0) * 10; // 마지막페이지
        var startNum = endNum - 9; // 시작페이지

        var prev = startNum != 1; // 이전페이지는 시작페이지가 1이 아닌 경우 true
        var next = false; //

        if (endNum * 10 >= replyCnt) {
           endNum = Math.ceil(replyCnt / 10.0);
        }
        // next 는 마지막 페이지가 총 댓글 수를 10으로 나눈 후 올림한 값보다 작은 경우 true
        if (endNum * 10 < replyCnt) {
           next = true;
        }
        <!-- 화면 번호 출력 -->
        var str = "<ul class='pagination pull-right'>";
        // prev 가 true 인 경우에 이전 페이지 버튼
        if (prev) {
            str += "<li class='page-item'><a class='page-link' href='" + (startNum - 1) + "'>Previous</a></li>";
        }
        // Add the page number buttons
        for (var i = startNum; i <= endNum; i++) {
            var active = pageNum == i ? "active" : "";
            str += "<li class='page-item " + active + " '><a class='page-link' href='" + i + "'>" + i + "</a></li>";
       }
       // next 가 true 인 경우에 다음 페이지 버튼
       if (next) {
           str += "<li class='page-item'><a class='page-link' href='" + (endNum + 1) + "'>Next</a></li>";
       }
       // Close the pagination buttons HTML
       str += "</ul></div>";
       console.log(str);
       // HTML 요소에 str 을 추가하여 pagination buttons 을 보이도록
       replyPageFooter.html(str);
    }
    <!-- showReplyPage end  -->

    // 클릭 이벤트가 발생하면 showList(pageNum) 함수를 호출하여 해당 페이지의 댓글을 표시
    replyPageFooter.on("click", "li a", function(e) {
        e.preventDefault();
        console.log("page click");

        var targetPageNum = $(this).attr("href");
        console.log("targetPageNum: " + targetPageNum);

        pageNum = targetPageNum;
        showList(pageNum);
    });
    // replyPageFooter end

    var modal = $(".modal"); // 모달 창을 선택
    // 모달 창에서 댓글 입력 필드를 선택
    var modalInputReply = modal.find("input[name='reply']");
    var modalInputReplyer = modal.find("input[name='replyer']");
    var modalInputReplyDate = modal.find("input[name='replyDate']");
    // 모달 창에서 버튼을 선택
    var modalModBtn = $("#modalModBtn");
    var modalRemoveBtn = $("#modalRemoveBtn");
    var modalRegisterBtn = $("#modalRegisterBtn");

    // 모달 창을 닫기 위한 이벤트 리스너
    $("#modalCloseBtn").on("click", function(e) {
        modal.modal('hide');
    });

    // 새 댓글을 추가하기 위한 이벤트 리스너
    $("#addReplyBtn").on("click", function(e) {
        // Clear the input fields
        modal.find("input").val("");
        // ex06
        modal.find("input[name='replyer']").val(replyer);
        modalInputReplyDate.closest("div").hide();
        modal.find("button[id !='modalCloseBtn']").hide();

        // Show the register button
        modalRegisterBtn.show();
        // Show the modal
        $(".modal").modal("show");
    });

    // ex06: Add AJAX 요청을 보내기 전에 CSRF 헤더를 설정
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
    });

    // part4 새 댓글을 추가 후 서버에 저장하기 위한 등록 이벤트 리스너
    modalRegisterBtn.on("click", function(e) {
        var reply = {
            reply: modalInputReply.val(),
            replyer: modalInputReplyer.val(),
            bno: bnoValue
        };
         // 새로운 댓글을 추가하면 page 값을 -1로 전송하고, 댓글의 전체 숫자를 파악한 후에 페이지 이동
        replyService.add(reply, function(result) {
            console.log(result);

            modal.find("input").val("");
            modal.modal("hide");
            // Refresh the reply list
            showList(-1);  // page 값을 -1로 전송
        });
    });
    // modalRegisterBtn end

    // 댓글 조회를 클릭하여 세부 정보를 모달 창에 표시하기 위한 이벤트 리스너
    $(".chat").on("click", "li", function(e) {
        var rno = $(this).data("rno");
        // 특정댓글 조회
        replyService.get(rno, function(reply) {
            console.log(reply);
            // Set the reply and replyer values in the modal
            modalInputReply.val(reply.reply);
            modalInputReplyer.val(reply.replyer);
            modalInputReplyDate.val(replyService.displayTime(reply.replyDate)).attr("readonly", "readonly");

            modal.data("rno", reply.rno);
            modal.find("button[id !='modalCloseBtn']").hide();
            // Show the modify and remove buttons
            modalModBtn.show();
            modalRemoveBtn.show();

            $(".modal").modal("show");
        }); // replyService
    }); //댓글 조회 클릭 이벤트 처리

    // 기존 댓글을 수정하기 위한 이벤트 리스너
    modalModBtn.on("click", function(e) {
        var originalReplyer = modalInputReplyer.val();
         // Get the reply number from the data attribute
        var rno = modal.data("rno");
        console.log("Original Replyer: " + originalReplyer);

        var reply = {
            rno: modal.data("rno"),
            reply: modalInputReply.val()
        };
        //ex 06
        if(!replyer){
             alert("로그인후 수정이 가능합니다.");
             modal.modal("hide");
             return;
        }
        if(replyer  != originalReplyer){
             alert("자신이 작성한 댓글만 수정이 가능합니다.");
             modal.modal("hide");
             return;
        }
        // 댓글 수정
        replyService.update(reply, function(result) {
            console.log(result);

            modal.modal("hide");
            showList(pageNum);
        });
    }); // modalModBtn end

    // 기존 댓글을 삭제하기 위한 이벤트 리스
    modalRemoveBtn.on("click", function(e) {
      var rno = modal.data("rno");
      console.log("RNO: " + rno);
      //ex06
      if(!replyer){
         alert("로그인후 삭제가 가능합니다.");
         modal.modal("hide");
         return;
      }
      var originalReplyer = modalInputReplyer.val();
      if(replyer  != originalReplyer){
         alert("자신이 작성한 댓글만 삭제가 가능합니다.");
         modal.modal("hide");
         return;
      }
      // 존재하는 댓글의 번호를 이용해서 처리
      replyService.remove(rno, function(result) {
        console.log(result);

        modal.modal("hide");
        showList(pageNum);
      });
    });
    // modalRemoveBtn end

    // Get the current user's username
    var replyer = null;
    <!-- ex06 CSRF -->
      <sec:authorize access="isAuthenticated()">
      replyer = '<sec:authentication property="principal.username"/>';
      </sec:authorize>
   // Get the CSRF header name and token value
      var csrfHeaderName ="${_csrf.headerName}"; // Declare and initialize csrfHeaderName
      var csrfTokenValue="${_csrf.token}"; // Declare and initialize csrfTokenValue
      <!-- ex06 -->
}); // ready
</script>

<!-- ex05: 서버에서 첨부파일 목록을 가져오는 기능 -->
<script>
$(document).ready(function(){
    // 변수 bno 를 초기화하고, 서버에서 첨부파일 목록을 가져옵니다.
   (function(){
     var bno = '<c:out value="${board.bno}"/>';

     /* $.getJSON("/board/getAttachList", {bno: bno}, function(arr){
       console.log(arr);
     });
     */
     // Part6 게시물의 조회와 첨부파일
     $.getJSON("/board/getAttachList", {bno: bno}, function(arr){
        console.log(arr);
        var str = "";
        // arr 배열을 순회하며, 각 첨부파일에 대한 HTML 문자열을 생성합니다.
        $(arr).each(function(i, attach){
          // 첨부파일이 이미지 타입인 경우
          if(attach.fileType){
            var fileCallPath =  encodeURIComponent( attach.uploadPath+ "/s_"+attach.uuid +"_"+attach.fileName);

            str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
            str += "<img src='/display?fileName="+fileCallPath+"'>";
            str += "</div>";
            str +"</li>";
          }else{

            str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
            str += "<span> "+ attach.fileName+"</span><br/>";
            str += "<img src='/resources/img/attach.png'></a>";
            str += "</div>";
            str +"</li>";
          }
        });
        // HTML 문자열을.uploadResult ul 요소에 추가합니다.
        $(".uploadResult ul").html(str);

      });//end getjson

   })();//end function

   //.uploadResult ul 요소에서 클릭 이벤트를 감지합니다.
   $(".uploadResult").on("click","li", function(e){
     console.log("view image");

     var liObj = $(this);
     var path = encodeURIComponent(liObj.data("path")+"/" + liObj.data("uuid")+"_" + liObj.data("filename"));
     // 클릭한 첨부파일이 이미지 타입인 경우 showImage 함수를 호출합니다.
     if(liObj.data("type")){
       showImage(path.replace(new RegExp(/\\/g),"/"));
     }else {
       //클릭한 첨부파일이 이미지 타입이 아닌 경우, 다운로드 링크로 이동합니다.
       self.location ="/download?fileName="+path
     }

   });

   // showImage 함수: 클릭한 첨부파일을 화면에 표시합니다.
   function showImage(fileCallPath){
     alert(fileCallPath);
     //.bigPictureWrapper 요소를 표시하고,.bigPicture 요소에 이미지를 추가합니다.
     $(".bigPictureWrapper").css("display","flex").show();

     $(".bigPicture")
     .html("<img src='/display?fileName="+fileCallPath+"' >")
     .animate({width:'100%', height: '100%'}, 1000);

   }
   //.bigPictureWrapper 요소에서 클릭 이벤트를 감지합니다.
   $(".bigPictureWrapper").on("click", function(e){
     //bigPicture 요소를 숨깁니다.
     $(".bigPicture").animate({width:'0%', height: '0%'}, 1000);
     setTimeout(function(){
       $('.bigPictureWrapper').hide();
     }, 1000);
   });
 });
</script>

<%@include file="../includes/footer.jsp"%>

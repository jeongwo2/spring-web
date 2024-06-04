<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- ex02 get.jsp -->
<%@include file="../includes/header.jsp"%>
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

                <button data-oper='modify' class="btn btn-default">Modify</button>
                <button data-oper='list' class="btn btn-info">List</button>

                <%--   <!--
                <form id='operForm' action="/boad/modify" method="get">
                    <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
                </form>
                amount rename to -->
                --%>

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

<!-- ex03 modal ==============================================================-->
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
<!-- /.modal ==============================================================-->

<!-- ex03 reply ==============================================================-->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<!-- ex03 ./reply ============================================================-->

<!-- ex03 게시판 세부 페이지에서 댓글 기능 -->
<script>
    $(document).ready(function () { // 문서가 완전히 로드된 후에 JavaScript 코드를 실행
        var bnoValue = '<c:out value="${board.bno}"/>'; // EL 표현식으고 게시판 번호를 가져옴
        var replyUL = $(".chat"); // HTML 요소 .chat 을 선택하여 댓글 목록을 표시

        showList(1); // 첫 페이지의 댓글을 초기 표시
        // 서버에서 댓글 목록을 가져오고 HTML 로 구성
        function showList(page){
            console.log("show list " + page);

            replyService.getList({
        bno: bnoValue,
        page: page || 1
      }, function(replyCnt, list) {
                console.log("replyCnt: " + replyCnt);

                if (page == -1) {
                    pageNum = Math.ceil(replyCnt / 10.0);
                    showList(pageNum);
                    return;
                }
                var str = "";
                if (list == null || list.length == 0) {
                    return;
                }
                for (var i = 0, len = list.length || 0; i < len; i++) {
                    str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
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

    // 페이지네이션을 처리
    function showReplyPage(replyCnt) {
        var endNum = Math.ceil(pageNum / 10.0) * 10;
        var startNum = endNum - 9;

        var prev = startNum != 1;
        var next = false;

        if (endNum * 10 >= replyCnt) {
           endNum = Math.ceil(replyCnt / 10.0);
        }
        if (endNum * 10 < replyCnt) {
           next = true;
        }

        var str = "<ul class='pagination pull-right'>";

        if (prev) {
            str += "<li class='page-item'><a class='page-link' href='" + (startNum - 1) + "'>Previous</a></li>";
        }

        for (var i = startNum; i <= endNum; i++) {
            var active = pageNum == i ? "active" : "";
            str += "<li class='page-item " + active + " '><a class='page-link' href='" + i + "'>" + i + "</a></li>";
       }

       if (next) {
           str += "<li class='page-item'><a class='page-link' href='" + (endNum + 1) + "'>Next</a></li>";
       }
       str += "</ul></div>";
            console.log(str);

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
        modal.find("input").val("");
        modalInputReplyDate.closest("div").hide();
        modal.find("button[id !='modalCloseBtn']").hide();

        modalRegisterBtn.show();

        $(".modal").modal("show");
    });

        // 새 댓글을 서버에 저장하기 위한 이벤트 리스너
        modalRegisterBtn.on("click", function(e) {
            var reply = {
            reply: modalInputReply.val(),
            replyer: modalInputReplyer.val(),
            bno: bnoValue
          };
      replyService.add(reply, function(result) {
          alert(result);

          modal.find("input").val("");
          modal.modal("hide");

          showList(-1);
      });

        });
        // modalRegisterBtn end

        // 댓글을 클릭하여 세부 정보를 모달 창에 표시하기 위한 이벤트 리스너
        $(".chat").on("click", "li", function(e) {
            var rno = $(this).data("rno");

            replyService.get(rno, function(reply) {
                modalInputReply.val(reply.reply);
                modalInputReplyer.val(reply.replyer);
                modalInputReplyDate.val(replyService.displayTime(reply.replyDate)).attr("readonly", "readonly");

                modal.data("rno", reply.rno);
                modal.find("button[id !='modalCloseBtn']").hide();

                modalModBtn.show();
                modalRemoveBtn.show();

                $(".modal").modal("show");
            }); // replyService
        });
        //댓글 조회 클릭 이벤트 처리
        // 기존 댓글을 수정하기 위한 이벤트 리스너
        modalModBtn.on("click", function(e) {
            var reply = {
        rno: modal.data("rno"),
        reply: modalInputReply.val()
      };

      replyService.update(reply, function(result) {

        alert(result);

        modal.modal("hide");
        showList(pageNum);
      });
        }); // modalModBtn end

        // 기존 댓글을 삭제하기 위한 이벤트 리스
        modalRemoveBtn.on("click", function(e) {
            var rno = modal.data("rno");

      replyService.remove(rno, function(result) {

        alert(result);

        modal.modal("hide");
        showList(pageNum);
      });
        });
        // modalRemoveBtn end

    }); // ready
</script>

<%@include file="../includes/footer.jsp"%>

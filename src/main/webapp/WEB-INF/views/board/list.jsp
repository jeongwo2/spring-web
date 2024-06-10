<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- JSTL 사용을 위한 태그 라이브러리 추가 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- 중복 되는 코드 include 지시자 활용 -->
<%@include file="../includes/header.jsp" %>
<!-- ex02 list.jsp -->

<!-- Content Header (Page header) -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">게시판</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<!-- Main content -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                Board List Page
                <button id='regBtn' type="button" class="btn btn-xs pull-right">Register[[등록]</button>
            </div>

            <!-- /.panel-heading -->
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <th>#번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                        <th>수정일</th>
                    </tr>
                    </thead>

                    <c:forEach items="${list}" var="board">
                        <tr>
                            <td>
                                <c:out value="${board.bno}"/>
                            </td>
                            <%-- 제목 클릭 시 페이지 이동
                            <td><a href='/board/get?bno=<c:out value="${board.bno}"/>'>
                                <c:out value="${board.title}"/>
                            </a></td>
                            --%>
                            <td>
                                <a class='move' href='<c:out value="${board.bno}"/>'> <c:out value="${board.title}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${board.writer}"/>
                            </td>
                            <td>
                                <fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}"/>
                            </td>
                            <td>
                                <fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <!-- /.table-responsive -->
                <div class='row'>
                    <div class="col-lg-12">
                        <form id='searchForm' action="/board/list" method='get'>
                            <!-- part3 JSP 의 표현 언어(EL)를 사용하여 조건부 논리를 HTML 속성 안에서 처리 -->
                            <select name="type">
                                <option value="" ${empty pageMaker.cri.type? "selected" : ""}>-- </option>
                                <option value="T" ${pageMaker.cri.type eq 'T'? "selected" : ""}>제목 </option>
                                <option value="C" ${pageMaker.cri.type eq 'C'? "selected" : ""}>내용 </option>
                                <option value="W" ${pageMaker.cri.type eq 'W'? "selected" : ""}>작성자 </option>
                                <option value="TC" ${pageMaker.cri.type eq 'TC'? "selected" : ""}>제목 or 내용 </option>
                                <option value="TW" ${pageMaker.cri.type eq 'TW'? "selected" : ""}>제목 or 작성자 </option>
                                <option value="TWC" ${pageMaker.cri.type eq 'TWC'? "selected" : ""}>제목 or 내용 or 작성자 </option>
                            </select>
                            <input type='text'   name='keyword' value='${pageMaker.cri.keyword}'/>
                            <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'/>
                            <input type='hidden' name='amount'  value='${pageMaker.cri.pagePerNum}' />
                            <button class='btn btn-default'>Search</button>
                        </form>
                    </div>
                </div>

                <div class='pull-right'>
                    <ul class="pagination">

                        <c:if test="${pageMaker.prev}">
                            <li class="paginate_button previous">
                                <a href="${pageMaker.startPage -1}">Previous</a>
                            </li>
                        </c:if>

                        <c:forEach var="num"
                                   begin="${pageMaker.startPage}"
                                   end="${pageMaker.endPage}">
                            <li class="paginate_button  ${pageMaker.cri.pageNum == num ? " active":""} ">
                            <a href=" ${num}">${num}</a>
                            </li>
                        </c:forEach>

                        <c:if test="${pageMaker.next}">
                            <li class="paginate_button next">
                                <a href="${pageMaker.endPage +1 }">Next</a>
                            </li>
                        </c:if>

                    </ul>
                </div>
                <!--  end Pagination -->
            </div>
            <!-- action /board/list -->
            <form id='actionForm' action="/board/list" method='get'>
                <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
                <input type='hidden' name='amount' value='${pageMaker.cri.pagePerNum}'>

                <input type='hidden' name='type' value='<c:out value="${ pageMaker.cri.type }"/>'>
                <input type='hidden' name='keyword' value='<c:out value="${ pageMaker.cri.keyword }"/>'>
            </form>
            <!-- action /board/list -->

            <!-- ex02 modal  추가 -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                        </div>
                        <div class="modal-body">처리가 완료되었습니다.</div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Save changes</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <!-- /.modal 추가 end -->

        </div>
        <!-- end panel-body -->
    </div>
    <!-- end panel-body -->
</div>
<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<!-- part3 검색이벤트 처리 -->
<script type="text/javascript">
    // Document ready function to ensure all DOM elements are fully loaded before executing the script
    $(document).ready(function () {
        // Get the result value from the server and store it in the 'result' variable
        var result = '<c:out value="${result}"/>';

        // Call the checkModal function with the 'result' parameter
        checkModal(result);
        // Replace the current state of the history object with an empty object and null URL
        history.replaceState({}, null, null);

        // Function to check and display a modal with a success message
        function checkModal(result) {
            if (result === '' || history.state) {
                return;
            }
            // Construct the success message using the 'result' value
            const message = "게시글 " + parseInt(result) + " 번이 등록되었습니다.";
            // Set the modal body text with the success message
            $(".modal-body").html(message);
            // Show the modal
            $("#myModal").modal("show");
        }

        // Event handler for the 'regBtn' button click event
        $("#regBtn").on("click", function () {
            self.location = "/board/register";
        });
        // Get the 'actionForm' element
        const actionForm = $("#actionForm");

        // Event handler for the pagination links click event
        $(".paginate_button a").on("click", function (e) {
            e.preventDefault();  // Prevent the default link behavior
            console.log('click');

            // Set the 'pageNum' input value of the 'actionForm' with the href attribute of the clicked link
            actionForm.find("input[name='pageNum']")
                .val($(this).attr("href"));
             // Submit the 'actionForm'
            actionForm.submit();
        });

        // 게시물 조회를 위한 이벤트 처리 추가
        $(".move").on("click", function (e) {
            e.preventDefault();

            // Append a hidden input field with the 'bno' name
            actionForm.append("<input type='hidden' name='bno' value='" + $(this).attr("href") + "'>");
            actionForm.attr("action", "/board/get");
            // 상세내역화면
            actionForm.submit();
        });

        // Get the 'searchForm' element
        var searchForm = $("#searchForm");

        // part3 검색이벤트 처리
        $("#searchForm button").on("click", function (e) {
            // If the selected option in the search type dropdown is empty, show an alert and return false
            if (!searchForm.find("option:selected").val()) {
                alert("검색종류를 선택하세요");
                return false;
            }
            if (!searchForm.find(
                "input[name='keyword']").val()) {
                alert("키워드를 입력하세요");
                return false;
            }
            // Set the 'pageNum' input value of the 'searchForm' to '1'
            searchForm.find("input[name='pageNum']").val("1");

            // Prevent the default form submission behavior
            e.preventDefault();
            // Submit the 'searchForm'
            searchForm.submit();
        });
    });
</script>

<%@include file="../includes/footer.jsp" %>
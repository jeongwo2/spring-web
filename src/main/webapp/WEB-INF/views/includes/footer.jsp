<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- ex02 -->
     </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery 주석처리 -->
<!--     <script src="/resources/vendor/jquery/jquery.min.js"></script> -->

    <!-- Bootstrap Core JavaScript -->
    <script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="/resources/vendor/metisMenu/metisMenu.min.js"></script>

    <!-- DataTables JavaScript -->
    <script src="/resources/vendor/datatables/js/jquery.dataTables.min.js"></script>
    <script src="/resources/vendor/datatables-plugins/dataTables.bootstrap.min.js"></script>
    <script src="/resources/vendor/datatables-responsive/dataTables.responsive.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="/resources/dist/js/sb-admin-2.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
      // DataTables 플러그인을 초기화하고, 페이지를 반응형으로 만들기 위해 사이드바 네비게이션 요소를 수정
      $(document).ready(function() {
        $('#dataTables-example').DataTable({
          responsive: true         // 초기화
        });
        $(".sidebar-nav") // 작은 화면에서 사이드바 네비게이션을 접히도록
          .attr("class","sidebar-nav navbar-collapse collapse")
          .attr("aria-expanded",'false')
          .attr("style","height:1px");
      });
    </script>

</body>

</html>

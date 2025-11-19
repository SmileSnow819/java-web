<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>学生信息分页管理</title>
    <style>
      body {
        font-family: Arial, sans-serif;
      }
      table {
        width: 80%;
        border-collapse: collapse;
        margin: 20px auto;
      }
      th,
      td {
        border: 1px solid #ccc;
        padding: 10px;
        text-align: center;
      }
      th {
        background-color: #f2f2f2;
      }
      .action-link {
        margin: 0 5px;
        text-decoration: none;
        color: blue;
      }
      .action-link:hover {
        text-decoration: underline;
      }
      .pagination {
        text-align: center;
        margin: 20px 0;
      }
      .pagination button {
        margin: 0 5px;
        padding: 5px 10px;
        border: 1px solid #ccc;
        background-color: #f8f8f8;
        cursor: pointer;
      }
      .pagination button:hover {
        background-color: #e8e8e8;
      }
      .pagination button:disabled {
        background-color: #f0f0f0;
        color: #999;
        cursor: not-allowed;
      }
      .page-info {
        text-align: center;
        margin: 10px 0;
        color: #666;
      }
      .custom-page {
        display: inline-block;
        margin-left: 20px;
      }
      .custom-page input {
        width: 50px;
        padding: 5px;
        margin: 0 5px;
      }
    </style>
  </head>
  <body>
    <h1>学生信息分页列表</h1>

    <%-- 显示当前登录用户信息和退出按钮 --%>
    <div style="text-align: right; margin-bottom: 20px">
      <c:if test="${not empty sessionScope.currentUser}">
        欢迎，${sessionScope.currentUser.u_name}！
        <a href="UserServlet?action=logout" style="margin-left: 10px">
          <button>退出登录</button>
        </a>
      </c:if>
    </div>

    <%-- 录入按钮和返回全查视图按钮 --%>
    <p>
      <a href="addStu.jsp">
        <button>+ 录入学生信息</button>
      </a>
      
      <a href="StudentServlet?action=getAll" style="margin-left: 10px">
        <button>📋 全查视图</button>
      </a>
    </p>

    <hr />

    <c:choose>
      <%-- 判断分页数据是否为空 --%>
      <c:when test="${empty page.list}">
        <p>目前没有学生信息记录。</p>
      </c:when>
      <c:otherwise>
        <table>
          <thead>
            <tr>
              <th>学号</th>
              <th>姓名</th>
              <th>年龄</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <%-- 遍历分页数据 --%>
            <c:forEach var="stu" items="${page.list}">
              <tr>
                <td><c:out value="${stu.stuNo}" /></td>
                <td><c:out value="${stu.stuName}" /></td>
                <td><c:out value="${stu.stuAge}" /></td>
                <td>
                  <%-- 编辑功能：根据ID查询，转发到 updateStu.jsp --%>
                  <a
                    href="StudentServlet?action=toUpdate&stuNo=${stu.stuNo}"
                    class="action-link"
                    >编辑</a
                  >

                  <%-- 开除功能：调用 JavaScript 确认函数 --%>
                  <a
                    href="javascript:void(0);"
                    onclick="confirmDelete(${stu.stuNo})"
                    class="action-link"
                    >开除</a
                  >
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

        <%-- 分页信息显示 --%>
        <div class="page-info">
          当前第 ${page.pageNow} 页，共 ${page.pageSum} 页，总计 ${page.pageCount} 条记录
        </div>

        <%-- 分页导航 --%>
        <div class="pagination">
          <%-- 首页 --%>
          <button onclick="goToPage(1)" <c:if test="${page.pageNow == 1}">disabled</c:if>>
            首页
          </button>

          <%-- 上一页 --%>
          <button onclick="goToPage(${page.pageNow - 1})" <c:if test="${page.pageNow == 1}">disabled</c:if>>
            上一页
          </button>

          <%-- 下一页 --%>
          <button onclick="goToPage(${page.pageNow + 1})" <c:if test="${page.pageNow == page.pageSum}">disabled</c:if>>
            下一页
          </button>

          <%-- 尾页 --%>
          <button onclick="goToPage(${page.pageSum})" <c:if test="${page.pageNow == page.pageSum}">disabled</c:if>>
            尾页
          </button>

          <%-- 自定义跳转 --%>
          <div class="custom-page">
            跳转到第 
            <input type="number" id="customPage" min="1" max="${page.pageSum}" value="${page.pageNow}" />
            页
            <button onclick="goToCustomPage()">跳转</button>
          </div>
        </div>
      </c:otherwise>
    </c:choose>

    <script>
      /**
       * 跳转到指定页码
       * @param {number} pageNow 目标页码
       */
      function goToPage(pageNow) {
        window.location.href = 'StudentServlet?action=getStuPage&pageNow=' + pageNow;
      }

      /**
       * 跳转到自定义页码
       */
      function goToCustomPage() {
        var customPage = document.getElementById('customPage').value;
        var maxPage = ${page.pageSum};
        
        if (customPage < 1) {
          customPage = 1;
        } else if (customPage > maxPage) {
          customPage = maxPage;
        }
        
        goToPage(customPage);
      }

      /**
       * 弹出确认框，确认后向 StudentServlet 发送删除请求
       * @param {number} stuNo 要删除的学生编号
       */
      function confirmDelete(stuNo) {
        // 弹出一个删除提示框
        if (confirm('是否确认开除学生学号 ' + stuNo + ' ？此操作不可逆！')) {
          // 点击确认，发送请求到服务器删除学生
          // URL: StudentServlet?action=delStu&stuNo=XXX
          window.location.href = 'StudentServlet?action=delStu&stuNo=' + stuNo;
        } else {
          // 点击取消，取消操作
          alert('开除操作已取消。');
        }
      }

      /**
       * 监听回车键跳转
       */
      document.getElementById('customPage').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
          goToCustomPage();
        }
      });
    </script>
  </body>
</html>
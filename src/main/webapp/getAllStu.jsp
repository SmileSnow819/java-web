<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%-- 引入 JSTL 核心标签库，用于循环遍历和条件判断 --%>
<%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>学生信息管理</title>
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
    </style>
  </head>
  <body>
    <h1>学生信息列表</h1>

    <%-- 显示当前登录用户信息和退出按钮 --%>
    <div style="text-align: right; margin-bottom: 20px">
      <c:if test="${not empty sessionScope.currentUser}">
        欢迎，${sessionScope.currentUser.u_name}！
        <a href="UserServlet?action=logout" style="margin-left: 10px">
          <button>退出登录</button>
        </a>
      </c:if>
    </div>

    <%-- 录入按钮：跳转到录入页面 --%>
    <p>
      <a href="addStu.jsp">
        <button>+ 录入学生信息</button>
      </a>

      <%-- 分页视图切换按钮 --%>
      <a
        href="StudentServlet?action=getStuPage&pageNow=1"
        style="margin-left: 10px"
      >
        <button>📄 分页视图</button>
      </a>
    </p>

    <hr />

    <c:choose>
      <%-- 判断 studentList 是否为空 --%>
      <c:when test="${empty studentList}">
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
            <%-- 遍历 StudentServlet 存入 request 域的 studentList --%>
            <c:forEach var="stu" items="${studentList}">
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
      </c:otherwise>
    </c:choose>

    <script>
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
    </script>
  </body>
</html>

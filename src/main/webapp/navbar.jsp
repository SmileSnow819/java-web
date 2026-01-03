<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="bg-white shadow-md">
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div class="flex justify-between items-center h-16">
      <h1 class="text-2xl font-bold text-gray-800">学生管理系统</h1>
      <c:if test="${not empty sessionScope.currentUser}">
        <div class="flex items-center space-x-4">
          <span class="text-gray-700"
            >欢迎，<span class="font-semibold text-blue-600"
              >${sessionScope.currentUser.u_name}</span
            >！</span
          >
          <a
            href="UserServlet?action=logout"
            class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
          >
            退出登录
          </a>
        </div>
      </c:if>
    </div>
  </div>
</nav>
<div class="bg-blue-50 text-blue-700 px-4 py-2 text-right text-sm">
  当前在线人数：<span id="onlineCountTop"
    ><c:out
      value="${applicationScope.onlineCount != null ? applicationScope.onlineCount : 0}"
  /></span>
</div>


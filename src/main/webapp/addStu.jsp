<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>录入学生信息</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-50 min-h-screen">
    <!-- 顶部导航栏 -->
    <nav class="bg-white shadow-md">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <h1 class="text-2xl font-bold text-gray-800">录入新学生</h1>
          <c:if test="${not empty sessionScope.currentUser}">
            <div class="flex items-center space-x-4">
              <span class="text-gray-700">欢迎，<span class="font-semibold text-blue-600">${sessionScope.currentUser.u_name}</span>！</span>
              <a href="UserServlet?action=logout" class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors">
                退出登录
              </a>
            </div>
          </c:if>
        </div>
      </div>
    </nav>

    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white rounded-lg shadow-xl p-8">
        <c:if test="${not empty msg}">
          <div class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg">
            ${msg}
          </div>
        </c:if>

        <form action="StudentServlet" method="post" class="space-y-6">
          <input type="hidden" name="action" value="addStu" />
          <input type="hidden" name="returnView" value="${param.returnView != null ? param.returnView : 'getAll'}" />
          <c:if test="${param.pageNow != null}">
            <input type="hidden" name="pageNow" value="${param.pageNow}" />
          </c:if>
          <c:if test="${param.searchStuNo != null}">
            <input type="hidden" name="searchStuNo" value="${param.searchStuNo}" />
          </c:if>
          <c:if test="${param.searchStuName != null}">
            <input type="hidden" name="searchStuName" value="${param.searchStuName}" />
          </c:if>
          <c:if test="${param.searchStuAge != null}">
            <input type="hidden" name="searchStuAge" value="${param.searchStuAge}" />
          </c:if>

          <div>
            <label for="stuName" class="block text-sm font-medium text-gray-700 mb-2">
              姓名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="stuName"
              name="stuName"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入学生姓名"
            />
          </div>

          <div>
            <label for="stuAge" class="block text-sm font-medium text-gray-700 mb-2">
              年龄 <span class="text-red-500">*</span>
            </label>
            <input
              type="number"
              id="stuAge"
              name="stuAge"
              required
              min="15"
              max="60"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入年龄（15-60岁）"
            />
            <p class="mt-2 text-sm text-gray-500">年龄范围：15-60岁</p>
          </div>

          <div class="flex gap-4 pt-4">
            <button
              type="submit"
              class="flex-1 bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-3 rounded-lg font-semibold hover:from-blue-600 hover:to-indigo-700 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
            >
              <svg class="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              确认录入
            </button>
            <c:choose>
              <c:when test="${param.returnView == 'getStuPage'}">
                <c:set var="pageNow" value="${param.pageNow != null ? param.pageNow : sessionScope.currentPageNow != null ? sessionScope.currentPageNow : '1'}" />
                <c:url var="returnUrl" value="StudentServlet">
                  <c:param name="action" value="getStuPage" />
                  <c:param name="pageNow" value="${pageNow}" />
                  <c:if test="${not empty param.searchStuNo}">
                    <c:param name="stuNo" value="${param.searchStuNo}" />
                  </c:if>
                  <c:if test="${not empty param.searchStuName}">
                    <c:param name="stuName" value="${param.searchStuName}" />
                  </c:if>
                  <c:if test="${not empty param.searchStuAge}">
                    <c:param name="stuAge" value="${param.searchStuAge}" />
                  </c:if>
                </c:url>
              </c:when>
              <c:otherwise>
                <c:url var="returnUrl" value="StudentServlet">
                  <c:param name="action" value="getAll" />
                  <c:if test="${not empty param.searchStuNo}">
                    <c:param name="stuNo" value="${param.searchStuNo}" />
                  </c:if>
                  <c:if test="${not empty param.searchStuName}">
                    <c:param name="stuName" value="${param.searchStuName}" />
                  </c:if>
                  <c:if test="${not empty param.searchStuAge}">
                    <c:param name="stuAge" value="${param.searchStuAge}" />
                  </c:if>
                </c:url>
              </c:otherwise>
            </c:choose>
            <a
              href="${returnUrl}"
              class="flex-1 bg-gray-500 text-white py-3 rounded-lg font-semibold hover:bg-gray-600 transition-all duration-200 shadow-lg hover:shadow-xl text-center inline-flex items-center justify-center"
            >
              <svg class="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
              </svg>
              返回列表
            </a>
          </div>
        </form>
      </div>
    </div>
  </body>
</html>

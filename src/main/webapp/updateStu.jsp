<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>编辑学生信息</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-50 min-h-screen">
    <!-- 顶部导航栏 -->
    <nav class="bg-white shadow-md">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <h1 class="text-2xl font-bold text-gray-800">编辑学生信息</h1>
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
          <input type="hidden" name="action" value="updateStu" />
          <input type="hidden" name="pageNow" value="${pageNow}" />
          <input type="hidden" name="searchStuNo" value="${searchStuNo}" />
          <input type="hidden" name="searchStuName" value="${searchStuName}" />
          <input type="hidden" name="searchStuAge" value="${searchStuAge}" />
          <input type="hidden" name="returnView" value="${returnView}" />

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              学号 <span class="text-gray-500 text-xs">(不可修改)</span>
            </label>
            <input
              type="text"
              name="stuNo"
              value="${student.stuNo}"
              readonly
              class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-100 text-gray-600 cursor-not-allowed"
            />
          </div>

          <div>
            <label for="stuName" class="block text-sm font-medium text-gray-700 mb-2">
              姓名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="stuName"
              name="stuName"
              value="${student.stuName}"
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
              value="${student.stuAge}"
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
              确认修改
            </button>
            <button
              type="button"
              onclick="goBack()"
              class="flex-1 bg-gray-500 text-white py-3 rounded-lg font-semibold hover:bg-gray-600 transition-all duration-200 shadow-lg hover:shadow-xl"
            >
              <svg class="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
              </svg>
              取消并返回
            </button>
          </div>
        </form>
      </div>
    </div>

    <script>
      function goBack() {
        var returnView = '${returnView}';
        var url = 'StudentServlet?action=' + returnView;
        
        if (returnView === 'getStuPage') {
          var pageNow = '${pageNow}';
          if (pageNow && pageNow.trim() !== '') {
            url += '&pageNow=' + pageNow;
          } else {
            url += '&pageNow=1';
          }
        }
        
        var searchStuNo = '${searchStuNo}';
        var searchStuName = '${searchStuName}';
        var searchStuAge = '${searchStuAge}';
        
        if (searchStuNo && searchStuNo.trim() !== '') {
          url += '&stuNo=' + encodeURIComponent(searchStuNo);
        }
        if (searchStuName && searchStuName.trim() !== '') {
          url += '&stuName=' + encodeURIComponent(searchStuName);
        }
        if (searchStuAge && searchStuAge.trim() !== '') {
          url += '&stuAge=' + encodeURIComponent(searchStuAge);
        }
        
        window.location.href = url;
      }
    </script>
  </body>
</html>

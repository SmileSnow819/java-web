<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="navbar.jsp"/>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>学生信息分页管理</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-50 min-h-screen">

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- 操作按钮区域 -->
      <div class="mb-6 flex flex-wrap gap-4">
        <c:url var="addStuUrl" value="addStu.jsp">
          <c:param name="returnView" value="getStuPage" />
          <c:param name="pageNow" value="${page.pageNow}" />
          <c:if test="${not empty searchStuNo}">
            <c:param name="searchStuNo" value="${searchStuNo}" />
          </c:if>
          <c:if test="${not empty searchStuName}">
            <c:param name="searchStuName" value="${searchStuName}" />
          </c:if>
          <c:if test="${not empty searchStuAge}">
            <c:param name="searchStuAge" value="${searchStuAge}" />
          </c:if>
        </c:url>
        <a href="${addStuUrl}" class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-all shadow-md hover:shadow-lg transform hover:-translate-y-0.5">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
          </svg>
          录入学生信息
        </a>
        <a href="StudentServlet?action=getAll" class="inline-flex items-center px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all shadow-md hover:shadow-lg transform hover:-translate-y-0.5">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
          </svg>
          全查视图
        </a>
      </div>

      <!-- 搜索条件表单 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <form action="StudentServlet" method="get" class="flex flex-wrap gap-4 items-end">
          <input type="hidden" name="action" value="getStuPage" />
          <input type="hidden" name="pageNow" value="1" />
          
          <div class="flex-1 min-w-[200px]">
            <label for="searchStuNo" class="block text-sm font-medium text-gray-700 mb-2">学号</label>
            <input
              type="text"
              id="searchStuNo"
              name="stuNo"
              value="${searchStuNo}"
              placeholder="请输入学号"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>
          
          <div class="flex-1 min-w-[200px]">
            <label for="searchStuName" class="block text-sm font-medium text-gray-700 mb-2">姓名</label>
            <input
              type="text"
              id="searchStuName"
              name="stuName"
              value="${searchStuName}"
              placeholder="请输入姓名"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
            />
          </div>
          
          <div class="flex-1 min-w-[200px]">
            <label class="block text-sm font-medium text-gray-700 mb-2">年龄范围</label>
            <div class="flex items-center space-x-2">
              <input
                type="number"
                name="startAge"
                value="${searchStartAge}"
                placeholder="起始"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
              />
              <span class="text-gray-500">-</span>
              <input
                type="number"
                name="endAge"
                value="${searchEndAge}"
                placeholder="结束"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
              />
            </div>
          </div>
          
          <div class="flex gap-2">
            <button type="submit" class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors shadow-md">
              <svg class="w-5 h-5 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              搜索
            </button>
            <a href="StudentServlet?action=getStuPage&pageNow=1" class="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors shadow-md">
              重置
            </a>
          </div>
        </form>
      </div>

      <c:choose>
        <c:when test="${empty page.list}">
          <div class="bg-white rounded-lg shadow-md p-12 text-center">
            <svg class="w-16 h-16 mx-auto text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path>
            </svg>
            <p class="text-gray-500 text-lg">目前没有学生信息记录</p>
          </div>
        </c:when>
        <c:otherwise>
          <!-- 数据表格 -->
          <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gradient-to-r from-blue-500 to-indigo-600">
                  <tr>
                    <th class="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">学号</th>
                    <th class="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">姓名</th>
                    <th class="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">年龄</th>
                    <th class="px-6 py-4 text-left text-xs font-semibold text-white uppercase tracking-wider">操作</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <c:forEach var="stu" items="${page.list}">
                    <tr class="hover:bg-gray-50 transition-colors">
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${stu.stuNo}</td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">${stu.stuName}</td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">${stu.stuAge}</td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm">
                        <button
                          onclick="goToUpdate(${stu.stuNo})"
                          class="mr-3 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors text-xs"
                        >
                          编辑
                        </button>
                        <button
                          onclick="confirmDelete(${stu.stuNo})"
                          class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors text-xs"
                        >
                          开除
                        </button>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>

          <!-- 分页信息 -->
          <div class="mt-6 text-center text-gray-600">
            <p class="text-sm">当前第 <span class="font-bold text-blue-600">${page.pageNow}</span> 页，共 <span class="font-bold text-blue-600">${page.pageSum}</span> 页，总计 <span class="font-bold text-blue-600">${page.pageCount}</span> 条记录</p>
          </div>

          <!-- 分页导航 -->
          <div class="mt-6 flex flex-wrap justify-center items-center gap-2">
            <button
              onclick="goToPage(1)"
              <c:if test="${page.pageNow == 1}">disabled</c:if>
              class="px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              首页
            </button>
            <button
              onclick="goToPage(${page.pageNow - 1})"
              <c:if test="${page.pageNow == 1}">disabled</c:if>
              class="px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              上一页
            </button>
            <button
              onclick="goToPage(${page.pageNow + 1})"
              <c:if test="${page.pageNow == page.pageSum}">disabled</c:if>
              class="px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              下一页
            </button>
            <button
              onclick="goToPage(${page.pageSum})"
              <c:if test="${page.pageNow == page.pageSum}">disabled</c:if>
              class="px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              尾页
            </button>
            <div class="flex items-center gap-2 ml-4">
              <span class="text-sm text-gray-700">跳转到第</span>
              <input
                type="number"
                id="customPage"
                min="1"
                max="${page.pageSum}"
                value="${page.pageNow}"
                class="w-16 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none text-center"
              />
              <span class="text-sm text-gray-700">页</span>
              <button
                onclick="goToCustomPage()"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors shadow-sm"
              >
                跳转
              </button>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <script>
      function goToPage(pageNow) {
        var url = 'StudentServlet?action=getStuPage&pageNow=' + pageNow;
        var stuNo = document.getElementById('searchStuNo') ? document.getElementById('searchStuNo').value : '';
        var stuName = document.getElementById('searchStuName') ? document.getElementById('searchStuName').value : '';
        var startAge = document.querySelector('input[name="startAge"]').value;
        var endAge = document.querySelector('input[name="endAge"]').value;
        
        if (stuNo && stuNo.trim() !== '') {
          url += '&stuNo=' + encodeURIComponent(stuNo);
        }
        if (stuName && stuName.trim() !== '') {
          url += '&stuName=' + encodeURIComponent(stuName);
        }
        if (startAge && startAge.trim() !== '') {
          url += '&startAge=' + encodeURIComponent(startAge);
        }
        if (endAge && endAge.trim() !== '') {
          url += '&endAge=' + encodeURIComponent(endAge);
        }
        
        window.location.href = url;
      }

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

      function goToUpdate(stuNo) {
        var url = 'StudentServlet?action=toUpdate&stuNo=' + stuNo + '&pageNow=${page.pageNow}';
        var searchStuNo = document.getElementById('searchStuNo') ? document.getElementById('searchStuNo').value : '';
        var searchStuName = document.getElementById('searchStuName') ? document.getElementById('searchStuName').value : '';
        var searchStartAge = document.querySelector('input[name="startAge"]').value;
        var searchEndAge = document.querySelector('input[name="endAge"]').value;
        
        if (searchStuNo && searchStuNo.trim() !== '') {
          url += '&searchStuNo=' + encodeURIComponent(searchStuNo);
        }
        if (searchStuName && searchStuName.trim() !== '') {
          url += '&searchStuName=' + encodeURIComponent(searchStuName);
        }
        if (searchStartAge && searchStartAge.trim() !== '') {
          url += '&startAge=' + encodeURIComponent(searchStartAge);
        }
        if (searchEndAge && searchEndAge.trim() !== '') {
          url += '&endAge=' + encodeURIComponent(searchEndAge);
        }
        
        window.location.href = url;
      }

      function confirmDelete(stuNo) {
        if (confirm('是否确认开除学生学号 ' + stuNo + ' ？此操作不可逆！')) {
          var url = 'StudentServlet?action=delStu&stuNo=' + stuNo + '&pageNow=${page.pageNow}';
          var searchStuNo = document.getElementById('searchStuNo') ? document.getElementById('searchStuNo').value : '';
          var searchStuName = document.getElementById('searchStuName') ? document.getElementById('searchStuName').value : '';
          var searchStartAge = document.querySelector('input[name="startAge"]').value;
          var searchEndAge = document.querySelector('input[name="endAge"]').value;
          
          if (searchStuNo && searchStuNo.trim() !== '') {
            url += '&searchStuNo=' + encodeURIComponent(searchStuNo);
          }
          if (searchStuName && searchStuName.trim() !== '') {
            url += '&searchStuName=' + encodeURIComponent(searchStuName);
          }
          if (searchStartAge && searchStartAge.trim() !== '') {
            url += '&startAge=' + encodeURIComponent(searchStartAge);
          }
          if (searchEndAge && searchEndAge.trim() !== '') {
            url += '&endAge=' + encodeURIComponent(searchEndAge);
          }
          
          window.location.href = url;
        } else {
          alert('开除操作已取消。');
        }
      }

      document.getElementById('customPage')?.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
          goToCustomPage();
        }
      });
    </script>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>学生详情 - 学生管理系统</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-50 min-h-screen">
    <!-- 顶部导航栏 -->
    <jsp:include page="navbar.jsp" />

    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white rounded-lg shadow-xl overflow-hidden">
        <!-- 页面标题 -->
        <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-4">
          <h1 class="text-2xl font-bold text-white">学生详细信息</h1>
        </div>

        <!-- 学生信息卡片 -->
        <div class="p-8">
          <c:if test="${not empty student}">
            <div class="flex flex-col md:flex-row gap-8">
              <!-- 左侧：头像区域 -->
              <div class="flex-shrink-0">
                <div class="w-48 h-48 rounded-lg overflow-hidden border-4 border-blue-200 shadow-lg">
                  <c:choose>
                    <c:when test="${not empty student.stuImg}">
                      <img
                        src="${pageContext.request.contextPath}/${student.stuImg}"
                        alt="${student.stuName}"
                        class="w-full h-full object-cover"
                        onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
                      />
                    </c:when>
                    <c:otherwise>
                      <div class="w-full h-full bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center">
                        <svg class="w-24 h-24 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                        </svg>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>

              <!-- 右侧：详细信息 -->
              <div class="flex-1">
                <div class="space-y-6">
                  <!-- 学号 -->
                  <div class="flex items-center border-b border-gray-200 pb-4">
                    <div class="w-32 flex-shrink-0">
                      <span class="text-sm font-semibold text-gray-600">学号：</span>
                    </div>
                    <div class="flex-1">
                      <span class="text-lg font-bold text-gray-900">${student.stuNo}</span>
                    </div>
                  </div>

                  <!-- 姓名 -->
                  <div class="flex items-center border-b border-gray-200 pb-4">
                    <div class="w-32 flex-shrink-0">
                      <span class="text-sm font-semibold text-gray-600">姓名：</span>
                    </div>
                    <div class="flex-1">
                      <span class="text-lg font-medium text-gray-900">${student.stuName}</span>
                    </div>
                  </div>

                  <!-- 年龄 -->
                  <div class="flex items-center border-b border-gray-200 pb-4">
                    <div class="w-32 flex-shrink-0">
                      <span class="text-sm font-semibold text-gray-600">年龄：</span>
                    </div>
                    <div class="flex-1">
                      <span class="text-lg text-gray-900">${student.stuAge} 岁</span>
                    </div>
                  </div>

                  <!-- 头像路径（仅用于调试，可选） -->
                  <c:if test="${not empty student.stuImg}">
                    <div class="flex items-start border-b border-gray-200 pb-4">
                      <div class="w-32 flex-shrink-0">
                        <span class="text-sm font-semibold text-gray-600">头像：</span>
                      </div>
                      <div class="flex-1">
                        <span class="text-sm text-gray-700">${student.stuImg}</span>
                      </div>
                    </div>
                  </c:if>
                </div>
              </div>
            </div>
          </c:if>

          <c:if test="${empty student}">
            <div class="text-center py-12">
              <svg class="w-16 h-16 mx-auto text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <p class="text-gray-500 text-lg">未找到学生信息</p>
            </div>
          </c:if>
        </div>

        <!-- 底部操作按钮 -->
        <div class="bg-gray-50 px-6 py-4 border-t border-gray-200">
          <div class="flex justify-between items-center">
            <div>
              <c:choose>
                <c:when test="${returnView == 'getStuPage'}">
                  <c:set var="pageNow" value="${pageNow != null ? pageNow : '1'}" />
                  <c:url var="returnUrl" value="StudentServlet">
                    <c:param name="action" value="getStuPage" />
                    <c:param name="pageNow" value="${pageNow}" />
                    <c:if test="${not empty searchStuNo}">
                      <c:param name="stuNo" value="${searchStuNo}" />
                    </c:if>
                    <c:if test="${not empty searchStuName}">
                      <c:param name="stuName" value="${searchStuName}" />
                    </c:if>
                    <c:if test="${not empty searchStartAge}">
                      <c:param name="startAge" value="${searchStartAge}" />
                    </c:if>
                    <c:if test="${not empty searchEndAge}">
                      <c:param name="endAge" value="${searchEndAge}" />
                    </c:if>
                  </c:url>
                </c:when>
                <c:otherwise>
                  <c:url var="returnUrl" value="StudentServlet">
                    <c:param name="action" value="getAll" />
                    <c:if test="${not empty searchStuNo}">
                      <c:param name="stuNo" value="${searchStuNo}" />
                    </c:if>
                    <c:if test="${not empty searchStuName}">
                      <c:param name="stuName" value="${searchStuName}" />
                    </c:if>
                    <c:if test="${not empty searchStartAge}">
                      <c:param name="startAge" value="${searchStartAge}" />
                    </c:if>
                    <c:if test="${not empty searchEndAge}">
                      <c:param name="endAge" value="${searchEndAge}" />
                    </c:if>
                  </c:url>
                </c:otherwise>
              </c:choose>
              
              <a
                href="${returnUrl}"
                class="inline-flex items-center px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-all duration-200 shadow-md hover:shadow-lg"
              >
                <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                </svg>
                返回列表
              </a>
            </div>

            <c:if test="${not empty student}">
              <div class="flex gap-3">
                <c:choose>
                  <c:when test="${returnView == 'getStuPage'}">
                    <c:url var="editUrl" value="StudentServlet">
                      <c:param name="action" value="toUpdate" />
                      <c:param name="stuNo" value="${student.stuNo}" />
                      <c:param name="pageNow" value="${pageNow}" />
                      <c:if test="${not empty searchStuNo}">
                        <c:param name="searchStuNo" value="${searchStuNo}" />
                      </c:if>
                      <c:if test="${not empty searchStuName}">
                        <c:param name="searchStuName" value="${searchStuName}" />
                      </c:if>
                      <c:if test="${not empty searchStartAge}">
                        <c:param name="startAge" value="${searchStartAge}" />
                      </c:if>
                      <c:if test="${not empty searchEndAge}">
                        <c:param name="endAge" value="${searchEndAge}" />
                      </c:if>
                    </c:url>
                  </c:when>
                  <c:otherwise>
                    <c:url var="editUrl" value="StudentServlet">
                      <c:param name="action" value="toUpdate" />
                      <c:param name="stuNo" value="${student.stuNo}" />
                      <c:if test="${not empty searchStuNo}">
                        <c:param name="searchStuNo" value="${searchStuNo}" />
                      </c:if>
                      <c:if test="${not empty searchStuName}">
                        <c:param name="searchStuName" value="${searchStuName}" />
                      </c:if>
                      <c:if test="${not empty searchStartAge}">
                        <c:param name="startAge" value="${searchStartAge}" />
                      </c:if>
                      <c:if test="${not empty searchEndAge}">
                        <c:param name="endAge" value="${searchEndAge}" />
                      </c:if>
                    </c:url>
                  </c:otherwise>
                </c:choose>
                
                <a
                  href="${editUrl}"
                  class="inline-flex items-center px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all duration-200 shadow-md hover:shadow-lg"
                >
                  <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                  </svg>
                  编辑信息
                </a>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>


<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>录入学生信息</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  </head>
  <body class="bg-gray-50 min-h-screen">
    <!-- 顶部导航栏 -->
    <jsp:include page="navbar.jsp" />

    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white rounded-lg shadow-xl p-8">
        <c:if test="${not empty msg}">
          <div
            class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg"
          >
            ${msg}
          </div>
        </c:if>

        <!-- 显示姓名敏感词过滤提示 -->
        <c:if test="${not empty originalName && not empty safeName}">
          <div
            class="mb-6 p-4 bg-orange-50 border border-orange-200 text-orange-700 rounded-lg"
          >
            <div class="flex items-center">
              <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fill-rule="evenodd"
                  d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                  clip-rule="evenodd"
                ></path>
              </svg>
              <strong>敏感词过滤提示：</strong>
            </div>
            <p class="mt-2">检测到姓名中包含敏感词，系统已自动过滤：</p>
            <p class="mt-1">
              原始输入：<span
                class="font-mono bg-orange-100 px-2 py-1 rounded text-red-600"
                >${originalName}</span
              >
            </p>
            <p class="mt-1">
              过滤结果：<span
                class="font-mono bg-green-100 px-2 py-1 rounded text-green-600"
                >${safeName}</span
              >
            </p>
            <p class="mt-2 text-sm text-gray-600">
              💡 提示：系统会根据敏感词的长度替换为相应数量的 * 字符
            </p>
          </div>
        </c:if>

        <form action="StudentServlet" method="post" enctype="multipart/form-data" class="space-y-6">
          <input type="hidden" name="action" value="addStu" />
          <input
            type="hidden"
            name="returnView"
            value="${param.returnView != null ? param.returnView : 'getAll'}"
          />
          <c:if test="${param.pageNow != null}">
            <input type="hidden" name="pageNow" value="${param.pageNow}" />
          </c:if>
          <c:if test="${param.searchStuNo != null}">
            <input
              type="hidden"
              name="searchStuNo"
              value="${param.searchStuNo}"
            />
          </c:if>
          <c:if test="${param.searchStuName != null}">
            <input
              type="hidden"
              name="searchStuName"
              value="${param.searchStuName}"
            />
          </c:if>
          <c:if test="${param.searchStuAge != null}">
            <input
              type="hidden"
              name="searchStuAge"
              value="${param.searchStuAge}"
            />
          </c:if>

          <!-- 学生头像上传 -->
          <div>
            <label
              for="stuImg"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              学生头像 <span class="text-red-500">*</span>
            </label>
            <div class="space-y-4">
              <input
                type="file"
                id="stuImg"
                name="stuImg"
                accept="image/*"
                onchange="handleFileChange(event)"
                required
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              />
              <p class="text-sm text-gray-500">支持格式：JPG、PNG、GIF等图片格式</p>
              
              <!-- 图片预览区域 -->
              <div id="imagePreviewContainer" class="mt-4" style="display: none;">
                <p class="text-sm font-medium text-gray-700 mb-2">预览：</p>
                <div class="relative inline-block">
                  <img
                    id="imagePreviewImg"
                    src=""
                    alt="头像预览"
                    class="w-32 h-32 object-cover rounded-lg border-2 border-gray-300 shadow-md"
                  />
                  <button
                    type="button"
                    id="clearPreviewBtn"
                    onclick="clearPreview()"
                    class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-600 transition-colors"
                    title="清除预览"
                  >
                    ×
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div>
            <label
              for="stuName"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              姓名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="stuName"
              name="stuName"
              value="${not empty safeName ? safeName : ''}"
              required
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入学生姓名"
            />
          </div>

          <div>
            <label
              for="stuAge"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
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
              <svg
                class="w-5 h-5 inline mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M5 13l4 4L19 7"
                ></path>
              </svg>
              确认录入
            </button>
            <c:choose>
              <c:when test="${param.returnView == 'getStuPage'}">
                <c:set
                  var="pageNow"
                  value="${param.pageNow != null ? param.pageNow : sessionScope.currentPageNow != null ? sessionScope.currentPageNow : '1'}"
                />
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
              <svg
                class="w-5 h-5 inline mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M10 19l-7-7m0 0l7-7m-7 7h18"
                ></path>
              </svg>
              返回列表
            </a>
          </div>
        </form>
      </div>
    </div>

    <div id="app"></div>
    <script>
      const { createApp } = Vue;
      
      const app = createApp({
        data() {
          return {
            imagePreview: null
          };
        },
        methods: {
          handleFileChange(event) {
            const file = event.target.files[0];
            if (file) {
              // 验证文件类型
              if (!file.type.startsWith('image/')) {
                alert('请选择图片文件！');
                event.target.value = '';
                this.imagePreview = null;
                return;
              }
              
              // 验证文件大小（限制为5MB）
              if (file.size > 5 * 1024 * 1024) {
                alert('图片大小不能超过5MB！');
                event.target.value = '';
                this.imagePreview = null;
                return;
              }
              
              // 创建预览
              const reader = new FileReader();
              reader.onload = (e) => {
                this.imagePreview = e.target.result;
              };
              reader.readAsDataURL(file);
            } else {
              this.imagePreview = null;
            }
          },
          clearPreview() {
            this.imagePreview = null;
            document.getElementById('stuImg').value = '';
          }
        }
      });
      
      // 挂载Vue到app div，通过全局方法处理文件选择事件
      app.mount('#app');
      
      // 将handleFileChange方法暴露到全局，供文件输入框调用
      window.vueApp = app;
    </script>
    
    <script>
      // 直接在文件输入框上使用内联事件处理器
      function handleFileChange(event) {
        const file = event.target.files[0];
        const previewContainer = document.getElementById('imagePreviewContainer');
        const previewImg = document.getElementById('imagePreviewImg');
        const clearBtn = document.getElementById('clearPreviewBtn');
        
        if (file) {
          // 验证文件类型
          if (!file.type.startsWith('image/')) {
            alert('请选择图片文件！');
            event.target.value = '';
            if (previewContainer) previewContainer.style.display = 'none';
            return;
          }
          
          // 验证文件大小（限制为5MB）
          if (file.size > 5 * 1024 * 1024) {
            alert('图片大小不能超过5MB！');
            event.target.value = '';
            if (previewContainer) previewContainer.style.display = 'none';
            return;
          }
          
          // 创建预览
          const reader = new FileReader();
          reader.onload = function(e) {
            if (previewImg) {
              previewImg.src = e.target.result;
              if (previewContainer) previewContainer.style.display = 'block';
            }
          };
          reader.readAsDataURL(file);
        } else {
          if (previewContainer) previewContainer.style.display = 'none';
        }
      }
      
      function clearPreview() {
        document.getElementById('stuImg').value = '';
        const previewContainer = document.getElementById('imagePreviewContainer');
        if (previewContainer) previewContainer.style.display = 'none';
      }
    </script>
  </body>
</html>

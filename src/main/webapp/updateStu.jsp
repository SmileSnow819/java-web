<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
          <input type="hidden" name="action" value="updateStu" />
          <input type="hidden" name="pageNow" value="${pageNow}" />
          <input type="hidden" name="searchStuNo" value="${searchStuNo}" />
          <input type="hidden" name="searchStuName" value="${searchStuName}" />
          <input type="hidden" name="startAge" value="${searchStartAge}" />
          <input type="hidden" name="endAge" value="${searchEndAge}" />
          <input type="hidden" name="returnView" value="${returnView}" />
          <input type="hidden" id="originalStuImg" value="${not empty student.stuImg ? student.stuImg : 'images/default-avatar.png'}" />

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

          <!-- 学生头像上传 -->
          <div>
            <label
              for="stuImg"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              学生头像 <span class="text-gray-500 text-xs">(可选，不选择则保留原头像)</span>
            </label>
            <div class="space-y-4">
              <!-- 原始头像显示 -->
              <div id="originalAvatarContainer" class="mt-2">
                <p class="text-sm font-medium text-gray-700 mb-2">当前头像：</p>
                <div class="relative inline-block">
                  <img
                    id="originalAvatarImg"
                    src="${pageContext.request.contextPath}/${not empty student.stuImg ? student.stuImg : 'images/default-avatar.png'}"
                    alt="当前头像"
                    class="w-32 h-32 object-cover rounded-lg border-2 border-gray-300 shadow-md"
                    onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
                  />
                </div>
              </div>

              <!-- 文件上传输入 -->
              <input
                type="file"
                id="stuImg"
                name="stuImg"
                accept="image/*"
                onchange="handleFileChange(event)"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              />
              <p class="text-sm text-gray-500">支持格式：JPG、PNG、GIF等图片格式</p>
              
              <!-- 新头像预览区域 -->
              <div id="newAvatarPreviewContainer" class="mt-4" style="display: none;">
                <p class="text-sm font-medium text-gray-700 mb-2">新头像预览：</p>
                <div class="relative inline-block">
                  <img
                    id="newAvatarPreviewImg"
                    src=""
                    alt="新头像预览"
                    class="w-32 h-32 object-cover rounded-lg border-2 border-blue-400 shadow-md"
                  />
                  <button
                    type="button"
                    id="cancelNewAvatarBtn"
                    onclick="cancelNewAvatar()"
                    class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-600 transition-colors"
                    title="取消更换，恢复原头像"
                  >
                    ×
                  </button>
                </div>
                <p class="mt-2 text-sm text-blue-600">💡 点击 × 可取消更换，恢复原头像</p>
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
              value="${not empty safeName ? safeName : student.stuName}"
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
              确认修改
            </button>
            <button
              type="button"
              onclick="goBack()"
              class="flex-1 bg-gray-500 text-white py-3 rounded-lg font-semibold hover:bg-gray-600 transition-all duration-200 shadow-lg hover:shadow-xl"
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
              取消并返回
            </button>
          </div>
        </form>
      </div>
    </div>

    <script>
      // 保存原始头像路径
      var originalAvatarSrc = '${pageContext.request.contextPath}/${not empty student.stuImg ? student.stuImg : 'images/default-avatar.png'}';
      
      // 处理文件选择
      function handleFileChange(event) {
        const file = event.target.files[0];
        const previewContainer = document.getElementById('newAvatarPreviewContainer');
        const previewImg = document.getElementById('newAvatarPreviewImg');
        const fileInput = document.getElementById('stuImg');
        
        if (file) {
          // 验证文件类型
          if (!file.type.startsWith('image/')) {
            alert('请选择图片文件！');
            fileInput.value = '';
            if (previewContainer) previewContainer.style.display = 'none';
            return;
          }
          
          // 验证文件大小（限制为5MB）
          if (file.size > 5 * 1024 * 1024) {
            alert('图片大小不能超过5MB！');
            fileInput.value = '';
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
      
      // 取消新头像，恢复原头像
      function cancelNewAvatar() {
        const fileInput = document.getElementById('stuImg');
        const previewContainer = document.getElementById('newAvatarPreviewContainer');
        const previewImg = document.getElementById('newAvatarPreviewImg');
        
        // 清空文件输入
        if (fileInput) {
          fileInput.value = '';
        }
        
        // 隐藏预览区域
        if (previewContainer) {
          previewContainer.style.display = 'none';
        }
        
        // 清空预览图片
        if (previewImg) {
          previewImg.src = '';
        }
      }

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
        var searchStartAge = '${searchStartAge}';
        var searchEndAge = '${searchEndAge}';

        if (searchStuNo && searchStuNo.trim() !== '') {
          url += '&stuNo=' + encodeURIComponent(searchStuNo);
        }
        if (searchStuName && searchStuName.trim() !== '') {
          url += '&stuName=' + encodeURIComponent(searchStuName);
        }
        if (searchStartAge && searchStartAge.trim() !== '') {
          url += '&startAge=' + encodeURIComponent(searchStartAge);
        }
        if (searchEndAge && searchEndAge.trim() !== '') {
          url += '&endAge=' + encodeURIComponent(searchEndAge);
        }

        window.location.href = url;
      }
    </script>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>用户登录 - 学生管理系统</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/tips.js"></script>
  </head>
  <body
    class="bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 min-h-screen flex items-center justify-center p-4"
  >
    <div class="w-full max-w-md">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-800 mb-2">欢迎回来</h1>
          <p class="text-gray-600">请登录您的账户</p>
        </div>

        <c:if test="${not empty msg}">
          <div
            class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg"
          >
            ${msg}
          </div>
        </c:if>

        <c:if test="${param.logout == 'success'}">
          <div
            class="mb-6 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg"
          >
            您已成功退出登录。
          </div>
        </c:if>

        <form action="UserServlet" method="post" class="space-y-6">
          <input type="hidden" name="action" value="login" />

          <div>
            <label
              for="user"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              用户名
            </label>
            <input
              type="text"
              id="user"
              name="user"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入用户名"
              required
            />
          </div>

          <div>
            <label
              for="pwd"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              密码
            </label>
            <input
              type="password"
              id="pwd"
              name="pwd"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入密码"
              required
            />
          </div>

          <button
            type="submit"
            class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-3 rounded-lg font-semibold hover:from-blue-600 hover:to-indigo-700 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
          >
            登录
          </button>

          <div class="text-center pt-4">
            <a
              href="register.jsp"
              class="text-blue-600 hover:text-blue-800 font-medium transition-colors"
            >
              还没有账户？立即注册
            </a>
          </div>
        </form>
      </div>
    </div>

    <script>
      // 检查URL参数，显示相应提示
      window.onload = function () {
        const urlParams = new URLSearchParams(window.location.search);
        const logout = urlParams.get('logout');

        if (logout === 'success') {
          $tips.success('已成功退出登录');
          // 清除URL参数
          window.history.replaceState(
            {},
            document.title,
            window.location.pathname
          );
        }
      };

      // 处理登录表单提交
      async function handleLogin(event) {
        event.preventDefault();

        const form = event.target;
        const formData = new FormData(form);

        try {
          const response = await axios.post('UserServlet', formData);

          // 解析JSON响应
          let result;
          if (typeof response.data === 'string') {
            try {
              result = JSON.parse(response.data);
            } catch (e) {
              // 如果不是JSON，可能是重定向或其他响应
              if (
                response.request.responseURL &&
                response.request.responseURL.includes('StudentServlet')
              ) {
                result = { success: true, message: '登录成功' };
              } else {
                result = { success: false, message: '登录失败' };
              }
            }
          } else {
            result = response.data;
          }

          if (result.success) {
            $tips.success(result.message + '！正在跳转...');
            setTimeout(() => {
              window.location.href = 'StudentServlet?action=getAll';
            }, 1000);
          } else {
            $tips.error(result.message);
          }
        } catch (error) {
          console.error('登录时出错:', error);
          $tips.error('登录失败，请稍后重试');
        }
      }
    </script>
  </body>
</html>

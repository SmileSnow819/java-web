<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>用户注册 - 学生管理系统</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/tips.js"></script>
  </head>
  <body
    class="bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 min-h-screen flex items-center justify-center p-4"
  >
    <div id="app" class="w-full max-w-md">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-800 mb-2">创建账户</h1>
          <p class="text-gray-600">注册新用户账户</p>
        </div>

        <c:if test="${not empty msg}">
          <div
            class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg"
          >
            ${msg}
          </div>
        </c:if>

        <form
          @submit.prevent="handleSubmit"
          action="UserServlet"
          method="post"
          class="space-y-6"
        >
          <input type="hidden" name="action" value="register" />

          <div>
            <label
              for="user"
              class="block text-sm font-medium text-gray-700 mb-2"
            >
              用户名
            </label>
            <div class="relative">
              <input
                type="text"
                id="user"
                name="user"
                v-model="username"
                @input="checkUsername"
                :class="[
                  'w-full px-4 py-3 border rounded-lg focus:ring-2 focus:border-transparent outline-none transition-all',
                  usernameStatus === 'error' ? 'border-red-500 focus:ring-red-500' : 
                  usernameStatus === 'success' ? 'border-green-500 focus:ring-green-500' : 
                  'border-gray-300 focus:ring-blue-500'
                ]"
                placeholder="请输入用户名"
                required
              />
            </div>
            <div
              v-if="usernameCheckMessage"
              :class="[
                   'mt-1 text-sm',
                   usernameStatus === 'error' ? 'text-red-600' : 'text-green-600'
                 ]"
            >
              {{ usernameCheckMessage }}
            </div>
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
              v-model="password"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="请输入密码（至少6位）"
              required
              minlength="6"
            />
          </div>

          <button
            type="submit"
            :disabled="!canSubmit"
            :class="[
              'w-full py-3 rounded-lg font-semibold transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5',
              canSubmit ? 'bg-gradient-to-r from-blue-500 to-indigo-600 text-white hover:from-blue-600 hover:to-indigo-700' : 
                          'bg-gray-400 text-gray-600 cursor-not-allowed'
            ]"
          >
            注册
          </button>

          <div class="text-center pt-4">
            <a
              href="login.jsp"
              class="text-blue-600 hover:text-blue-800 font-medium transition-colors"
            >
              已有账户？立即登录
            </a>
          </div>
        </form>
      </div>
    </div>

    <script>
      const { createApp, ref, computed } = Vue;

      createApp({
        setup() {
          // 响应式数据
          const username = ref('');
          const password = ref('');
          const usernameStatus = ref(''); // 'success', 'error', ''
          const usernameCheckMessage = ref('');
          const isChecking = ref(false);

          // 检查用户名是否可用
          const checkUsername = async () => {
            const u_name = username.value.trim();

            // 如果用户名为空，清除验证信息
            if (!u_name) {
              usernameStatus.value = '';
              usernameCheckMessage.value = '';
              return;
            }

            // 防抖处理：等待用户输入停止后再发送请求
            clearTimeout(checkUsername.timer);
            checkUsername.timer = setTimeout(async () => {
              isChecking.value = true;
              try {
                console.log('发送用户名验证请求，用户名:', u_name);

                const response = await axios.get('UserServlet', {
                  params: {
                    action: 'checkUsername',
                    u_name: u_name,
                  },
                });

                console.log('收到服务器响应:', response.data);
                console.log('响应数据类型:', typeof response.data);

                // 响应数据是字符串 'true' 或 'false'，也可能是布尔值
                let responseData = response.data;
                // 处理可能的字符串响应
                if (typeof responseData === 'string') {
                  responseData = responseData.trim();
                }

                console.log('处理后的响应数据:', responseData);

                const isAvailable =
                  responseData === true ||
                  responseData === 'true' ||
                  (typeof responseData === 'string' &&
                    responseData.toLowerCase() === 'true');

                console.log('用户名是否可用:', isAvailable);

                if (isAvailable) {
                  usernameStatus.value = 'success';
                  usernameCheckMessage.value = '用户名可用';
                  console.log('设置状态为success');
                } else {
                  usernameStatus.value = 'error';
                  usernameCheckMessage.value = '该账号已经被注册过';
                  console.log('设置状态为error');
                }
              } catch (error) {
                console.error('验证账号时出错:', error);
                usernameStatus.value = 'error';
                usernameCheckMessage.value = '验证失败，请稍后重试';
              } finally {
                isChecking.value = false;
              }
            }, 500); // 500ms 防抖延迟
          };

          // 计算属性：是否可以提交表单
          const canSubmit = computed(() => {
            return (
              usernameStatus.value === 'success' &&
              username.value.trim() !== '' &&
              password.value.trim() !== '' &&
              !isChecking.value
            );
          });

          // 表单提交处理
          const handleSubmit = async (event) => {
            console.log('handleSubmit 被调用');

            // 如果账号已被注册或正在验证，阻止提交
            if (usernameStatus.value !== 'success' || isChecking.value) {
              event.preventDefault();
              $tips.warning('请先验证账号是否可用');
              return false;
            }

            // 验证通过，使用AJAX提交表单
            event.preventDefault();

            try {
              const formData = new FormData();
              formData.append('action', 'register');
              formData.append('user', username.value.trim());
              formData.append('pwd', password.value.trim());

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
                    response.request.responseURL.includes('login.jsp')
                  ) {
                    result = { success: true, message: '注册成功' };
                  } else {
                    result = { success: false, message: '注册失败' };
                  }
                }
              } else {
                result = response.data;
              }

              if (result.success) {
                $tips.success(result.message + '！正在跳转到登录页面...');
                setTimeout(() => {
                  window.location.href = 'login.jsp';
                }, 1500);
              } else {
                $tips.error(result.message);
              }
            } catch (error) {
              console.error('注册时出错:', error);
              $tips.error('注册失败，请稍后重试');
            }
          };

          return {
            username,
            password,
            usernameStatus,
            usernameCheckMessage,
            isChecking,
            checkUsername,
            canSubmit,
            handleSubmit,
          };
        },
      }).mount('#app');
    </script>
  </body>
</html>

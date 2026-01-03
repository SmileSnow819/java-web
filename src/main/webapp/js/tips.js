// 简单的提示工具类
var $tips = {
  // 显示成功提示
  success: function (message) {
    this.show(message, 'success');
  },

  // 显示错误提示
  error: function (message) {
    this.show(message, 'error');
  },

  // 显示信息提示
  info: function (message) {
    this.show(message, 'info');
  },

  // 显示警告提示
  warning: function (message) {
    this.show(message, 'warning');
  },

  // 显示提示的核心方法
  show: function (message, type) {
    // 移除已存在的提示
    var existingTip = document.getElementById('tips-message');
    if (existingTip) {
      existingTip.remove();
    }

    // 创建提示元素
    var tip = document.createElement('div');
    tip.id = 'tips-message';
    tip.style.cssText =
      'position: fixed; top: 20px; right: 20px; z-index: 9999; ' +
      'padding: 12px 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); ' +
      'animation: slideIn 0.3s ease-out; max-width: 400px;';

    // 根据类型设置样式
    var colors = {
      success: { bg: '#10b981', color: '#ffffff' },
      error: { bg: '#ef4444', color: '#ffffff' },
      info: { bg: '#3b82f6', color: '#ffffff' },
      warning: { bg: '#f59e0b', color: '#ffffff' },
    };

    var style = colors[type] || colors.info;
    tip.style.backgroundColor = style.bg;
    tip.style.color = style.color;
    tip.textContent = message;

    // 添加到页面
    document.body.appendChild(tip);

    // 3秒后自动移除
    setTimeout(function () {
      tip.style.animation = 'slideOut 0.3s ease-out';
      setTimeout(function () {
        if (tip.parentNode) {
          tip.parentNode.removeChild(tip);
        }
      }, 300);
    }, 3000);
  },
};

// 添加动画样式
var style = document.createElement('style');
style.textContent = `
  @keyframes slideIn {
    from {
      transform: translateX(400px);
      opacity: 0;
    }
    to {
      transform: translateX(0);
      opacity: 1;
    }
  }
  
  @keyframes slideOut {
    from {
      transform: translateX(0);
      opacity: 1;
    }
    to {
      transform: translateX(400px);
      opacity: 0;
    }
  }
`;
document.head.appendChild(style);

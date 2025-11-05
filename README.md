# Java Web 项目模板

欢迎使用本项目模板！这是一个基础的 Java Web 应用，用于演示用户注册、登录及基本的数据操作。

在使用本项目之前，您**必须**根据您的环境和偏好进行以下两项重要的配置更改。

## 一、先决条件

- Java Development Kit (JDK)
- Apache Tomcat 或其他 Servlet 容器
- MySQL 或其他关系型数据库
- Maven (如果使用 IDE 通常已内置)

## 二、项目配置指南

### 1. 数据库连接配置

您需要将数据库的连接信息修改为您的本地配置。

**关键修改文件：**

请定位项目中的数据库工具类文件：

- `src/main/java/untl/DBUtil.java`

**操作步骤：**

1. 打开 `DBUtil.java` 文件。
2. 修改文件中的数据库连接常量，替换为您的实际数据库信息：
   - **JDBC URL (数据库地址):** 将连接地址替换为您的数据库路径。
   - **Username (用户名):** 替换为您数据库的用户名。
   - **Password (密码):** 替换为您数据库的密码。

```
// 示例：在 DBUtil.java 中修改
private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
// TODO: 请将 your_database 替换为您的数据库名称
private static final String URL = "jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC"; 
// TODO: 请将 root 替换为您的用户名
private static final String USERNAME = "root"; 
// TODO: 请将 password 替换为您的密码
private static final String PASSWORD = "password"; 
```

### 2. 基础包名重构

本项目默认使用 `com.yf` 作为基础包名。为了避免包名冲突，并使用您自己的组织或项目的缩写，**强烈建议**您将其替换为您的缩写（例如 `com.myorg` 或 `com.abc`）。

**关键修改路径：**

- 项目的主 Java 源代码包：`src/main/java/com/yf/...`

**操作步骤：**

1. **使用 IDE 重构 (推荐方式)：**
   - 在您的 IDE (如 IntelliJ IDEA 或 Eclipse) 中，右键点击项目视图中的基础包 `com.yf`。
   - 选择 **Refactor (重构)** -> **Rename (重命名)**。
   - 输入您新的缩写包名（例如 `myorg`）。
   - 确认重构操作，IDE 会自动更新所有文件和引用。
2. **手动修改：**
   - 在文件系统中重命名 `src/main/java/com/yf` 目录。
   - 全局搜索所有 Java 文件，将所有的 `package com.yf...` 语句替换为您的新包名（例如 `package com.myorg...`）。

配置完成后，您即可构建和部署项目到您的 Web 容器中运行。

Java Web 项目模板

欢迎使用本项目模板！这是一个基础的 Java Web 应用，用于演示用户注册、登录及基本的数据操作。

在使用之前，请务必根据您的环境和偏好进行以下两项重要的配置更改。

一、先决条件

Java Development Kit (JDK)

Apache Tomcat 或其他 Servlet 容器

MySQL 或其他关系型数据库

Maven (用于项目构建，如果使用IDE通常已内置)

二、项目配置指南

1. 数据库连接配置

您需要将数据库的连接信息修改为您的本地配置。

操作步骤：

找到并打开项目中的数据库工具类文件：src/main/java/util/DBUtil.java（路径可能为 untl/DBUtil.java 或其他类似路径，请自行定位）。

修改文件中的数据库连接常量，替换为您的数据库信息：

JDBC URL (数据库地址): 将连接地址（如 jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC）替换为您的数据库路径。

Username (用户名): 替换为您数据库的用户名。

Password (密码): 替换为您数据库的密码。

注意： 请确保您的数据库已创建相应的表结构。如果未创建，请先执行项目的 SQL 初始化脚本（如果有的话）。

2. 基础包名重构

为了避免包名冲突，并使用您自己的组织或项目的缩写，建议您更改基础包名。

本项目默认使用 com.yf 作为基础包名，您需要将其替换为您的缩写（例如 com.myorg 或 com.abc）。

操作步骤：

在 IDE 中执行重构 (推荐)：

打开您的 IDE (如 IntelliJ IDEA 或 Eclipse)。

在项目视图中找到基础包 com.yf。

右键点击 yf 包 -> 选择 Refactor (重构) -> 选择 Rename (重命名)。

输入您新的缩写包名（例如 myorg）。

确认重构操作，IDE 会自动更新所有引用到该包名的 Java 文件。

手动查找替换 (不推荐)：

如果您不使用 IDE 的重构功能，请确保在整个项目中全局搜索 com.yf 并将其替换为您的新包名（例如 com.myorg）。

同时，您需要手动在文件系统中重命名 src/main/java/com/yf 目录。

配置完成后，您即可构建和部署项目到您的 Web 容器中运行。

如果您在配置或运行过程中遇到任何问题，欢迎提出。

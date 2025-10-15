<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Student" %> <%-- 导入 Student 类 --%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>学生列表</title>
    <style>
        h2 {
            margin:0 auto;
        }
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
            border: 1px solid #ddd;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }

    </style>
</head>
<body>
    <h2>学生信息列表</h2>
    <%!
        // 这个方法将作为一个实例方法被添加到生成的 Servlet 类中
        public List<Student> createStudents() {
            List<Student> studentList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                String studentId = "S" + String.format("%03d", i); // S001, S002, ...
                String name = "学生" + i;
                int age = 18 + i; // 年龄从 19 到 28
                studentList.add(new Student(studentId, name, age));
            }
            return studentList;
        }
    %>

    <%
        // JSP 脚本：调用方法创建学生列表
        List<Student> students = createStudents();
    %>

    <%-- JSP 表达式和脚本：显示学生信息到表格 --%>
    <table>
        <thead>
            <tr>
                <th>学号</th>
                <th>姓名</th>
                <th>年龄</th>
            </tr>
        </thead>
        <tbody>
            <%
                // 遍历学生列表并显示每一行
                for (Student s : students) {
            %>
            <tr>
                <%-- 使用 JSP 表达式显示属性值 --%>
                <td><%= s.getStudentId() %></td>
                <td><%= s.getName() %></td>
                <td><%= s.getAge() %></td>
            </tr>
            <%
                } // 结束 for 循环
            %>
        </tbody>
    </table>

</body>
</html>
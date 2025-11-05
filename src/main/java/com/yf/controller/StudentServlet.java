package com.yf.controller;

import com.yf.model.Student;
import com.yf.service.StudentService;
import com.yf.service.impl.StudentServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {

    // 依赖 Service 层
    private StudentService studentService = new StudentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        // 使用 action 参数进行多功能分发
        String action = req.getParameter("action");

        if (action == null) {
            action = "getAll"; // 默认行为
        }

        switch (action) {
            case "getAll":
                getAllStu(req, resp); // 查询全部学生
                break;
            case "delStu":
                delStu(req, resp); // 删除学生
                break;
            case "toUpdate":
                getStuById(req, resp); // 根据ID查询学生，并转发到编辑页
                break;
            case "updateStu":
                updateStu(req, resp); // 编辑学生信息
                break;
            case "addStu":
                addStu(req, resp); // 录入学生信息
                break;
            default:
                getAllStu(req, resp);
        }
    }

    // --- 1. 查询全部学生 ---
    private void getAllStu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 调: 调用业务层方法
        List<Student> studentList = studentService.getAllStu();

        // 存: 存储功能结果
        req.setAttribute("studentList", studentList);

        // 转: 请求转发到展示页面
        req.getRequestDispatcher("getAllStu.jsp").forward(req, resp);
    }

    // --- 2. 录入学生 ---
    private void addStu(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 取: 获取请求中的数据
        String stuName = req.getParameter("stuName");
        // 安全起见，使用 try-catch 处理 Integer.parseInt
        int stuAge = 0;
        try {
            stuAge = Integer.parseInt(req.getParameter("stuAge"));
        } catch (NumberFormatException e) {
            req.setAttribute("msg", "年龄输入格式有误！");
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
            return;
        }

        // 封装 POJO
        Student stu = new Student();
        stu.setStuName(stuName);
        stu.setStuAge(stuAge);

        // 调: 调用业务层方法
        int num = studentService.addStu(stu);

        if (num > 0) { // 录入成功
            // 转: 重定向到查询全部学生的界面
            resp.sendRedirect("StudentServlet?action=getAll");
        } else { // 录入失败（可能业务校验失败）
            // 存: 存储功能结果
            req.setAttribute("msg", "录入失败，请检查数据是否符合要求！");
            // 转: 请求转发回录入页面
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
        }
    }

    // --- 3. 开除学生（删除）---
    private void delStu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 取: 获取开除学生的学号
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));

        // 调: 调用业务层中删除的方法
        studentService.delStu(stuNo);

        // 转: 重定向回查询全部学生的界面
        resp.sendRedirect("StudentServlet?action=getAll");
    }

    // --- 4. 根据ID查询学生（用于跳转到编辑页）---
    private void getStuById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取学号
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));

        // 调: 调用业务层中查询的方法
        Student stu = studentService.getStuById(stuNo);

        // 存: 把查询到的数据带到编辑页面上
        req.setAttribute("student", stu);

        // 转: 请求转发到编辑页面
        req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
    }

    // --- 5. 编辑学生 ---
    private void updateStu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 取: 获取所有参数
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));
        String stuName = req.getParameter("stuName");
        int stuAge = Integer.parseInt(req.getParameter("stuAge"));

        // 封装 POJO
        Student stu = new Student();
        stu.setStuNo(stuNo);
        stu.setStuName(stuName);
        stu.setStuAge(stuAge);

        // 调: 调用业务层修改的方法
        studentService.updateStu(stu);

        // 转: 修改成功后回到查询全部学生的界面
        resp.sendRedirect("StudentServlet?action=getAll");
    }
}
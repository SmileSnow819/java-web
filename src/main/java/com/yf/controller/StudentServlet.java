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
        
        // 检查用户是否登录
        Object currentUser = req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            // 未登录，重定向到登录页面
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
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
            case "getStuPage":
                getStuPage(req, resp); // 分页显示学生信息
                break;
            default:
                getAllStu(req, resp);
        }
    }

    // --- 1. 查询全部学生 ---
    private void getAllStu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取查询条件
        Integer stuNo = null;
        String stuNoStr = req.getParameter("stuNo");
        if (stuNoStr != null && !stuNoStr.trim().isEmpty()) {
            try {
                stuNo = Integer.parseInt(stuNoStr);
            } catch (NumberFormatException e) {
                // 忽略无效的学号参数
            }
        }
        
        String stuName = req.getParameter("stuName");
        if (stuName != null && stuName.trim().isEmpty()) {
            stuName = null;
        }
        
        Integer stuAge = null;
        String stuAgeStr = req.getParameter("stuAge");
        if (stuAgeStr != null && !stuAgeStr.trim().isEmpty()) {
            try {
                stuAge = Integer.parseInt(stuAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }
        
        // 调: 调用业务层方法（带搜索条件）
        List<Student> studentList = studentService.getAllStu(stuNo, stuName, stuAge);

        // 存: 存储功能结果和查询条件（用于页面回显）
        req.setAttribute("studentList", studentList);
        req.setAttribute("searchStuNo", stuNoStr);
        req.setAttribute("searchStuName", stuName);
        req.setAttribute("searchStuAge", stuAgeStr);

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
            // 获取当前页信息（从session或参数中获取）
            String pageNow = (String) req.getSession().getAttribute("currentPageNow");
            if (pageNow == null) {
                pageNow = "1";
            }
            // 获取查询条件（从session中获取）
            String searchParams = (String) req.getSession().getAttribute("searchParams");
            if (searchParams != null && !searchParams.isEmpty()) {
                // 转: 重定向到分页界面，保留查询条件和当前页
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow + "&" + searchParams);
            } else {
                // 转: 重定向到分页界面，保留当前页
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow);
            }
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
        
        // 判断返回视图类型（分页视图还是全查视图）
        String returnView = req.getParameter("returnView");
        
        // 获取查询条件（从参数中获取）
        StringBuilder searchParams = new StringBuilder();
        String searchStuNo = req.getParameter("searchStuNo");
        String searchStuName = req.getParameter("searchStuName");
        String searchStuAge = req.getParameter("searchStuAge");
        
        // 如果没有searchStuNo等参数，尝试从stuNo等参数获取（全查视图的情况）
        if (searchStuNo == null || searchStuNo.trim().isEmpty()) {
            String stuNoParam = req.getParameter("stuNo");
            // 注意：stuNo参数可能是要删除的学号，需要区分
            // 这里假设如果returnView=getAll，则查询条件在stuNo、stuName、stuAge中
            // 但这样会有冲突，所以全查视图删除时应该用不同的参数名
        }
        
        // 从请求参数中获取查询条件（全查视图的情况，使用不同的参数名避免冲突）
        String queryStuNo = req.getParameter("queryStuNo");
        String queryStuName = req.getParameter("queryStuName");
        String queryStuAge = req.getParameter("queryStuAge");
        
        // 优先使用queryStuNo等参数（全查视图），否则使用searchStuNo等参数（分页视图）
        if (queryStuNo != null && !queryStuNo.trim().isEmpty()) {
            searchParams.append("&stuNo=").append(queryStuNo);
        } else if (searchStuNo != null && !searchStuNo.trim().isEmpty()) {
            searchParams.append("&stuNo=").append(searchStuNo);
        }
        
        if (queryStuName != null && !queryStuName.trim().isEmpty()) {
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(queryStuName, "UTF-8"));
        } else if (searchStuName != null && !searchStuName.trim().isEmpty()) {
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(searchStuName, "UTF-8"));
        }
        
        if (queryStuAge != null && !queryStuAge.trim().isEmpty()) {
            searchParams.append("&stuAge=").append(queryStuAge);
        } else if (searchStuAge != null && !searchStuAge.trim().isEmpty()) {
            searchParams.append("&stuAge=").append(searchStuAge);
        }

        // 调: 调用业务层中删除的方法
        studentService.delStu(stuNo);

        // 转: 根据返回视图类型重定向
        if ("getAll".equals(returnView)) {
            // 返回到全查视图，保留查询条件
            if (searchParams.length() > 0) {
                resp.sendRedirect("StudentServlet?action=getAll" + searchParams.toString());
            } else {
                resp.sendRedirect("StudentServlet?action=getAll");
            }
        } else {
            // 返回到分页视图，保留当前页和查询条件
            String pageNow = req.getParameter("pageNow");
            if (pageNow == null || pageNow.trim().isEmpty()) {
                pageNow = (String) req.getSession().getAttribute("currentPageNow");
                if (pageNow == null) {
                    pageNow = "1";
                }
            }
            
            if (searchParams.length() > 0) {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow + searchParams.toString());
            } else {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow);
            }
        }
    }

    // --- 4. 根据ID查询学生（用于跳转到编辑页）---
    private void getStuById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取学号
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));
        
        // 获取当前页和查询条件（用于编辑后返回）
        String pageNow = req.getParameter("pageNow");
        String searchStuNo = req.getParameter("searchStuNo");
        String searchStuName = req.getParameter("searchStuName");
        String searchStuAge = req.getParameter("searchStuAge");
        
        // 判断是从分页视图还是全查视图跳转的
        String returnView = "getStuPage"; // 默认返回分页视图
        if (pageNow == null || pageNow.trim().isEmpty()) {
            returnView = "getAll"; // 如果没有pageNow参数，说明是从全查视图跳转的
        }

        // 调: 调用业务层中查询的方法
        Student stu = studentService.getStuById(stuNo);

        // 存: 把查询到的数据和返回信息带到编辑页面上
        req.setAttribute("student", stu);
        req.setAttribute("pageNow", pageNow);
        req.setAttribute("searchStuNo", searchStuNo);
        req.setAttribute("searchStuName", searchStuName);
        req.setAttribute("searchStuAge", searchStuAge);
        req.setAttribute("returnView", returnView);

        // 转: 请求转发到编辑页面
        req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
    }

    // --- 5. 编辑学生 ---
    private void updateStu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 取: 获取所有参数
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));
        String stuName = req.getParameter("stuName");
        int stuAge = Integer.parseInt(req.getParameter("stuAge"));
        
        // 获取返回视图类型
        String returnView = req.getParameter("returnView");
        if (returnView == null || returnView.trim().isEmpty()) {
            returnView = "getStuPage"; // 默认返回分页视图
        }
        
        // 获取当前页信息
        String pageNow = req.getParameter("pageNow");
        if (pageNow == null || pageNow.trim().isEmpty()) {
            pageNow = (String) req.getSession().getAttribute("currentPageNow");
            if (pageNow == null) {
                pageNow = "1";
            }
        }
        
        // 获取查询条件（从参数中获取）
        StringBuilder searchParams = new StringBuilder();
        String searchStuNo = req.getParameter("searchStuNo");
        String searchStuName = req.getParameter("searchStuName");
        String searchStuAge = req.getParameter("searchStuAge");
        
        if (searchStuNo != null && !searchStuNo.trim().isEmpty()) {
            searchParams.append("&stuNo=").append(searchStuNo);
        }
        if (searchStuName != null && !searchStuName.trim().isEmpty()) {
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(searchStuName, "UTF-8"));
        }
        if (searchStuAge != null && !searchStuAge.trim().isEmpty()) {
            searchParams.append("&stuAge=").append(searchStuAge);
        }

        // 封装 POJO
        Student stu = new Student();
        stu.setStuNo(stuNo);
        stu.setStuName(stuName);
        stu.setStuAge(stuAge);

        // 调: 调用业务层修改的方法
        studentService.updateStu(stu);

        // 转: 根据返回视图类型重定向
        if ("getAll".equals(returnView)) {
            // 返回到全查视图，保留查询条件
            if (searchParams.length() > 0) {
                resp.sendRedirect("StudentServlet?action=getAll" + searchParams.toString());
            } else {
                resp.sendRedirect("StudentServlet?action=getAll");
            }
        } else {
            // 返回到分页界面，保留当前页和查询条件
            if (searchParams.length() > 0) {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow + searchParams.toString());
            } else {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow);
            }
        }
    }
    
    // --- 6. 分页显示学生信息 ---
    private void getStuPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取客户端请求的信息
        String pageNowStr = req.getParameter("pageNow");
        int pageNow = 1;
        if (pageNowStr != null && !pageNowStr.trim().isEmpty()) {
            try {
                pageNow = Integer.parseInt(pageNowStr);
            } catch (NumberFormatException e) {
                pageNow = 1;
            }
        }
        
        // 保存当前页到session
        req.getSession().setAttribute("currentPageNow", String.valueOf(pageNow));
        
        // 取: 获取查询条件
        Integer stuNo = null;
        String stuNoStr = req.getParameter("stuNo");
        if (stuNoStr != null && !stuNoStr.trim().isEmpty()) {
            try {
                stuNo = Integer.parseInt(stuNoStr);
            } catch (NumberFormatException e) {
                // 忽略无效的学号参数
            }
        }
        
        String stuName = req.getParameter("stuName");
        if (stuName != null && stuName.trim().isEmpty()) {
            stuName = null;
        }
        
        Integer stuAge = null;
        String stuAgeStr = req.getParameter("stuAge");
        if (stuAgeStr != null && !stuAgeStr.trim().isEmpty()) {
            try {
                stuAge = Integer.parseInt(stuAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }
        
        // 保存查询条件到session（用于其他操作后返回）
        StringBuilder searchParams = new StringBuilder();
        if (stuNoStr != null && !stuNoStr.trim().isEmpty()) {
            searchParams.append("stuNo=").append(stuNoStr);
        }
        if (stuName != null && !stuName.trim().isEmpty()) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("stuName=").append(java.net.URLEncoder.encode(stuName, "UTF-8"));
        }
        if (stuAgeStr != null && !stuAgeStr.trim().isEmpty()) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("stuAge=").append(stuAgeStr);
        }
        req.getSession().setAttribute("searchParams", searchParams.toString());
        
        // 调: 调用分页业务（带搜索条件）
        com.yf.util.PageBean page = studentService.getStuPage(pageNow, stuNo, stuName, stuAge);
        
        // 把分页信息和查询条件保存到request的作用域当中
        req.setAttribute("page", page);
        req.setAttribute("searchStuNo", stuNoStr);
        req.setAttribute("searchStuName", stuName);
        req.setAttribute("searchStuAge", stuAgeStr);
        
        // 跳转至分页页面 (请求转发)
        req.getRequestDispatcher("getStuPage.jsp").forward(req, resp);
    }
}
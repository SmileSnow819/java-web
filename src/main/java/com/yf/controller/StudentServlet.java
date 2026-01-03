package com.yf.controller;

import com.yf.model.Student;
import com.yf.service.StudentService;
import com.yf.service.impl.StudentServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        String action = null;
        
        // 检查是否为multipart请求（文件上传）
        if (ServletFileUpload.isMultipartContent(req)) {
            // 对于multipart请求，action参数可能在URL中，也可能在表单数据中
            // 先尝试从URL参数获取
            action = req.getParameter("action");
            
            // 如果URL中没有action，说明在表单数据中
            // 对于这种情况，我们需要从表单数据中解析action
            // 但为了避免重复解析请求体，我们创建一个包装请求来缓存解析结果
            if (action == null) {
                try {
                    // 创建一个可重用的请求包装器
                    // 先解析一次获取action
                    DiskFileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    upload.setHeaderEncoding("UTF-8");
                    
                    // 解析请求（注意：这只能读取一次）
                    java.util.List<FileItem> items = upload.parseRequest(req);
                    
                    // 从解析结果中查找action参数
                    for (FileItem item : items) {
                        if (item.isFormField() && "action".equals(item.getFieldName())) {
                            action = item.getString("UTF-8");
                            break;
                        }
                    }
                    
                    // 将解析结果存储到request attribute中，供后续方法使用
                    req.setAttribute("parsedFileItems", items);
                    
                } catch (Exception e) {
                    System.out.println("[StudentServlet] 解析multipart请求失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            // 普通请求，直接获取参数
            action = req.getParameter("action");
        }

        if (action == null) {
            action = "getAll"; // 默认行为
        }
        
        System.out.println("[StudentServlet] doPost - action: " + action + ", isMultipart: " + ServletFileUpload.isMultipartContent(req));

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
            case "getDetail":
                getDetail(req, resp); // 查看学生详情
                break;
            case "exportExcel":
                exportExcel(req, resp); // 导出Excel
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
        
        Integer startAge = null;
        String startAgeStr = req.getParameter("startAge");
        if (startAgeStr != null && !startAgeStr.trim().isEmpty()) {
            try {
                startAge = Integer.parseInt(startAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }

        Integer endAge = null;
        String endAgeStr = req.getParameter("endAge");
        if (endAgeStr != null && !endAgeStr.trim().isEmpty()) {
            try {
                endAge = Integer.parseInt(endAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }
        
        // 调: 调用业务层方法（带搜索条件）
        List<Student> studentList = studentService.getAllStu(stuNo, stuName, startAge, endAge);

        // 存: 存储功能结果和查询条件（用于页面回显）
        req.setAttribute("studentList", studentList);
        req.setAttribute("searchStuNo", stuNoStr);
        req.setAttribute("searchStuName", stuName);
        req.setAttribute("searchStartAge", startAgeStr);
        req.setAttribute("searchEndAge", endAgeStr);

        // 转: 请求转发到展示页面
        req.getRequestDispatcher("getAllStu.jsp").forward(req, resp);
    }

    // --- 2. 录入学生 ---
    private void addStu(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("[StudentServlet] 开始处理学生录入请求");
        
        // 检查是否为multipart请求（文件上传）
        if (!ServletFileUpload.isMultipartContent(req)) {
            System.out.println("[StudentServlet] 错误：不是multipart请求");
            req.setAttribute("msg", "请求格式错误！请确保表单设置了enctype=\"multipart/form-data\"");
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
            return;
        }

        // 初始化变量
        String stuName = null;
        int stuAge = 0;
        String stuImg = null;  // 图片路径
        String returnView = "getAll";
        String searchStuNo = null;
        String searchStuName = null;
        String searchStartAge = null;
        String searchEndAge = null;

        List<FileItem> items = null;
        
        try {
            // 检查是否已经解析过（从doPost方法传递过来的）
            @SuppressWarnings("unchecked")
            List<FileItem> cachedItems = (List<FileItem>) req.getAttribute("parsedFileItems");
            
            if (cachedItems != null) {
                // 使用已解析的items
                System.out.println("[StudentServlet] 使用已缓存的解析结果");
                items = cachedItems;
            } else {
                // 需要重新解析请求
                System.out.println("[StudentServlet] 开始解析multipart请求");
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");  // 设置编码
                
                // 解析请求
                items = upload.parseRequest(req);
            }
            
            // 获取上传文件的保存目录
            String uploadPath = req.getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 遍历所有表单项
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 普通表单字段
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    
                    switch (fieldName) {
                        case "stuName":
                            stuName = fieldValue;
                            break;
                        case "stuAge":
                            try {
                                stuAge = Integer.parseInt(fieldValue);
                            } catch (NumberFormatException e) {
                                req.setAttribute("msg", "年龄输入格式有误！");
                                req.getRequestDispatcher("addStu.jsp").forward(req, resp);
                                return;
                            }
                            break;
                        case "returnView":
                            returnView = fieldValue;
                            break;
                        case "searchStuNo":
                            searchStuNo = fieldValue;
                            break;
                        case "searchStuName":
                            searchStuName = fieldValue;
                            break;
                        case "startAge":
                            searchStartAge = fieldValue;
                            break;
                        case "endAge":
                            searchEndAge = fieldValue;
                            break;
                    }
                } else {
                    // 文件字段
                    if ("stuImg".equals(item.getFieldName()) && item.getSize() > 0) {
                        String fileName = item.getName();
                        if (fileName != null && !fileName.trim().isEmpty()) {
                            // 生成唯一文件名（使用UUID避免重名）
                            String ext = fileName.substring(fileName.lastIndexOf("."));
                            String newFileName = UUID.randomUUID().toString() + ext;
                            String filePath = uploadPath + File.separator + newFileName;
                            
                            // 保存文件
                            File storeFile = new File(filePath);
                            try (InputStream is = item.getInputStream();
                                 FileOutputStream fos = new FileOutputStream(storeFile)) {
                                IOUtils.copy(is, fos);
                            }
                            
                            // 保存相对路径到数据库（相对于webapp根目录）
                            stuImg = "images/" + newFileName;
                            System.out.println("[StudentServlet] 图片上传成功: " + stuImg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[StudentServlet] 解析请求时发生异常: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("msg", "文件上传失败: " + e.getMessage());
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
            return;
        }

        System.out.println("[StudentServlet] 解析完成 - 姓名: " + stuName + ", 年龄: " + stuAge + ", 头像: " + stuImg);

        // 验证必填字段
        if (stuName == null || stuName.trim().isEmpty()) {
            System.out.println("[StudentServlet] 验证失败：学生姓名为空");
            req.setAttribute("msg", "学生姓名不能为空！");
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
            return;
        }
        
        if (stuImg == null || stuImg.trim().isEmpty()) {
            System.out.println("[StudentServlet] 验证失败：学生头像为空");
            req.setAttribute("msg", "请选择学生头像！");
            req.getRequestDispatcher("addStu.jsp").forward(req, resp);
            return;
        }

        // 封装 POJO
        Student stu = new Student();
        stu.setStuName(stuName);
        stu.setStuAge(stuAge);
        stu.setStuImg(stuImg);

        // 调: 调用业务层方法
        System.out.println("[StudentServlet] 准备保存学生信息到数据库");
        int num = studentService.addStu(stu);
        System.out.println("[StudentServlet] 保存结果: " + num);

        if (num > 0) { // 录入成功
            System.out.println("[StudentServlet] 学生录入成功");
            // 构建查询条件参数
            StringBuilder searchParams = new StringBuilder();
            
            if (searchStuNo != null && !searchStuNo.trim().isEmpty()) {
                searchParams.append("&stuNo=").append(searchStuNo);
            }
            if (searchStuName != null && !searchStuName.trim().isEmpty()) {
                searchParams.append("&stuName=").append(java.net.URLEncoder.encode(searchStuName, "UTF-8"));
            }
            if (searchStartAge != null && !searchStartAge.trim().isEmpty()) {
                searchParams.append("&startAge=").append(searchStartAge);
            }
            if (searchEndAge != null && !searchEndAge.trim().isEmpty()) {
                searchParams.append("&endAge=").append(searchEndAge);
            }

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
        } else { // 录入失败（可能业务校验失败）
            System.out.println("[StudentServlet] 学生录入失败（数据库返回0）");
            // 存: 存储功能结果
            req.setAttribute("msg", "录入失败，请检查数据是否符合要求！（年龄必须在15-60岁之间）");
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
        String searchStartAge = req.getParameter("startAge");
        String searchEndAge = req.getParameter("endAge");
        
        // 注意：stuNo参数是要删除的学号，查询条件使用searchStuNo或queryStuNo
        
        // 从请求参数中获取查询条件（全查视图的情况，使用不同的参数名避免冲突）
        String queryStuNo = req.getParameter("queryStuNo");
        String queryStuName = req.getParameter("queryStuName");
        String queryStartAge = req.getParameter("queryStartAge");
        String queryEndAge = req.getParameter("queryEndAge");
        
        // 解析查询条件为Integer类型（用于后续查询）
        Integer stuNoCondition = null;
        String stuNameCondition = null;
        Integer startAgeCondition = null;
        Integer endAgeCondition = null;
        
        // 优先使用queryStuNo等参数（全查视图），否则使用searchStuNo等参数（分页视图）
        if (queryStuNo != null && !queryStuNo.trim().isEmpty()) {
            try {
                stuNoCondition = Integer.parseInt(queryStuNo);
                searchParams.append("&stuNo=").append(queryStuNo);
            } catch (NumberFormatException e) {
                // 忽略无效的学号参数
            }
        } else if (searchStuNo != null && !searchStuNo.trim().isEmpty()) {
            try {
                stuNoCondition = Integer.parseInt(searchStuNo);
                searchParams.append("&stuNo=").append(searchStuNo);
            } catch (NumberFormatException e) {
                // 忽略无效的学号参数
            }
        }
        
        if (queryStuName != null && !queryStuName.trim().isEmpty()) {
            stuNameCondition = queryStuName;
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(queryStuName, "UTF-8"));
        } else if (searchStuName != null && !searchStuName.trim().isEmpty()) {
            stuNameCondition = searchStuName;
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(searchStuName, "UTF-8"));
        }
        
        if (queryStartAge != null && !queryStartAge.trim().isEmpty()) {
            try {
                startAgeCondition = Integer.parseInt(queryStartAge);
                searchParams.append("&startAge=").append(queryStartAge);
            } catch (NumberFormatException e) {
            }
        } else if (searchStartAge != null && !searchStartAge.trim().isEmpty()) {
            try {
                startAgeCondition = Integer.parseInt(searchStartAge);
                searchParams.append("&startAge=").append(searchStartAge);
            } catch (NumberFormatException e) {
            }
        }

        if (queryEndAge != null && !queryEndAge.trim().isEmpty()) {
            try {
                endAgeCondition = Integer.parseInt(queryEndAge);
                searchParams.append("&endAge=").append(queryEndAge);
            } catch (NumberFormatException e) {
            }
        } else if (searchEndAge != null && !searchEndAge.trim().isEmpty()) {
            try {
                endAgeCondition = Integer.parseInt(searchEndAge);
                searchParams.append("&endAge=").append(searchEndAge);
            } catch (NumberFormatException e) {
            }
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
            // 返回到分页视图，需要检查删除后当前页是否还有数据
            String pageNowStr = req.getParameter("pageNow");
            int pageNow = 1;
            if (pageNowStr != null && !pageNowStr.trim().isEmpty()) {
                try {
                    pageNow = Integer.parseInt(pageNowStr);
                } catch (NumberFormatException e) {
                    pageNow = 1;
                }
            } else {
                String sessionPageNow = (String) req.getSession().getAttribute("currentPageNow");
                if (sessionPageNow != null) {
                    try {
                        pageNow = Integer.parseInt(sessionPageNow);
                    } catch (NumberFormatException e) {
                        pageNow = 1;
                    }
                }
            }
            
            // 获取删除后的总记录数
            int totalCount = studentService.getStuCount(stuNoCondition, stuNameCondition, startAgeCondition, endAgeCondition);
            
            // 计算删除后的总页数（每页5条）
            int pageSize = 5;
            int totalPages = (totalCount + pageSize - 1) / pageSize; // 向上取整
            if (totalPages == 0) {
                totalPages = 1; // 至少有一页，即使没有数据
            }
            
            // 如果当前页大于总页数，则跳转到最后一页
            if (pageNow > totalPages) {
                pageNow = totalPages;
            }
            
            // 构建重定向URL
            if (searchParams.length() > 0) {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow + searchParams.toString());
            } else {
                resp.sendRedirect("StudentServlet?action=getStuPage&pageNow=" + pageNow);
            }
        }
    }

    // --- 4. 查看学生详情 ---
    private void getDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取学号
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));
        
        // 获取当前页和查询条件（用于返回时保留）
        String pageNow = req.getParameter("pageNow");
        String searchStuNo = req.getParameter("searchStuNo");
        String searchStuName = req.getParameter("searchStuName");
        String searchStartAge = req.getParameter("startAge");
        String searchEndAge = req.getParameter("endAge");
        
        // 判断是从分页视图还是全查视图跳转的
        String returnView = "getStuPage"; // 默认返回分页视图
        if (pageNow == null || pageNow.trim().isEmpty()) {
            returnView = "getAll"; // 如果没有pageNow参数，说明是从全查视图跳转的
        }

        // 调: 调用业务层中查询的方法
        Student stu = studentService.getStuById(stuNo);
        
        if (stu == null) {
            // 学生不存在，返回列表页
            resp.sendRedirect("StudentServlet?action=getAll");
            return;
        }

        // 存: 把查询到的数据和返回信息带到详情页面上
        req.setAttribute("student", stu);
        req.setAttribute("pageNow", pageNow);
        req.setAttribute("searchStuNo", searchStuNo);
        req.setAttribute("searchStuName", searchStuName);
        req.setAttribute("searchStartAge", searchStartAge);
        req.setAttribute("searchEndAge", searchEndAge);
        req.setAttribute("returnView", returnView);

        // 转: 请求转发到详情页面
        req.getRequestDispatcher("detailStu.jsp").forward(req, resp);
    }

    // --- 5. 根据ID查询学生（用于跳转到编辑页）---
    private void getStuById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 取: 获取学号
        int stuNo = Integer.parseInt(req.getParameter("stuNo"));
        
        // 获取当前页和查询条件（用于编辑后返回）
        String pageNow = req.getParameter("pageNow");
        String searchStuNo = req.getParameter("searchStuNo");
        String searchStuName = req.getParameter("searchStuName");
        String searchStartAge = req.getParameter("startAge");
        String searchEndAge = req.getParameter("endAge");
        
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
        req.setAttribute("searchStartAge", searchStartAge);
        req.setAttribute("searchEndAge", searchEndAge);
        req.setAttribute("returnView", returnView);

        // 转: 请求转发到编辑页面
        req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
    }

    // --- 6. 编辑学生 ---
    private void updateStu(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 检查是否为multipart请求（文件上传）
        if (!ServletFileUpload.isMultipartContent(req)) {
            System.out.println("[StudentServlet] 错误：编辑学生不是multipart请求");
            req.setAttribute("msg", "请求格式错误！请确保表单设置了enctype=\"multipart/form-data\"");
            // 需要重新获取学生信息并转发到编辑页面
            int stuNo = Integer.parseInt(req.getParameter("stuNo"));
            Student stu = studentService.getStuById(stuNo);
            req.setAttribute("student", stu);
            req.setAttribute("pageNow", req.getParameter("pageNow"));
            req.setAttribute("searchStuNo", req.getParameter("searchStuNo"));
            req.setAttribute("searchStuName", req.getParameter("searchStuName"));
            req.setAttribute("searchStartAge", req.getParameter("startAge"));
            req.setAttribute("searchEndAge", req.getParameter("endAge"));
            req.setAttribute("returnView", req.getParameter("returnView"));
            req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
            return;
        }

        // 初始化变量
        int stuNo = 0;
        String stuName = null;
        int stuAge = 0;
        String stuImg = null;  // 新上传的图片路径
        String returnView = "getStuPage";
        String pageNow = "1";
        String searchStuNo = null;
        String searchStuName = null;
        String searchStartAge = null;
        String searchEndAge = null;
        
        // 先查询原有学生信息，用于获取原有头像
        Student existingStu = null;

        List<FileItem> items = null;
        
        try {
            // 检查是否已经解析过（从doPost方法传递过来的）
            @SuppressWarnings("unchecked")
            List<FileItem> cachedItems = (List<FileItem>) req.getAttribute("parsedFileItems");
            
            if (cachedItems != null) {
                // 使用已解析的items
                System.out.println("[StudentServlet] 编辑学生 - 使用已缓存的解析结果");
                items = cachedItems;
            } else {
                // 需要重新解析请求
                System.out.println("[StudentServlet] 编辑学生 - 开始解析multipart请求");
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");  // 设置编码
                
                // 解析请求
                items = upload.parseRequest(req);
            }
            
            // 获取上传文件的保存目录
            String uploadPath = req.getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 遍历所有表单项
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 普通表单字段
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    
                    switch (fieldName) {
                        case "stuNo":
                            try {
                                stuNo = Integer.parseInt(fieldValue);
                            } catch (NumberFormatException e) {
                                req.setAttribute("msg", "学号格式错误！");
                                req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
                                return;
                            }
                            break;
                        case "stuName":
                            stuName = fieldValue;
                            break;
                        case "stuAge":
                            try {
                                stuAge = Integer.parseInt(fieldValue);
                            } catch (NumberFormatException e) {
                                req.setAttribute("msg", "年龄输入格式有误！");
                                req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
                                return;
                            }
                            break;
                        case "returnView":
                            returnView = fieldValue;
                            break;
                        case "pageNow":
                            pageNow = fieldValue;
                            break;
                        case "searchStuNo":
                            searchStuNo = fieldValue;
                            break;
                        case "searchStuName":
                            searchStuName = fieldValue;
                            break;
                        case "startAge":
                            searchStartAge = fieldValue;
                            break;
                        case "endAge":
                            searchEndAge = fieldValue;
                            break;
                    }
                } else {
                    // 文件字段
                    if ("stuImg".equals(item.getFieldName()) && item.getSize() > 0) {
                        String fileName = item.getName();
                        if (fileName != null && !fileName.trim().isEmpty()) {
                            // 验证文件类型
                            String contentType = item.getContentType();
                            if (contentType == null || !contentType.startsWith("image/")) {
                                req.setAttribute("msg", "请上传图片文件！");
                                if (stuNo > 0) {
                                    existingStu = studentService.getStuById(stuNo);
                                    req.setAttribute("student", existingStu);
                                    req.setAttribute("pageNow", pageNow);
                                    req.setAttribute("searchStuNo", searchStuNo);
                                    req.setAttribute("searchStuName", searchStuName);
                                    req.setAttribute("searchStartAge", searchStartAge);
                                    req.setAttribute("searchEndAge", searchEndAge);
                                    req.setAttribute("returnView", returnView);
                                }
                                req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
                                return;
                            }
                            
                            // 验证文件大小（限制为5MB）
                            if (item.getSize() > 5 * 1024 * 1024) {
                                req.setAttribute("msg", "图片大小不能超过5MB！");
                                if (stuNo > 0) {
                                    existingStu = studentService.getStuById(stuNo);
                                    req.setAttribute("student", existingStu);
                                    req.setAttribute("pageNow", pageNow);
                                    req.setAttribute("searchStuNo", searchStuNo);
                                    req.setAttribute("searchStuName", searchStuName);
                                    req.setAttribute("searchStartAge", searchStartAge);
                                    req.setAttribute("searchEndAge", searchEndAge);
                                    req.setAttribute("returnView", returnView);
                                }
                                req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
                                return;
                            }
                            
                            // 生成唯一文件名（使用UUID避免重名）
                            String ext = fileName.substring(fileName.lastIndexOf("."));
                            String newFileName = UUID.randomUUID().toString() + ext;
                            String filePath = uploadPath + File.separator + newFileName;
                            
                            // 保存文件
                            File storeFile = new File(filePath);
                            try (InputStream is = item.getInputStream();
                                 FileOutputStream fos = new FileOutputStream(storeFile)) {
                                IOUtils.copy(is, fos);
                            }
                            
                            // 保存相对路径到数据库（相对于webapp根目录）
                            stuImg = "images/" + newFileName;
                            System.out.println("[StudentServlet] 编辑学生 - 新头像上传成功: " + stuImg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[StudentServlet] 编辑学生 - 解析请求时发生异常: " + e.getMessage());
            req.setAttribute("msg", "文件上传失败: " + e.getMessage());
            if (stuNo > 0) {
                existingStu = studentService.getStuById(stuNo);
                req.setAttribute("student", existingStu);
                req.setAttribute("pageNow", pageNow);
                req.setAttribute("searchStuNo", searchStuNo);
                req.setAttribute("searchStuName", searchStuName);
                req.setAttribute("searchStartAge", searchStartAge);
                req.setAttribute("searchEndAge", searchEndAge);
                req.setAttribute("returnView", returnView);
            }
            req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
            return;
        }

        System.out.println("[StudentServlet] 编辑学生 - 解析完成 - 学号: " + stuNo + ", 姓名: " + stuName + ", 年龄: " + stuAge + ", 新头像: " + stuImg);

        // 验证必填字段
        if (stuNo <= 0) {
            req.setAttribute("msg", "学号不能为空！");
            if (stuNo > 0) {
                existingStu = studentService.getStuById(stuNo);
                req.setAttribute("student", existingStu);
                req.setAttribute("pageNow", pageNow);
                req.setAttribute("searchStuNo", searchStuNo);
                req.setAttribute("searchStuName", searchStuName);
                req.setAttribute("searchStartAge", searchStartAge);
                req.setAttribute("searchEndAge", searchEndAge);
                req.setAttribute("returnView", returnView);
            }
            req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
            return;
        }
        
        if (stuName == null || stuName.trim().isEmpty()) {
            req.setAttribute("msg", "姓名不能为空！");
            existingStu = studentService.getStuById(stuNo);
            req.setAttribute("student", existingStu);
            req.setAttribute("pageNow", pageNow);
            req.setAttribute("searchStuNo", searchStuNo);
            req.setAttribute("searchStuName", searchStuName);
            req.setAttribute("searchStartAge", searchStartAge);
            req.setAttribute("searchEndAge", searchEndAge);
            req.setAttribute("returnView", returnView);
            req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
            return;
        }

        // 检查是否经过过滤器处理
        String originalName = (String) req.getAttribute("originalName");
        if (originalName != null) {
            // 记录过滤信息
            System.out.println("[StudentServlet] 编辑学生 - 姓名已被过滤: \"" + originalName + "\" -> \"" + stuName + "\"");
        }
        
        // 如果用户没有上传新头像，则保留原有头像
        if (stuImg == null || stuImg.trim().isEmpty()) {
            // 查询原有学生信息，获取原有头像路径
            if (existingStu == null) {
                existingStu = studentService.getStuById(stuNo);
            }
            if (existingStu != null && existingStu.getStuImg() != null && !existingStu.getStuImg().trim().isEmpty()) {
                stuImg = existingStu.getStuImg();
                System.out.println("[StudentServlet] 编辑学生 - 保留原有头像: " + stuImg);
            } else {
                // 如果原有也没有头像，使用默认头像
                stuImg = "images/default-avatar.png";
                System.out.println("[StudentServlet] 编辑学生 - 使用默认头像");
            }
        }

        // 封装 POJO
        Student stu = new Student();
        stu.setStuNo(stuNo);
        stu.setStuName(stuName);
        stu.setStuAge(stuAge);
        stu.setStuImg(stuImg);  // 设置头像路径（新上传的或保留原有的）

        // 调: 调用业务层修改的方法
        int result = studentService.updateStu(stu);
        
        if (result <= 0) {
            req.setAttribute("msg", "修改失败！");
            existingStu = studentService.getStuById(stuNo);
            req.setAttribute("student", existingStu);
            req.setAttribute("pageNow", pageNow);
            req.setAttribute("searchStuNo", searchStuNo);
            req.setAttribute("searchStuName", searchStuName);
            req.setAttribute("searchStartAge", searchStartAge);
            req.setAttribute("searchEndAge", searchEndAge);
            req.setAttribute("returnView", returnView);
            req.getRequestDispatcher("updateStu.jsp").forward(req, resp);
            return;
        }

        // 构建查询参数用于重定向
        StringBuilder searchParams = new StringBuilder();
        if (searchStuNo != null && !searchStuNo.trim().isEmpty()) {
            searchParams.append("&stuNo=").append(searchStuNo);
        }
        if (searchStuName != null && !searchStuName.trim().isEmpty()) {
            searchParams.append("&stuName=").append(java.net.URLEncoder.encode(searchStuName, "UTF-8"));
        }
        if (searchStartAge != null && !searchStartAge.trim().isEmpty()) {
            searchParams.append("&startAge=").append(searchStartAge);
        }
        if (searchEndAge != null && !searchEndAge.trim().isEmpty()) {
            searchParams.append("&endAge=").append(searchEndAge);
        }

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
            if (pageNow == null || pageNow.trim().isEmpty()) {
                pageNow = "1";
            }
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
        
        Integer startAge = null;
        String startAgeStr = req.getParameter("startAge");
        if (startAgeStr != null && !startAgeStr.trim().isEmpty()) {
            try {
                startAge = Integer.parseInt(startAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }

        Integer endAge = null;
        String endAgeStr = req.getParameter("endAge");
        if (endAgeStr != null && !endAgeStr.trim().isEmpty()) {
            try {
                endAge = Integer.parseInt(endAgeStr);
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
        if (startAgeStr != null && !startAgeStr.trim().isEmpty()) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("startAge=").append(startAgeStr);
        }
        if (endAgeStr != null && !endAgeStr.trim().isEmpty()) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("endAge=").append(endAgeStr);
        }
        req.getSession().setAttribute("searchParams", searchParams.toString());
        
        // 调: 调用分页业务（带搜索条件）
        com.yf.util.PageBean page = studentService.getStuPage(pageNow, stuNo, stuName, startAge, endAge);
        
        // 把分页信息和查询条件保存到request的作用域当中
        req.setAttribute("page", page);
        req.setAttribute("searchStuNo", stuNoStr);
        req.setAttribute("searchStuName", stuName);
        req.setAttribute("searchStartAge", startAgeStr);
        req.setAttribute("searchEndAge", endAgeStr);
        
        // 跳转至分页页面 (请求转发)
        req.getRequestDispatcher("getStuPage.jsp").forward(req, resp);
    }
    
    // --- 7. 导出Excel ---
    private void exportExcel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("[StudentServlet] 开始导出Excel");
        
        // 获取查询条件
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
        
        Integer startAge = null;
        String startAgeStr = req.getParameter("startAge");
        if (startAgeStr != null && !startAgeStr.trim().isEmpty()) {
            try {
                startAge = Integer.parseInt(startAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }

        Integer endAge = null;
        String endAgeStr = req.getParameter("endAge");
        if (endAgeStr != null && !endAgeStr.trim().isEmpty()) {
            try {
                endAge = Integer.parseInt(endAgeStr);
            } catch (NumberFormatException e) {
                // 忽略无效的年龄参数
            }
        }
        
        // 根据查询条件获取学生列表
        List<Student> studentList = studentService.getAllStu(stuNo, stuName, startAge, endAge);
        
        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生信息");
        
        // 创建标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        // 创建数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"学号", "姓名", "年龄", "头像路径"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (Student stu : studentList) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(stu.getStuNo());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(stu.getStuName());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(stu.getStuAge());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(stu.getStuImg() != null ? stu.getStuImg() : "");
            cell3.setCellStyle(dataStyle);
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // 设置最小列宽
            sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 3000));
        }
        
        // 设置响应头
        String fileName = "学生信息_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
        // 处理中文文件名
        String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
        resp.setCharacterEncoding("UTF-8");
        
        // 将Excel写入响应流
        OutputStream out = resp.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
        
        System.out.println("[StudentServlet] Excel导出成功，共导出 " + studentList.size() + " 条记录");
    }
}
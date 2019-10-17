package club.search;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 上传个人图标
 */
@WebServlet("/IconUpload")
public class IconUpload extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload/icons";

    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;    // 3M
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;   // 40M
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50M

    public IconUpload(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(true);
        String adminID = "error";    // 获取登录信息
        String message;
        if(session.getAttribute("adminID") != null && !((String)session.getAttribute("adminID")).equals("null")){
            adminID = (String)session.getAttribute("adminID");
        }else{
            message = "游客登录或登录无效，请使用管理员账号登录";
            request.setAttribute("message", message);
            request.getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
        }

        // 检测是否为多媒体上传
        if(!ServletFileUpload.isMultipartContent(request)){
            message = "提交文件必须为多媒体";
            request.setAttribute("message", message);
            request.getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后产生临时文件
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // 设置最大请求值（包含文件和表单数据）
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // 中文处理
        upload.setHeaderEncoding("UTF-8");
        // 临时存储路径
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }

        try{
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
            if(formItems != null && formItems.size() > 0){
                // 迭代表单数据
                for(FileItem item : formItems){
                    // 处理不在表单中的字段
                    if(!item.isFormField()){
                        String oldFileName = new File(item.getName()).getName();
                        // 利用用户id，重命名文件
                        String fileName = adminID + oldFileName.substring(oldFileName.lastIndexOf("."));
                        String filePath = uploadPath + File.separator + fileName;
                        request.setAttribute("path", filePath);
                        File storeFile = new File(filePath);
                        // 文件已存在处理
                        if(storeFile.exists()){
                            message = "文件已存在，重新覆盖";
                            request.setAttribute("message", message);
                            // continue;
                        }
                        // 保存文件到硬盘
                        item.write(storeFile);
                        message = "文件上传成功";
                        request.setAttribute("message", message);
                        // 获取文件名
                        request.setAttribute("filename", fileName);
                    }
                }
            }
        }catch(Exception ex){
            message = "错误信息: " + ex.getMessage();
            request.setAttribute("message", message);
        }
        request.getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
    }
}
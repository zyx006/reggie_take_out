package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 12:59
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //使用UUID重新生成文件名
        String originalFilename = file.getOriginalFilename();
        String newFilename =  UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //判断目录是否存在，若不存在则创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + newFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(newFilename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fis = new FileInputStream(basePath + name);

            ServletOutputStream os = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1){
                os.write(buffer, 0, len);
                os.flush();
            }

            fis.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

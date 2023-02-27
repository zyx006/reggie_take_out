package cn.czyx007.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : 张宇轩
 * @createTime : 2022/12/19 - 23:04
 */
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching//开启SpringCache缓存功能
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
    }
}

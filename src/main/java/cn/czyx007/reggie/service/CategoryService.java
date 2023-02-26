package cn.czyx007.reggie.service;

import cn.czyx007.reggie.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 10:49
 */
public interface CategoryService extends IService<Category> {
    void remove(long id);
}

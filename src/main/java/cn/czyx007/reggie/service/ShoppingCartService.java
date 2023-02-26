package cn.czyx007.reggie.service;

import cn.czyx007.reggie.bean.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 20:36
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}

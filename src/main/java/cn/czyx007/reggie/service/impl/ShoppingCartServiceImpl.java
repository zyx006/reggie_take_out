package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.ShoppingCart;
import cn.czyx007.reggie.common.BaseContext;
import cn.czyx007.reggie.mapper.ShoppingCartMapper;
import cn.czyx007.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 20:36
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        this.remove(lqw);
    }
}

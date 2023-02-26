package cn.czyx007.reggie.service;

import cn.czyx007.reggie.bean.Orders;
import cn.czyx007.reggie.dto.OrdersDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 21:47
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);

    Page<OrdersDto> getPage(long page, long pageSize, LambdaQueryWrapper<Orders> lqw);
}

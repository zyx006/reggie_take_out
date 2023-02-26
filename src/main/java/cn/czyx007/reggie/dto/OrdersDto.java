package cn.czyx007.reggie.dto;

import cn.czyx007.reggie.bean.OrderDetail;
import cn.czyx007.reggie.bean.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 22:33
 */
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails;
}

package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.OrderDetail;
import cn.czyx007.reggie.mapper.OrderDetailMapper;
import cn.czyx007.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 21:48
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}

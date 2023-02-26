package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.bean.OrderDetail;
import cn.czyx007.reggie.bean.Orders;
import cn.czyx007.reggie.bean.ShoppingCart;
import cn.czyx007.reggie.common.BaseContext;
import cn.czyx007.reggie.common.R;
import cn.czyx007.reggie.dto.OrdersDto;
import cn.czyx007.reggie.service.OrderDetailService;
import cn.czyx007.reggie.service.OrdersService;
import cn.czyx007.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 21:49
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping(value = "/page")
    public R<Page<OrdersDto>> backendPageAllCondition(@RequestParam("page") Long page, @RequestParam("pageSize")Long pageSize,
          @RequestParam(value = "number", required = false) Long number,
          @RequestParam(value = "beginTime", required = false) LocalDateTime beginTime,
          @RequestParam(value = "endTime", required = false) LocalDateTime endTime){
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(number != null, Orders::getNumber, number);
        lqw.ge(beginTime != null, Orders::getOrderTime, beginTime);
        lqw.le(endTime != null, Orders::getOrderTime, endTime);
        lqw.orderByDesc(Orders::getOrderTime);

        return R.success(ordersService.getPage(page, pageSize, lqw));
    }

//    @GetMapping(value = "/page", params = "!number")
//    public R<Page<OrdersDto>> backendPageWithTime(@RequestParam("page") Long page, @RequestParam("pageSize")Long pageSize,
//          @RequestParam(value = "beginTime", required = false) LocalDateTime beginTime,
//          @RequestParam(value = "endTime", required = false) LocalDateTime endTime){
//        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
//        lqw.ge(beginTime != null, Orders::getOrderTime, beginTime);
//        lqw.le(endTime != null, Orders::getOrderTime, endTime);
//        lqw.orderByDesc(Orders::getOrderTime);
//
//        return R.success(ordersService.getPage(page, pageSize, lqw));
//    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> frontendPage(long page, long pageSize){
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId, BaseContext.getCurrentId());
        lqw.orderByDesc(Orders::getOrderTime);

        return R.success(ordersService.getPage(page, pageSize, lqw));
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Map<String,Long> map){
        shoppingCartService.clean();

        Long id = map.get("id");
        LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> details = orderDetailService.list(lqw);

        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = details.stream().map((item) -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(item, cart);
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            return cart;
        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(list);
        return R.success("喜欢吃就再来一单！");
    }
}

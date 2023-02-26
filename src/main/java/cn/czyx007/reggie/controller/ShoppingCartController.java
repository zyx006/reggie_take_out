package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.bean.ShoppingCart;
import cn.czyx007.reggie.common.BaseContext;
import cn.czyx007.reggie.common.CustomException;
import cn.czyx007.reggie.common.R;
import cn.czyx007.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 19:23
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lqw.orderByDesc(ShoppingCart::getCreateTime);
        return R.success(shoppingCartService.list(lqw));
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if (shoppingCart.getDishId() != null){
            //菜品
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);
        if (cartServiceOne != null) {//已存在重复的，数量+1
            cartServiceOne.setNumber(cartServiceOne.getNumber()+1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if (shoppingCart.getDishId() != null){
            //菜品
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);
        if(cartServiceOne == null){
            throw new CustomException("未知错误");
        }

        if(cartServiceOne.getNumber() > 1){
            cartServiceOne.setNumber(cartServiceOne.getNumber()-1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCartService.removeById(cartServiceOne);
        }

        return R.success("购物车减少成功");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        shoppingCartService.clean();
        return R.success("购物车清空成功");
    }
}

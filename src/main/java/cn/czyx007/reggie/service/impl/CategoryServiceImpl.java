package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.Category;
import cn.czyx007.reggie.bean.Dish;
import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.common.CustomException;
import cn.czyx007.reggie.mapper.CategoryMapper;
import cn.czyx007.reggie.service.CategoryService;
import cn.czyx007.reggie.service.DishService;
import cn.czyx007.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 10:49
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，需要先判断是否关联了菜品或套餐
     * @param id
     */
    @Override
    public void remove(long id) {
        //查询是否关联菜品，若关联则抛出业务异常
        LambdaQueryWrapper<Dish> dish = new LambdaQueryWrapper<>();
        dish.eq(Dish::getCategoryId, id);
        if (dishService.count(dish) > 0) {
            //已关联菜品，抛出业务异常
            throw new CustomException("当前分类已关联菜品，不能删除");
        }
        //查询是否关联套餐，若关联则抛出业务异常
        LambdaQueryWrapper<Setmeal> setmeal = new LambdaQueryWrapper<>();
        setmeal.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmeal) > 0) {
            //已关联套餐，抛出业务异常
            throw new CustomException("当前分类已关联套餐，不能删除");
        }
        //删除分类
        super.removeById(id);
    }
}

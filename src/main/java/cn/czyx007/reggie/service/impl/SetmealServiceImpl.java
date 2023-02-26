package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.DishFlavor;
import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.bean.SetmealDish;
import cn.czyx007.reggie.dto.SetmealDto;
import cn.czyx007.reggie.mapper.SetmealMapper;
import cn.czyx007.reggie.service.SetmealDishService;
import cn.czyx007.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 11:51
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);
        //保存关联信息
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map((item) -> {
           item.setSetmealId(setmealDto.getId());
           return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Override
    public SetmealDto getByIdWithDishes(long id) {
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, id);
        setmealDto.setSetmealDishes(setmealDishService.list(lqw));

        return setmealDto;
    }

    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        //更新套餐基本信息
        this.updateById(setmealDto);
        //清理当前套餐的菜品信息后重新添加
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(lqw);

        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(dishes);
    }
}

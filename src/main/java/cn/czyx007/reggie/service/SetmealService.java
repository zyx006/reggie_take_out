package cn.czyx007.reggie.service;

import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 11:50
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDishes(long id);

    void updateWithDishes(SetmealDto setmealDto);
}

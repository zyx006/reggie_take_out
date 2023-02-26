package cn.czyx007.reggie.service;

import cn.czyx007.reggie.bean.Dish;
import cn.czyx007.reggie.dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 11:50
 */
public interface DishService extends IService<Dish> {
    //新增菜品，同时插入对应的口味数据
    void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品信息和口味信息
    DishDto getByIdWithFlavor(long id);
    //更新菜品信息，同时更新口味信息
    void updateWithFlavor(DishDto dishDto);
}
